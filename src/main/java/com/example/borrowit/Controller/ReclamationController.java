package com.example.borrowit.Controller;

import com.example.borrowit.DTO.DeliveryDTO;
import com.example.borrowit.DTO.ReclamationDTO;
import com.example.borrowit.DTO.ReclamationSearchCriteria;
import com.example.borrowit.Entity.Delivery;
import com.example.borrowit.Entity.Reclamation;
import com.example.borrowit.Entity.ServiceStatus;
import com.example.borrowit.Service.ImageUploadService;
import com.example.borrowit.Service.ReclamationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping(value = "/{userId}/{deliveryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Reclamation> createReclamation(
            @PathVariable Long userId,
            @PathVariable Long deliveryId,
            @RequestPart("reclamation") String reclamationJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Reclamation reclamation = objectMapper.readValue(reclamationJson, Reclamation.class);
            if (images != null && images.length > 0) {
                List<byte[]> imageBytesList = imageUploadService.saveImagesAsBytes(images);
                reclamation.setImageBytes(imageBytesList);
            }
            Reclamation saved = reclamationService.createReclamation(reclamation,userId,deliveryId);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/userReclamations/{userId}")
    public ResponseEntity<List<ReclamationDTO>> getMyReclamations(@PathVariable Long userId) {
        List<ReclamationDTO> reclamations = reclamationService.getUserReclamations(userId);
        return ResponseEntity.ok(reclamations);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReclamationDTO>> advancedSearch(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String reclamationDateFrom,
            @RequestParam(required = false) String reclamationDateTo) {

        List<ReclamationDTO> reclamations = reclamationService.advancedSearch(status, itemName, reclamationDateFrom, reclamationDateTo);
        return ResponseEntity.ok(reclamations);
    }

    @GetMapping
    public ResponseEntity<List<ReclamationDTO>> getAllReclamations() {
        List<ReclamationDTO> reclamations = reclamationService.getAllReclamations();
        return ResponseEntity.ok(reclamations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReclamationById(@PathVariable Long id) {
        ReclamationDTO reclamation = reclamationService.getReclamationById(id);
        if (reclamation == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Delivery not found for ID: " + id));
        }
        return ResponseEntity.ok(reclamation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> updateReclamation(
            @PathVariable Long id,
            @RequestBody ServiceStatus status
    ) {
        Reclamation updated = reclamationService.updateReclamation(id, status);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable Long id) {
        boolean deleted = reclamationService.deleteReclamation(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/search")
    public List<Reclamation> searchReclamations(@RequestBody ReclamationSearchCriteria criteria) {
        return reclamationService.searchReclamations(criteria);
    }

    @PutMapping("/respond/{id}")
    public ResponseEntity<String> respondToReclamation(
            @PathVariable Long id,
            @RequestBody String response) {

        boolean updated = reclamationService.respondReclamation(id, response);

        if (updated) {
            return ResponseEntity.ok("Réclamation traitée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réclamation non trouvée.");
        }
    }

    @PutMapping("/solved/{id}")
    public ResponseEntity<?> updateReclamationStatus(@PathVariable Long id) {
        try {
            reclamationService.updateReclamationStatus(id);
            return ResponseEntity.ok().body(Map.of("message", "Solved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Delivery not found for ID: " + id));
        }
    }
}
