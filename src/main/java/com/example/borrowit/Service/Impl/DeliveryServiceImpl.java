package com.example.borrowit.Service.Impl;

import com.example.borrowit.DTO.DeliveryDTO;
import com.example.borrowit.DTO.ReclamationDTO;
import com.example.borrowit.Entity.*;
import com.example.borrowit.Repository.DeliveryRepository;
import com.example.borrowit.Repository.ReclamationRepository;
import com.example.borrowit.Repository.RequestRepository;
import com.example.borrowit.Repository.UserRepository;
import com.example.borrowit.Service.DeliveryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ReclamationRepository reclamationRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    @Override
    public Delivery createDelivery(Delivery delivery, Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if(request != null) {
            delivery.setRequest(request);
            return deliveryRepository.save(delivery);
        }
        return null;
    }

    @Override
    public List<DeliveryDTO> getAllDeliveries() {
        return deliveryRepository.findAll()
                .stream()
                .map(DeliveryDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryDTO getDeliveryById(Long id) {
        DeliveryDTO delivery = deliveryRepository.findById(id).map(DeliveryDTO::new).orElse(null);
        if(delivery != null) {
            return delivery;
        }
        return null;
    }

    @Override
    public Delivery updateDelivery(Long id, Delivery delivery) {
        Delivery delivery1 = deliveryRepository.findById(id).orElse(null);
        if (delivery1 != null) {
            delivery1.setAddress(delivery.getAddress());
            return deliveryRepository.save(delivery1);
        }
        return null;
    }

    @Override
    public Optional<Delivery> updateDeliveryStatus(Long id, DeliveryStatus status) {
        Optional<Delivery> existing = deliveryRepository.findById(id);
        if (existing.isPresent()) {
            Delivery updated = existing.get();
            updated.setStatus(status);
            return Optional.of(deliveryRepository.save(updated));
        }
        return Optional.empty();
    }

    @Override
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
        List<Reclamation> reclamations = reclamationRepository.findByDeliveryId(deliveryId);
        reclamations.forEach(reclamation -> reclamationRepository.deleteById(reclamation.getId()));
        if (delivery.getRequest() != null) {
            Request request = delivery.getRequest();
            request.setDelivery(null);
            requestRepository.save(request);
        }
        deliveryRepository.deleteById(deliveryId);
    }

    @Override
    public List<Delivery> getAllDeliveriesSorted(String sortBy, boolean ascending) {
        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return deliveryRepository.findAll(sort);
    }

    @Override
    public List<DeliveryDTO> advancedSearch(String status, String address, String deliveryDateFrom, String deliveryDateTo) {
        List<DeliveryDTO> deliveries = deliveryRepository.findAll()
                .stream()
                .map(DeliveryDTO::new)
                .collect(Collectors.toList());

        if (status != null && !status.isEmpty()) {
            deliveries = filterByStatus(deliveries, status);
        }
        if (address != null && !address.isEmpty()) {
            deliveries = filterByAddress(deliveries, address);
        }
        if (deliveryDateFrom != null && !deliveryDateFrom.isEmpty()) {
            deliveries = filterByDateFrom(deliveries, deliveryDateFrom);
        }
        if (deliveryDateTo != null && !deliveryDateTo.isEmpty()) {
            deliveries = filterByDateTo(deliveries, deliveryDateTo);
        }

        return deliveries;
    }

    private List<DeliveryDTO> filterByStatus(List<DeliveryDTO> deliveries, String status) {
        return deliveries.stream()
                .filter(deliveryDTO -> deliveryDTO.getStatus().toString().toLowerCase().contains(status.toLowerCase())) // Case-insensitive search
                .collect(Collectors.toList());
    }

    private List<DeliveryDTO> filterByAddress(List<DeliveryDTO> deliveries, String address) {
        return deliveries.stream()
                .filter(deliveryDTO -> deliveryDTO.getAddress().toLowerCase().contains(address.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<DeliveryDTO> filterByDateFrom(List<DeliveryDTO> deliveries, String dateFrom) {
        LocalDate parsedDate = LocalDate.parse(dateFrom);
        return deliveries.stream()
                .filter(deliveryDTO -> !deliveryDTO.getCreatedAt().isBefore(parsedDate))
                .collect(Collectors.toList());
    }

    private List<DeliveryDTO> filterByDateTo(List<DeliveryDTO> deliveries, String dateTo) {
        LocalDate parsedDate = LocalDate.parse(dateTo);
        return deliveries.stream()
                .filter(deliveryDTO -> !deliveryDTO.getCreatedAt().isAfter(parsedDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryDTO> getUserDeliveries(Long idUser) {
        List<Delivery> deliveries = requestRepository.findDeliveriesByUserId(idUser);
        return deliveries
                .stream()
                .map(DeliveryDTO::new)
                .collect(Collectors.toList());
    }
}
