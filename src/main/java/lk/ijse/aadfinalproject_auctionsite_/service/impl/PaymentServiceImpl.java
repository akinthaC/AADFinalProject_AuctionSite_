package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.PaymentDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.Payment;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import lk.ijse.aadfinalproject_auctionsite_.repo.*;
import lk.ijse.aadfinalproject_auctionsite_.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private PurchaseRepo purchaseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmedItemListing farmedItemRepository;

    @Autowired
    private LandListingRepo landListingRepo;

    @Autowired
    private VehicleListingRepo vehicleListingRepo;
    @Autowired
    private EmailService emailService;


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

    @Override
    public Payment getPaymentByPurchaseId(String purchaseId) {
        return paymentRepo.findByPurchaseId(purchaseId);
    }

    @Override
    public long getCountByStatus(String hold) {
       return paymentRepo.countByPaymentStatus(hold);
    }

    public List<PaymentDTO> getAllPayments() {
        return paymentRepo.findAll().stream().map(payment -> {
            PaymentDTO dto = new PaymentDTO();
            dto.setPurchaseId(payment.getPurchase().getId()); // Assuming getPurchase() returns a Purchase entity
            dto.setAmount(payment.getAmount());
            dto.setPaymentStatus(payment.getPaymentStatus());
            dto.setPaymentMethod(payment.getPaymentMethod());
            dto.setListingId(payment.getListingId());
            dto.setListingType(payment.getListingType());
            dto.setPaymentDate(payment.getPaymentDate());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean confirmPayment(String orderId) {
        Payment payment = paymentRepo.findByPurchaseId(orderId);
        if (payment != null) {
            payment.setPaymentStatus("Success");
            paymentRepo.save(payment);

            Optional<FarmedItem> farmedItem = farmedItemRepository.findById(payment.getListingId());
            farmedItem.ifPresent(farmedItem1 -> {
                String email = farmedItem1.getUser().getEmail();
                Double price = payment.getAmount();

                // Construct the email message
                String subject = "Payment Confirmation";
                String message = String.format(
                        "<h3>Your payment has been successfully processed.</h3>" +
                                "<p><strong>Order ID:</strong> %s</p>" +
                                "<p><strong>Total Price:</strong> Rs. %.2f</p>" +
                                "<p>Thank you for your transaction. Your payment has been successfully processed and confirmed.</p>" +
                                "<p>Best regards,</p>" +
                                "<p>Your Auction Site Team</p>", orderId, price);

                // Send email to the user
                emailService.sendEmail(email, subject, message);
            });

            return true;
        }
        return false; // Return false if no payment found for the given orderId
    }


}
