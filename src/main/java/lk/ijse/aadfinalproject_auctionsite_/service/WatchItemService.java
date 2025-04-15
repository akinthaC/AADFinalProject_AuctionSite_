package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.WatchlistItemDTO;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.WatchItemServiceImpl;

import java.util.List;

public interface WatchItemService {
    String addToWatchList(WatchlistItemDTO watchlistItemDTO);

    List<WatchlistItemDTO> getWatchlistItemsByUserId(Long id);
}
