package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.PurchaseDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PurchaseService {
    void save(PurchaseDTO purchase);

    String getLatestPurchaseId();


    List<Purchase> findByUserId(Long id);

    List<Purchase> getPurchasesByListingIdAndType(Long id, String farmed);



    List<Map<String, Object>> getOrdersByDate();

    Long getPurchaseCount();
}
