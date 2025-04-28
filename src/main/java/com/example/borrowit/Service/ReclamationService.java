package com.example.borrowit.Service;

import com.example.borrowit.DTO.DeliveryDTO;
import com.example.borrowit.DTO.ReclamationDTO;
import com.example.borrowit.DTO.ReclamationSearchCriteria;
import com.example.borrowit.Entity.Reclamation;
import com.example.borrowit.Entity.ServiceStatus;

import java.util.List;
import java.util.Optional;

public interface ReclamationService {

    Reclamation createReclamation(Reclamation reclamation, Long idUser, Long deliveryId);

    ReclamationDTO getReclamationById(Long id);

    List<ReclamationDTO> getAllReclamations();

    Reclamation updateReclamation(Long id, ServiceStatus status);

    boolean deleteReclamation(Long id);

    List<Reclamation> getAllReclamationsSorted(String sortBy, boolean ascending);

    List<Reclamation> searchReclamations(ReclamationSearchCriteria criteria);

    Boolean respondReclamation(Long id, String response);
    void updateReclamationStatus(Long id);

    List<ReclamationDTO> advancedSearch(String status, String itemName, String reclamationDateFrom, String reclamationDateTo);

    List<ReclamationDTO> getUserReclamations(Long idUser);
}
