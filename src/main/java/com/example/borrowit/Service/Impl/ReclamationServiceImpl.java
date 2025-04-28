package com.example.borrowit.Service.Impl;

import com.example.borrowit.DTO.DeliveryDTO;
import com.example.borrowit.DTO.ReclamationDTO;
import com.example.borrowit.DTO.ReclamationSearchCriteria;
import com.example.borrowit.Entity.*;
import com.example.borrowit.Repository.DeliveryRepository;
import com.example.borrowit.Repository.ReclamationRepository;
import com.example.borrowit.Repository.RequestRepository;
import com.example.borrowit.Repository.UserRepository;
import com.example.borrowit.Service.ReclamationService;
import com.example.borrowit.Specification.ReclamationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReclamationServiceImpl implements ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;
    @Autowired
    private MailServiceImpl mailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public Reclamation createReclamation(Reclamation reclamation, Long idUser, Long deliveryId) {
        User user = userRepository.findById(idUser).orElse(null);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
        if(user != null && delivery != null) {
            reclamation.setUser(user);
            reclamation.setDelivery(delivery);
            reclamation.setCreatedAt(LocalDate.now());
            return reclamationRepository.save(reclamation);
        }
        return null;
    }

    @Override
    public ReclamationDTO getReclamationById(Long id) {
        ReclamationDTO reclamation = reclamationRepository.findById(id)
                .map(ReclamationDTO::new).orElse(null);
        if (reclamation != null) {
            return reclamation;
        }
        return null;
    }


    @Override
    public List<ReclamationDTO> getUserReclamations(Long idUser) {
        User user = userRepository.findById(idUser).get();
        List<Reclamation> reclamations = reclamationRepository.findReclamationByUser(user);
        return reclamations
                .stream()
                .map(ReclamationDTO::new)
                .collect(Collectors.toList());
    }


    @Override
    public List<ReclamationDTO> getAllReclamations() {
        return reclamationRepository.findAll()
                .stream()
                .map(ReclamationDTO::new)
                .collect(Collectors.toList());
    }
    @Override
    public Reclamation updateReclamation(Long id, ServiceStatus status) {
        Optional<Reclamation> optionalReclamation = reclamationRepository.findById(id);
        if (optionalReclamation.isPresent()) {
            Reclamation reclamation = optionalReclamation.get();
            reclamation.setStatus(status);
            Reclamation updatedReclamation = reclamationRepository.save(reclamation);

            if (status == ServiceStatus.RESOLVED &&
                    reclamation.getUser() != null &&
                    reclamation.getUser().getEmail() != null) {

                String to = reclamation.getUser().getEmail();
                String subject = "Reclamation Resolved";
                String text = "Dear user,\n\nWe are pleased to inform you that your reclamation has been resolved.\n\nThank you for your patience.";

                try {
                    mailService.sendSimpleMail(to, subject, text);
                } catch (Exception e) {
                    System.err.println("Error sending resolution email: " + e.getMessage());
                }
            }

            return updatedReclamation;
        }
        return null;
    }


    @Override
    public boolean deleteReclamation(Long id) {
        if (reclamationRepository.existsById(id)) {
            reclamationRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public List<Reclamation> getAllReclamationsSorted(String sortBy, boolean ascending) {
        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return reclamationRepository.findAll(sort);
    }

    @Override
    public List<Reclamation> searchReclamations(ReclamationSearchCriteria criteria) {
        Specification<Reclamation> spec = ReclamationSpecification.search(criteria);
        return reclamationRepository.findAll(spec);
    }

    @Override
    public Boolean respondReclamation(Long id, String response) {
        Optional<Reclamation> optionalReclamation = reclamationRepository.findById(id);
        if (optionalReclamation.isPresent()) {
            Reclamation reclamation = optionalReclamation.get();
            reclamation.setStatus(ServiceStatus.IN_PROGRESS);
            reclamation.setResponse(response);
            reclamation.setRespondedAt(LocalDate.now());
            reclamationRepository.save(reclamation);
            mailService.sendSimpleMail(
                    reclamation.getUser().getEmail(),
                    "Réponse à votre réclamation",
                    response
            );
            return true;
        }
        return false;
    }

    @Override
    public void updateReclamationStatus(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id).orElse(null);
        if (reclamation != null) {
            reclamation.setStatus(ServiceStatus.RESOLVED);
            reclamationRepository.save(reclamation);
            mailService.sendSimpleMail(
                    reclamation.getUser().getEmail(),
                    "Complaint resolved",
                    "We’re glad we resolved your complaint and hope to have met your expectations."
            );
        }
    }

    @Override
    public List<ReclamationDTO> advancedSearch(String status, String itemName, String deliveryDateFrom, String deliveryDateTo) {
        List<ReclamationDTO> reclamations = reclamationRepository.findAll()
                .stream()
                .map(ReclamationDTO::new)
                .collect(Collectors.toList());

        if (status != null && !status.isEmpty()) {
            reclamations = filterByStatus(reclamations, status);
        }
        if (itemName != null && !itemName.isEmpty()) {
            reclamations = filterByItemName(reclamations, itemName);
        }
        if (deliveryDateFrom != null && !deliveryDateFrom.isEmpty()) {
            reclamations = filterByDateFrom(reclamations, deliveryDateFrom);
        }
        if (deliveryDateTo != null && !deliveryDateTo.isEmpty()) {
            reclamations = filterByDateTo(reclamations, deliveryDateTo);
        }

        return reclamations;
    }

    private List<ReclamationDTO> filterByStatus(List<ReclamationDTO> reclamations, String status) {
        return reclamations.stream()
                .filter(reclamationDTO -> reclamationDTO.getStatus().toString().toLowerCase().contains(status.toLowerCase())) // Case-insensitive search
                .collect(Collectors.toList());
    }

    private List<ReclamationDTO> filterByItemName(List<ReclamationDTO> reclamations, String itemName) {
        return reclamations.stream()
                .filter(reclamation -> {
                    String currentItemName = reclamation.getItemName();
                    System.out.println("currentItemName: "+currentItemName);
                    System.out.println("check: "+ (currentItemName != null
                            && !currentItemName.isEmpty()
                            && currentItemName.toLowerCase().contains(itemName.toLowerCase())));
                    return currentItemName != null
                            && !currentItemName.isEmpty()
                            && currentItemName.toLowerCase().contains(itemName.toLowerCase());
                })
                .collect(Collectors.toList());
    }

    private List<ReclamationDTO> filterByDateFrom(List<ReclamationDTO> reclamations, String dateFrom) {
        LocalDate parsedDate = LocalDate.parse(dateFrom);
        return reclamations.stream()
                .filter(reclamationDTO -> !reclamationDTO.getCreatedAt().isBefore(parsedDate))
                .collect(Collectors.toList());
    }

    private List<ReclamationDTO> filterByDateTo(List<ReclamationDTO> reclamations, String dateTo) {
        LocalDate parsedDate = LocalDate.parse(dateTo);
        return reclamations.stream()
                .filter(reclamationDTO -> !reclamationDTO.getCreatedAt().isAfter(parsedDate))
                .collect(Collectors.toList());
    }
}
