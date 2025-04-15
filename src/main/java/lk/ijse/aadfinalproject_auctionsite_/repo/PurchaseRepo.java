package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.entity.PlaceBid;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepo extends JpaRepository<Purchase,String> {
    @Query("SELECT p.id FROM Purchase p ORDER BY p.id DESC LIMIT 1")
    String findLatestPurchaseId();

    List<Purchase> findByUserId(Long id);

    List<Purchase> findByListingIdAndListingType(Long id, String farmed);

    @Query("SELECT DATE(p.purchaseDate), COUNT(p) " +
            "FROM Purchase p GROUP BY DATE(p.purchaseDate) ORDER BY DATE(p.purchaseDate) ASC")
    List<Object[]> findOrderCountByDate();

}
