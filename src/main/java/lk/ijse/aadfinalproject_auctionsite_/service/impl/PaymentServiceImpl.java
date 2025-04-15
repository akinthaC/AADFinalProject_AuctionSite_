package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.PaymentDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Payment;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.repo.FarmedItemListing;
import lk.ijse.aadfinalproject_auctionsite_.repo.PaymentRepo;
import lk.ijse.aadfinalproject_auctionsite_.repo.PurchaseRepo;
import lk.ijse.aadfinalproject_auctionsite_.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private PurchaseRepo purchaseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void savePayment(PaymentDTO paymentDTO) {
        Purchase purchase = purchaseRepository.findById(paymentDTO.getPurchaseId())
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        // Map DTO to Entity
        Payment payment = new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setListingId(paymentDTO.getListingId());
        payment.setListingType(paymentDTO.getListingType());
        payment.setPurchase(purchase);

        // Save payment
        paymentRepo.save(payment);

    }
}
