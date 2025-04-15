package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.AddToCart;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddToCartRepo extends JpaRepository<AddToCart, Long> {

    int countByListingIdAndListingType(Long listingId, String farmed);

    boolean existsByUserAndListingIdAndListingType(User user, Long listingItemId, String listingType);

    @Query("SELECT c FROM AddToCart c WHERE c.user.id = :userId")
    List<AddToCart> findByUserId(Long userId);

}
