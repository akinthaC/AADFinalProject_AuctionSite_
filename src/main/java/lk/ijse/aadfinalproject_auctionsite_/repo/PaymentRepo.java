package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
}
