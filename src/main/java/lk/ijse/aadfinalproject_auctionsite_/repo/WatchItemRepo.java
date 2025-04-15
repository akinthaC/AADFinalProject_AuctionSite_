package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.dto.WatchlistItemDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import lk.ijse.aadfinalproject_auctionsite_.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchItemRepo  extends JpaRepository<Watchlist , Long> {

    int countByListingIdAndListingType(Long listingId, String farmed);

    boolean existsByUserAndListingIdAndListingType(User user, Long listingItemId, String listingType);

    @Query("SELECT c FROM Watchlist c WHERE c.user.id = :id")
    List<Watchlist> findByUserId(Long id);
}
