package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.LandListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LandListingRepo extends JpaRepository<LandListing,Long> {

    List<LandListing> findByStatusAndAuctionStartDate(String pending, LocalDate today);

    List<LandListing> findByStatus(String active);
}
