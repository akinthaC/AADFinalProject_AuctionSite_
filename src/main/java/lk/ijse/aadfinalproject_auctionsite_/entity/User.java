package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;


    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;


    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String role; // Buyer or Seller

    private String status;


    private String description; // Dynamic textarea field


    private String addressLine1;


    private String addressLine2;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<FarmedItem> farmedItems;

    // Cart - List of items in the user's cart
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<AddToCart> cartItems;

    // Watchlist - List of items in the user's watchlist
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Watchlist> watchlistItems;

    // Purchase history
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Purchase> purchases;

    // Bid history
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PlaceBid> bids;

    private Double latitude;
    private Double longitude;



}