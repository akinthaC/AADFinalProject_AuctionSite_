package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepo extends JpaRepository<Delivery,Long> {
    Delivery findByPurchaseId(String id);
}
