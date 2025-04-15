package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.WatchlistItemDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.*;
import lk.ijse.aadfinalproject_auctionsite_.repo.*;
import lk.ijse.aadfinalproject_auctionsite_.service.WatchItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchItemServiceImpl implements WatchItemService {
    @Autowired
    private WatchItemRepo watchItemRepo;

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

    @Override
    public String addToWatchList(WatchlistItemDTO dto) {
        User user = userRepository.findById(String.valueOf(dto.getUserId()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyExists = watchItemRepo.existsByUserAndListingIdAndListingType(user, dto.getListingItemId(), dto.getListingType());

        if (alreadyExists) {
            return "EXISTS";
        }


        Watchlist cart = modelMapper.map(dto, Watchlist.class);
        cart.setUser(user);
        watchItemRepo.save(cart);

        if ("FARMED".equalsIgnoreCase(dto.getListingType())) {
            Optional<FarmedItem> optionalListing = farmedItemRepository.findById(dto.getListingItemId());
            optionalListing.ifPresent(listing -> {
                Integer currentCount = listing.getToCart();
                listing.setWatchedItem(currentCount != null ? currentCount + 1 : 1);
                farmedItemRepository.save(listing);
            });
        }

        if ("Land".equalsIgnoreCase(dto.getListingType())) {
            Optional<LandListing> optionalListing = landListingRepo.findById(dto.getListingItemId());
            optionalListing.ifPresent(listing -> {
                Integer currentCount = listing.getToCart();
                listing.setWatchedItem(currentCount != null ? currentCount + 1 : 1);
                landListingRepo.save(listing);
            });
        }

        if ("Vehicle".equalsIgnoreCase(dto.getListingType())) {
            Optional<VehicleListing> optionalListing = vehicleListingRepo.findById(dto.getListingItemId());
            optionalListing.ifPresent(listing -> {
                Integer currentCount = listing.getToCart();
                listing.setWatchedItem(currentCount != null ? currentCount + 1 : 1);
                vehicleListingRepo.save(listing);
            });
        }

        return "ADDED";
    }

    @Override
    public List<WatchlistItemDTO> getWatchlistItemsByUserId(Long id) {
        return watchItemRepo.findByUserId(id).stream()
                .map(cart -> new WatchlistItemDTO(
                        cart.getId(),
                        cart.getUser().getId(),
                        cart.getListingId(),
                        cart.getListingType()
                ))
                .collect(Collectors.toList());
    }

}
