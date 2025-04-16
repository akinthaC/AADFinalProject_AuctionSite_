package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount; // Total payment amount

    @Column(nullable = false, length = 50)
    private String paymentMethod; // E.g., Credit Card, PayPal, Cash, etc.

    @Column(nullable = false, length = 20)
    private String paymentStatus; // Pending, Completed, Failed, etc.

    private LocalDateTime paymentDate; // Date and time when payment was made

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase; // Link to the purchase

    @Column(nullable = false)
    private Long listingId; // ID of the listing being bid on

    @Column(nullable = false)
    private String listingType;
    @PrePersist
    protected void onCreate() {
        paymentDate = LocalDateTime.now(); // Automatically set payment date when saved
    }
}
