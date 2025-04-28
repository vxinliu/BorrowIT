package com.example.borrowit.Service;

import com.example.borrowit.DTO.DeliveryDTO;
import com.example.borrowit.Entity.Delivery;
import com.example.borrowit.Entity.DeliveryStatus;

import java.util.List;
import java.util.Optional;

public interface DeliveryService {
    Delivery createDelivery(Delivery delivery,Long requestId);
    List<DeliveryDTO> getAllDeliveries();
    DeliveryDTO getDeliveryById(Long id);
    Optional<Delivery> updateDeliveryStatus(Long id, DeliveryStatus status);
    void deleteDelivery(Long id);
    Delivery updateDelivery(Long id, Delivery delivery);
    List<Delivery> getAllDeliveriesSorted(String sortBy, boolean ascending);
    List<DeliveryDTO> advancedSearch(String status, String itemName, String reclamationDateFrom, String reclamationDateTo);
    List<DeliveryDTO> getUserDeliveries(Long idUser);
}
