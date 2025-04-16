package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.FarmedListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.LandListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.LandListing;

import java.util.List;

public interface LandListingService {
    LandListingDTO getItemById(Long id);

    List<LandListingDTO> getAllLandListing();

    LandListing updateItem(Long id, LandListingDTO landListingDTO);

    boolean saveLandListing(LandListingDTO landListingDTO);

    void deleteItem(Long id);

    List<LandListingDTO> getActiveLandListings();

    List<LandListingDTO> getPendingAuctionItems();
}
