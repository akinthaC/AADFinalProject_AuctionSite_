package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.PurchaseDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.repo.*;
import lk.ijse.aadfinalproject_auctionsite_.service.PurchaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PurchaseRepo purchaseRepo;

    @Autowired
    private FarmedItemListing farmedItemRepository;

    @Autowired
    private LandListingRepo landListingRepo;

    @Autowired
    private VehicleListingRepo vehicleListingRepo;
    @Override
    public void save(PurchaseDTO purchaseDTO) {
        Purchase purchase = modelMapper.map(purchaseDTO, Purchase.class);

        // Set auto-generated or non-user fields manually if needed
        purchase.setPurchaseDate(LocalDateTime.now());

        purchaseRepo.save(purchase);
        if ("FARMED".equalsIgnoreCase(purchaseDTO.getListingType())) {
            Optional<FarmedItem> optionalListing = farmedItemRepository.findById(purchaseDTO.getListingId());
            optionalListing.ifPresent(listing -> {
                Integer qty = listing.getQty();
                listing.setQty(listing.getQty() - purchaseDTO.getQuantity());
                listing.setSold(true);
                farmedItemRepository.save(listing);
            });

        }
    }

    @Override
    public String getLatestPurchaseId() {
        String latestPurchaseId = purchaseRepo.findLatestPurchaseId();
        return latestPurchaseId != null ? latestPurchaseId : "OR000";
    }

    @Override
    public List<Purchase> findByUserId(Long id) {
        List<Purchase> purchases = new ArrayList<>();
        purchases = purchaseRepo.findByUserId(id);
        return purchases;
    }

    @Override
    public List<Purchase> getPurchasesByListingIdAndType(Long id, String farmed) {
        return purchaseRepo.findByListingIdAndListingType(id, farmed);
    }

    public List<Map<String, Object>> getOrdersByDate() {
        List<Object[]> rawData = purchaseRepo.findOrderCountByDate();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rawData) {
            Map<String, Object> map = new HashMap<>();
            map.put("order_date", row[0].toString());
            map.put("total_orders", row[1]);
            result.add(map);
        }

        return result;
    }

    @Override
    public Long getPurchaseCount() {
        return purchaseRepo.count();
    }

    @Override
    public Purchase getPurchaseById(String purchaseId) {
        return purchaseRepo.findById(purchaseId).orElse(null);
    }


}
