package com.example.borrowit.DTO;

import com.example.borrowit.Entity.Reclamation;
import com.example.borrowit.Entity.ServiceStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReclamationDTO {

    private Long id;
    private String issueDescription;
    private ServiceStatus status;
    private String response;
    private LocalDate createdAt;
    private LocalDate respondedAt;
    private List<byte[]> imageBytes;
    private Long userId;
    private String userName;
    private Long itemId;
    private String itemName;

    public ReclamationDTO(Reclamation reclamation) {
        this.id = reclamation.getId();
        this.issueDescription = reclamation.getIssueDescription();
        this.status = reclamation.getStatus();
        this.response = reclamation.getResponse();
        this.createdAt = reclamation.getCreatedAt();
        this.respondedAt = reclamation.getRespondedAt();
        this.imageBytes = reclamation.getImageBytes();

        if (reclamation.getUser() != null) {
            this.userId = reclamation.getUser().getId();
            this.userName = reclamation.getUser().getName();
        }

        if (reclamation.getDelivery() != null) {
            this.itemId = reclamation.getDelivery().getRequest().getItem().getId();
            this.itemName = reclamation.getDelivery().getRequest().getItem().getName();
        }
    }
}
