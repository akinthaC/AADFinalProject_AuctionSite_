package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.AllListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.BidDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.ResponseDTO;

import java.util.List;

public interface PlaceOrderService {
    void savePlaceBid(BidDTO placeBid);

    ResponseDTO getBidsByListingId(Long listingId);


    ResponseDTO getBidsByUserId(Long id);

    ResponseDTO getPendingItems(Long id);

    ResponseDTO getPaidItems(Long id);

    ResponseDTO getShippedItems(Long id);

    ResponseDTO getDeliveredItems(Long id);

    ResponseDTO getAllItems();
}
