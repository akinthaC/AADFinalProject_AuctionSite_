package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Farmedlistings")
public class FarmedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private String miniDescription;

    @Lob
    private String detailedDescription;

    private String sellType; // Fixed, Bidding, Both
    private Double price;
    private Double startingBid;
    private Integer bidDuration;

    private Integer qty;
    private String landDesk;
    private String status;
    private String deletes;


    private String mainImage;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> otherImages;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Double> bidAmounts;


    private boolean termsAccepted;
    private Integer toCart;
    private Integer watchedItem;

    private LocalDate bidStartedDate;  // Stores the date when bidding starts
    private LocalDateTime listingDate;     // Stores the date when the listing was created

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @PrePersist
    protected void onCreate() {
        listingDate = LocalDateTime.now(); // Automatically set listing date when saved
    }
}
