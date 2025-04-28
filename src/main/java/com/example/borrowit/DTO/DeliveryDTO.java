package com.example.borrowit.DTO;

import com.example.borrowit.Entity.Delivery;
import com.example.borrowit.Entity.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class DeliveryDTO {

    private Long id;
    private String address;
    private DeliveryStatus status;
    private LocalDate deliveryDate;
    private LocalDate createdAt;

    private Long itemId;
    private String itemName;
    private String itemDescription;

    public DeliveryDTO(Delivery delivery) {
        this.id = delivery.getId();
        this.address = delivery.getAddress();
        this.status = delivery.getStatus();
        this.deliveryDate = delivery.getDeliveryDate();
        this.createdAt = delivery.getCreatedAt();

        if (delivery.getRequest() != null) {
            this.itemId = delivery.getRequest().getItem().getId();
            this.itemName = delivery.getRequest().getItem().getName();
            this.itemDescription = delivery.getRequest().getItem().getDescription();
        }
    }
}
