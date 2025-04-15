package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartWatchListCountDTO;

import java.util.List;

public interface AddToCartService {
    AddToCartWatchListCountDTO getCountsByListingId(Long listingId);


    String addToCart(AddToCartDTO addToCartDTO);

    List<AddToCartDTO> getCartItemsByUserId(Long userId);
}
