package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FarmedItemListing extends JpaRepository<FarmedItem, Long> {
    List<FarmedItem> findByStatus(String status);


    List<FarmedItem> findByStatusAndSellTypeAndBidStartedDate(String pending, String bidding, LocalDate today);

    List<FarmedItem> findByStatusAndSellType(String active, String bidding);
}
