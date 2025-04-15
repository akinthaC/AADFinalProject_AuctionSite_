package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.entity.PlaceBid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceOrderRepo extends JpaRepository<PlaceBid,Long> {
    List<PlaceBid> findByListingIdOrderByBidTimeDesc(Long listingId);

    @Query("SELECT p FROM PlaceBid p WHERE p.listingId = :listingId AND p.listingType = :listingType ORDER BY p.bidAmount DESC")
    List<PlaceBid> findTop3Bids(@Param("listingId") Long listingId, @Param("listingType") String listingType, Pageable pageable);

    List<PlaceBid> findByUserId(Long id);
}
