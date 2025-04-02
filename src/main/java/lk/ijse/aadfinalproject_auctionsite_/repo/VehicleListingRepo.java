package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.entity.VehicleListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleListingRepo extends JpaRepository<VehicleListing, Long> {
    List<VehicleListing> findByStatusAndSellingOptionAndBidStartedDate(String pending, String bidding, LocalDate today);

    List<VehicleListing> findByStatusAndSellingOption(String active, String bidding);
}
