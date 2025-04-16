package lk.ijse.aadfinalproject_auctionsite_.dto;

import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.entity.Payment;
import lk.ijse.aadfinalproject_auctionsite_.entity.Delivery;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {
    private PurchaseDTO purchase;
    private PaymentDTO payment;
    private DeliveryDto delivery;
    private UserDTO user;

}
