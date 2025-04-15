package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class AddToCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long listingId; // ID of the listing being bid on

    @Column(nullable = false)
    private String listingType;

    @Column(nullable = false)
    private Integer quantity; // Number of items added to cart

    @Column(nullable = false)
    private LocalDateTime addedAt; // Timestamp of adding to cart

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
