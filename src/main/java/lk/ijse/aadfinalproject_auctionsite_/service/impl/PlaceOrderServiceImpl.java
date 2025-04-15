package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.*;
import lk.ijse.aadfinalproject_auctionsite_.entity.*;
import lk.ijse.aadfinalproject_auctionsite_.repo.*;
import lk.ijse.aadfinalproject_auctionsite_.service.PlaceOrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PlaceOrderServiceImpl implements PlaceOrderService {
    @Autowired
    private PlaceOrderRepo placeOrderRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmedItemListing farmedItemRepository;

    @Autowired
    private LandListingRepo landListingRepo;

    @Autowired
    private VehicleListingRepo vehicleListingRepo;

    @Autowired
    private PurchaseServiceImpl purchaseService;

    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Override
    public void savePlaceBid(BidDTO placeBid) {
        User user = userRepository.findById(String.valueOf(placeBid.getUserId()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        PlaceBid bid = modelMapper.map(placeBid, PlaceBid.class);
        placeOrderRepo.save(bid);

        // FARMED Items
        if ("FARMED".equalsIgnoreCase(placeBid.getListingType())) {
            Optional<FarmedItem> optionalListing = farmedItemRepository.findById(placeBid.getListingId());
            optionalListing.ifPresent(listing -> {
                List<Double> bidAmounts = listing.getBidAmounts();
                if (bidAmounts == null) bidAmounts = new ArrayList<>();
                bidAmounts.add(placeBid.getBidAmount());
                listing.setBidAmounts(bidAmounts);
                farmedItemRepository.save(listing);
            });
        }

        // Land Listings
        if ("Land".equalsIgnoreCase(placeBid.getListingType())) {
            Optional<LandListing> optionalListing = landListingRepo.findById(placeBid.getListingId());
            optionalListing.ifPresent(listing -> {
                List<Double> bidAmounts = listing.getBidAmounts();
                if (bidAmounts == null) bidAmounts = new ArrayList<>();
                bidAmounts.add(placeBid.getBidAmount());
                listing.setBidAmounts(bidAmounts);
                landListingRepo.save(listing);
            });
        }

        // Vehicle Listings
        if ("Vehicle".equalsIgnoreCase(placeBid.getListingType())) {
            Optional<VehicleListing> optionalListing = vehicleListingRepo.findById(placeBid.getListingId());
            optionalListing.ifPresent(listing -> {
                List<Double> bidAmounts = listing.getBidAmounts();
                if (bidAmounts == null) bidAmounts = new ArrayList<>();
                bidAmounts.add(placeBid.getBidAmount());
                listing.setBidAmounts(bidAmounts);
                vehicleListingRepo.save(listing);
            });
        }
    }


    @Override
    public ResponseDTO getBidsByListingId(Long listingId) {

        List<PlaceBid> bids = placeOrderRepo.findByListingIdOrderByBidTimeDesc(listingId);

        List<Map<String, Object>> bidResponses = bids.stream().map(bid -> {
            Map<String, Object> map = new HashMap<>();
            map.put("bidder", bid.getUser().getFirstName()); // or username if you prefer
            map.put("amount", bid.getBidAmount());
            map.put("time", bid.getBidTime());
            return map;
        }).collect(Collectors.toList());

        ResponseDTO response = new ResponseDTO();
        response.setCode(200);
        response.setMessage("Bids fetched successfully.");
        response.setData(Map.of("bids", bidResponses));

        return response;
    }

    @Override
    public ResponseDTO getBidsByUserId(Long id) {
        List<PlaceBid> bids = placeOrderRepo.findByUserId(id);
        List<AllListingDTO> allListingDTOs = new ArrayList<>();

        for (PlaceBid bid : bids) {
            if ("FARMED".equalsIgnoreCase(bid.getListingType())) {
                Optional<FarmedItem> optionalListing = farmedItemRepository.findById(bid.getListingId());
                optionalListing.ifPresent(listing -> {
                    if (listing.getCurrentWinningBidId() == bid.getId() && !listing.isSold()) {
                        AllListingDTO dto = new AllListingDTO();
                        dto.setListingID(listing.getId());
                        dto.setListingName(listing.getTitle());
                        dto.setPrice(bid.getBidAmount());
                        dto.setQty(Double.valueOf(listing.getQty()));
                        LocalDate winnerAssignedDate = listing.getWinnerAssignedDate(); // Assuming FarmedItem has a `getWinnerAssignedDate()` method

                        if (winnerAssignedDate != null) {
                            // Add 5 days to the winner assigned date
                            LocalDate completeDate = winnerAssignedDate.plusDays(5); // Add 5 days

                            // Calculate the number of days remaining to complete the payment
                            long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), completeDate);

                            // Set the remaining days as the "dateHave"
                            dto.setDateHave((int) remainingDays);
                        }

                        dto.setMainImage(listing.getMainImage()); // Assuming FarmedItem has a `getMainImage()` method

                        // Add the populated DTO to the list
                        allListingDTOs.add(dto);
                    }
                });
            }


            /*if ("LAND".equalsIgnoreCase(bid.getListingType())) {
                Optional<LandListing> optionalListing = landListingRepo.findById(bid.getListingId());
                optionalListing.ifPresent(listing -> {
                    if (listing.getCurrentWinningBidId() == bid.getId() && !listing.isSold()) {
                        AllListingDTO dto = new AllListingDTO();
                        dto.setListingID(listing.getId());
                        dto.setListingName(listing.getTitle());
                        dto.setPrice(listing.getPrice());
                        dto.setQty(Double.valueOf(listing.getQty()));
                        dto.setDateHave((int) (System.currentTimeMillis() / 1000)); // Example: current timestamp in seconds
                        dto.setMainImage(listing.getMainImage()); // Assuming FarmedItem has a `getMainImage()` method

                        // Add the populated DTO to the list
                        allListingDTOs.add(dto);
                    }
                });

            }*/
            /*if ("Vehicle".equalsIgnoreCase(bid.getListingType())) {
                Optional<VehicleListing> optionalListing = vehicleListingRepo.findById(bid.getListingId());
                optionalListing.ifPresent(listing -> {
                    if (listing.getCurrentWinningBidId() == bid.getId() && !listing.isSold()) {
                        AllListingDTO dto = new AllListingDTO();
                        dto.setListingID(listing.getId());
                        dto.setListingName(listing.getTitle());
                        dto.setPrice(listing.getPrice());
                        dto.setQty(Double.valueOf(listing.getQty()));
                        dto.setDateHave((int) (System.currentTimeMillis() / 1000)); // Example: current timestamp in seconds
                        dto.setMainImage(listing.getMainImage()); // Assuming FarmedItem has a `getMainImage()` method

                        // Add the populated DTO to the list
                        allListingDTOs.add(dto);
                    }
                });
            }*/

        }
        ResponseDTO response = new ResponseDTO();
        response.setCode(200);
        response.setMessage("Bids fetched successfully.");
        response.setData(Map.of("bids", allListingDTOs));

        return response;

    }

    @Override
    public ResponseDTO getPendingItems(Long id) {
        List<Purchase> purchases = purchaseService.findByUserId(id);
        List<AllListingDTO> allListingDTOs = new ArrayList<>();

        System.out.println(purchases.size());

        for (Purchase purchase : purchases) {
            if ("FARMED".equalsIgnoreCase(purchase.getListingType())) {
                Optional<FarmedItem> optionalListing = farmedItemRepository.findById(purchase.getListingId());
                optionalListing.ifPresent(listing -> {
                    Delivery delivery = deliveryService.findByPurchaseId(purchase.getId());
                    AllListingDTO dto = new AllListingDTO();
                    dto.setListingID(listing.getId());
                    dto.setListingName(listing.getTitle());
                    dto.setPrice(purchase.getTotalPrice());
                    dto.setQty(Double.valueOf(listing.getQty()));
                    dto.setStatus(delivery.getStatus());
                    dto.setOrderId(purchase.getId());
                    LocalDate deliveryAssignedDate = delivery.getDeliveryAssignedDate();
                    long predefinedDeliveryDuration = Long.parseLong(delivery.getDateCount());

                    if (predefinedDeliveryDuration > 0) {
                        LocalDate currentDate = LocalDate.now();

                        long daysPassed = ChronoUnit.DAYS.between(deliveryAssignedDate, currentDate);
                        System.out.println("Days passed since delivery assigned: " + daysPassed);

                        long daysRemaining = predefinedDeliveryDuration - daysPassed;

                        if (daysRemaining > 0) {
                            dto.setDateHave((int) daysRemaining);
                        } else {
                            dto.setDateHave(0);
                        }
                    }

                    dto.setMainImage(listing.getMainImage()); // Assuming FarmedItem has a `getMainImage()` method

                    // Add the populated DTO to the list
                    allListingDTOs.add(dto);
                });
            }
        }
        ResponseDTO response = new ResponseDTO();
        response.setCode(200);
        response.setMessage("Pending Items fetched successfully.");
        response.setData(Map.of("pending", allListingDTOs));

        return response;

    }

    @Override
    public ResponseDTO getPaidItems(Long id) {

        List<PaidListingDTO> paidListingDTOs = new ArrayList<>();

        List<FarmedItem> farmedItems = farmedItemRepository.findByUserId(id);
        System.out.println("Farmed items found: " + farmedItems.size());

        for (FarmedItem farmedItem : farmedItems) {
            List<Purchase> purchases = purchaseService.getPurchasesByListingIdAndType(farmedItem.getId(), "farmed");
            System.out.println("Purchases found: " + purchases.size());

            for (Purchase purchase : purchases) {
                Delivery delivery = deliveryService.findByPurchaseId(purchase.getId());

                if (delivery != null && "Pending".equalsIgnoreCase(delivery.getStatus())) {
                    PaidListingDTO dto = new PaidListingDTO();
                    dto.setListingId(purchase.getId());
                    dto.setListingName(farmedItem.getTitle());
                    dto.setQty(purchase.getQuantity());
                    dto.setMainImage(farmedItem.getMainImage());
                    dto.setStatus(delivery.getStatus());
                    dto.setDate(delivery.getDeliveryAssignedDate() != null ? delivery.getDeliveryAssignedDate().toString() : "N/A");
                    dto.setAddress1(delivery.getAddress1());
                    dto.setAddress2(delivery.getAddress2());
                    dto.setPostalCode(delivery.getPostalCode());

                    Optional<User> user = userRepository.findById(String.valueOf(purchase.getUser().getId()));
                    user.ifPresent(user1 -> {
                        dto.setUserName(user1.getFirstName() + " " + user1.getLastName());
                        dto.setUserId(String.valueOf(user1.getId()));
                        dto.setPhone(user1.getPhoneNumber());
                    });

                    paidListingDTOs.add(dto);
                }
            }
        }

        ResponseDTO response = new ResponseDTO();
        response.setCode(200);
        response.setMessage("Paid items fetched successfully.");
        response.setData(Map.of("paid", paidListingDTOs));

        return response;
    }



    @Override
    public ResponseDTO getShippedItems(Long id) {
        List<PaidListingDTO> paidListingDTOs = new ArrayList<>();

        List<FarmedItem> farmedItems = farmedItemRepository.findByUserId(id);
        for (FarmedItem farmedItem : farmedItems) {
            List<Purchase> purchases = purchaseService.getPurchasesByListingIdAndType(farmedItem.getId(), "farmed");
            for (Purchase purchase : purchases) {
                Delivery delivery = deliveryService.findByPurchaseId(purchase.getId());
                System.out.println("Delivery ID: " + delivery.getStatus());
                if (delivery.getTrackingNumber() != null) {
                    PaidListingDTO dto = new PaidListingDTO();
                    dto.setListingId(purchase.getId());
                    dto.setListingName(farmedItem.getTitle());
                    dto.setAddress1(delivery.getAddress1());
                    dto.setAddress2(delivery.getAddress2());
                    dto.setPostalCode(delivery.getPostalCode());
                    dto.setQty(purchase.getQuantity());
                    dto.setMainImage(farmedItem.getMainImage());
                    dto.setStatus(delivery.getStatus());

                    LocalDate deliveryAssignedDate = delivery.getDeliveryAssignedDate();
                    long predefinedDeliveryDuration = Long.parseLong(delivery.getDateCount());

                    if (predefinedDeliveryDuration > 0) {
                        LocalDate currentDate = LocalDate.now();

                        long daysPassed = ChronoUnit.DAYS.between(deliveryAssignedDate, currentDate);
                        System.out.println("Days passed since delivery assigned: " + daysPassed);

                        long daysRemaining = predefinedDeliveryDuration - daysPassed;

                        if (daysRemaining > 0) {
                            dto.setDate(String.valueOf((int) daysRemaining));
                        } else {
                            dto.setDate(String.valueOf(0));
                        }
                    }

                    Optional<User> user = userRepository.findById(String.valueOf(purchase.getUser().getId()));
                    user.ifPresent(user1 -> {
                        dto.setUserName(user1.getFirstName() + " " + user1.getLastName());
                        dto.setUserId(String.valueOf(user1.getId()));
                        dto.setPhone(user1.getPhoneNumber());
                    });
                    paidListingDTOs.add(dto);
                }
            }
        }

        ResponseDTO response = new ResponseDTO();
        response.setCode(200);
        response.setMessage("paid Items fetched successfully.");
        response.setData(Map.of("shipped", paidListingDTOs));

        return response;

    }

    @Override
    public ResponseDTO getDeliveredItems(Long id) {
        List<PaidListingDTO> paidListingDTOs = new ArrayList<>();

        List<FarmedItem> farmedItems = farmedItemRepository.findByUserId(id);
        for (FarmedItem farmedItem : farmedItems) {
            List<Purchase> purchases = purchaseService.getPurchasesByListingIdAndType(farmedItem.getId(), "farmed");

            for (Purchase purchase : purchases) {
                Delivery delivery = deliveryService.findByPurchaseId(purchase.getId());

                if (delivery != null && "Delivered".equalsIgnoreCase(delivery.getStatus())) {
                    PaidListingDTO dto = new PaidListingDTO();
                    dto.setListingId(purchase.getId());
                    dto.setListingName(farmedItem.getTitle());
                    dto.setAddress1(delivery.getAddress1());
                    dto.setAddress2(delivery.getAddress2());
                    dto.setPostalCode(delivery.getPostalCode());
                    dto.setQty(purchase.getQuantity());
                    dto.setMainImage(farmedItem.getMainImage());
                    dto.setStatus(delivery.getStatus());

                    // Handle remaining days calculation
                    LocalDate deliveryAssignedDate = delivery.getDeliveryAssignedDate();
                    String dateCountStr = delivery.getDateCount();

                    if (deliveryAssignedDate != null && dateCountStr != null && !dateCountStr.isEmpty()) {
                        try {
                            long predefinedDeliveryDuration = Long.parseLong(dateCountStr);
                            long daysPassed = ChronoUnit.DAYS.between(deliveryAssignedDate, LocalDate.now());
                            long daysRemaining = predefinedDeliveryDuration - daysPassed;

                            dto.setDate(String.valueOf(Math.max(daysRemaining, 0)));
                        } catch (NumberFormatException e) {
                            dto.setDate("0"); // fallback if dateCount is not a number
                        }
                    } else {
                        dto.setDate("0");
                    }

                    Optional<User> user = userRepository.findById(String.valueOf(purchase.getUser().getId()));
                    user.ifPresent(user1 -> {
                        dto.setUserName(user1.getFirstName() + " " + user1.getLastName());
                        dto.setUserId(String.valueOf(user1.getId()));
                        dto.setPhone(user1.getPhoneNumber());
                    });

                    paidListingDTOs.add(dto);
                }
            }
        }

        ResponseDTO response = new ResponseDTO();
        response.setCode(200);
        response.setMessage("Delivered items fetched successfully.");
        response.setData(Map.of("delivered", paidListingDTOs));

        return response;
    }

    @Override
    public ResponseDTO getAllItems() {
        HomePageCountDTO homePageCountDTO = new HomePageCountDTO();
        Long activeUserCount;
        Long activeSellerCount;
        Long activeOrderCount;
        Long totalListings;
        Long totalAdmins;

        activeUserCount = userRepository.countByRole("BUYER");
        activeSellerCount = userRepository.countByRole("Seller");
        Long mainAdmins = userRepository.countByRole("admin");
        Long admins = userRepository.countByRole("admins");
        activeOrderCount = purchaseService.getPurchaseCount();

        Long fCount= farmedItemRepository.count();
        Long vCount= vehicleListingRepo.count();
        Long lCount= landListingRepo.count();

        totalListings = fCount+vCount+lCount;
        totalAdmins=mainAdmins+admins;

        homePageCountDTO.setTotalListings(totalListings);
        homePageCountDTO.setTotalSellers(activeSellerCount);
        homePageCountDTO.setTotalUsers(activeUserCount);
        homePageCountDTO.setTotalOrders(activeOrderCount);
        homePageCountDTO.setTotalAdmins(totalAdmins);

        ResponseDTO response = new ResponseDTO();
        response.setCode(200);
        response.setMessage("Delivered items fetched successfully.");
        response.setData(Map.of("all", homePageCountDTO));

        return response;

    }

}



