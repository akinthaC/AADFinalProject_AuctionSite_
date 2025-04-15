package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "purchase_id_generator")
    @GenericGenerator(name = "purchase_id_generator", strategy = "lk.ijse.aadfinalproject_auctionsite_.entity.PurchaseIdGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long listingId; // ID of the listing being bid on

    @Column(nullable = false)
    private String listingType;

    private Integer quantity; // Quantity of the purchased item

    @Column(nullable = false)
    private Double totalPrice; // Total price for the purchase

    @Column(nullable = false)
    private LocalDateTime purchaseDate; // Date and time when the purchase was made

    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Payment> payments; // Payments associated with this purchase

    @PrePersist
    protected void onCreate() {
        purchaseDate = LocalDateTime.now(); // Automatically set purchase date when saved
    }
}
