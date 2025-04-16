package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.PaymentDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Payment;

import java.util.List;

public interface PaymentService {


    void savePayment(PaymentDTO paymentDTO);

    Payment getPaymentByPurchaseId(String purchaseId);

    long getCountByStatus(String hold);

    List<PaymentDTO> getAllPayments();

    boolean confirmPayment(String orderId);
}
