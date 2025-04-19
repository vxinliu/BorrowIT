package com.example.borrowit.Entity;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Feedback implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime date;

    @ManyToOne
    private Item item;
    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Reacts> reacts;

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Item getItem() {
        return item;
    }

    public List<Reacts> getReacts() {
        return reacts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setReacts(List<Reacts> reacts) {
        this.reacts = reacts;
    }
    private boolean reported;

    // getters and setters for reported
    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }

    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
