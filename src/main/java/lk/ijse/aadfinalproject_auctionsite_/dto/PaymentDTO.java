package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String purchaseId;
    private Double amount;
    private String paymentStatus;
    private String paymentMethod;
    private Long listingId;
    private String listingType;
    private LocalDateTime paymentDate; // Date and time when payment was made

    public PaymentDTO(Double amount, String paymentStatus, String paymentMethod, LocalDateTime paymentDate) {
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }
}
