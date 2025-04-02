package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.FarmedListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;

public interface FarmedItemService {
    public  boolean saveFarmedItem(FarmedListingDTO farmedListingDTO);

    List<FarmedListingDTO> getActiveAuctionItems();

    List<FarmedListingDTO> getAllFarmedListing();

    FarmedListingDTO getItemById(Long id);

    FarmedItem updateItem(Long id, FarmedListingDTO farmedItemDTO);

    void deleteItem(Long id);

    List<FarmedItem> getActiveListings();

    List<FarmedListingDTO> getActiveFarmedListings();

    FarmedListingDTO getListingById(Long id) throws ChangeSetPersister.NotFoundException;
}
