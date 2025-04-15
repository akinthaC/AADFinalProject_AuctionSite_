package lk.ijse.aadfinalproject_auctionsite_.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inquiry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;  // Reference to the related order

    private String email;  // Reference to the related order

    @Column(nullable = false, length = 1000)
    private String message;  // Mini description / inquiry text

    @Column(name = "inquiry_date", nullable = false)
    private LocalDateTime inquiryDate;

    @PrePersist
    protected void onCreate() {
        this.inquiryDate = LocalDateTime.now();
    }
}
