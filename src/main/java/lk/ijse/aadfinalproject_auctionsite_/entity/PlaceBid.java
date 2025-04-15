package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "bids")
public class PlaceBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Long listingId; // ID of the listing being bid on

    @Column(nullable = false)
    private String listingType;

    @Column(nullable = false)
    private Double bidAmount; // Amount of the bid

    @Column(nullable = false)
    private LocalDateTime bidTime; // When the bid was placed

    @PrePersist
    protected void onCreate() {
        bidTime = LocalDateTime.now(); // Set bid time automatically
    }
}
