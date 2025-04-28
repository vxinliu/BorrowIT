package com.example.borrowit.Controller;

import com.example.borrowit.DTO.DeliveryDTO;
import com.example.borrowit.Entity.Delivery;
import com.example.borrowit.Entity.DeliveryStatus;
import com.example.borrowit.Service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/{requestId}")
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery, @PathVariable Long requestId) {
        return ResponseEntity.ok(deliveryService.createDelivery(delivery,requestId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DeliveryDTO>> advancedSearch(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String deliveryDateFrom,
            @RequestParam(required = false) String deliveryDateTo) {

        List<DeliveryDTO> deliveries = deliveryService.advancedSearch(status, address, deliveryDateFrom, deliveryDateTo);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDeliveryById(@PathVariable Long id) {
        DeliveryDTO delivery = deliveryService.getDeliveryById(id);
        if (delivery == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Delivery not found for ID: " + id));
        }
        return ResponseEntity.ok(delivery);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        List<DeliveryDTO> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDelivery(
            @PathVariable Long id,
            @RequestBody Delivery delivery
    ) {
        try {
            Delivery updated = deliveryService.updateDelivery(id, delivery);
            if (updated == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Delivery not found with id: " + id));
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to update delivery: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<Delivery> updateDeliveryStatus(
            @PathVariable Long id,
            @PathVariable DeliveryStatus status
    ) {
        Optional<Delivery> updated = deliveryService.updateDeliveryStatus(id, status);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDelivery(@PathVariable Long id) {
        try {
            System.out.println("id: "+ id);
            deliveryService.deleteDelivery(id);
            return ResponseEntity.ok(Map.of("message", "Delivery deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to delete delivery: " + e.getMessage()));
        }
    }

    @GetMapping("/userDeliveries/{userId}")
    public ResponseEntity<List<DeliveryDTO>> getMyDeliveries(@PathVariable Long userId) {
        List<DeliveryDTO> deliveries = deliveryService.getUserDeliveries(userId);
        return ResponseEntity.ok(deliveries);
    }
}
