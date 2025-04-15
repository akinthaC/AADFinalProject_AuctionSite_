package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartWatchListCountDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.*;
import lk.ijse.aadfinalproject_auctionsite_.repo.*;
import lk.ijse.aadfinalproject_auctionsite_.service.AddToCartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
 public class AddToCartServiceImpl implements AddToCartService {
    @Autowired
    private AddToCartRepo addToCartRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WatchItemRepo watchItemRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmedItemListing farmedItemRepository;

    @Autowired
    private LandListingRepo landListingRepo;

    @Autowired
    private VehicleListingRepo vehicleListingRepo;

    @Override
    public AddToCartWatchListCountDTO getCountsByListingId(Long listingId) {
        int cartCount = addToCartRepo.countByListingIdAndListingType(listingId, "farmed");
        int watchlistCount = watchItemRepo.countByListingIdAndListingType(listingId,"farmed");

        AddToCartWatchListCountDTO dto = new AddToCartWatchListCountDTO();
        dto.setListingId(listingId);
        dto.setCartCount(cartCount);
        dto.setWatchlistCount(watchlistCount);

        return dto;
    }

    @Override
    public String addToCart(AddToCartDTO dto) {
        User user = userRepository.findById(String.valueOf(dto.getUserId()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyExists = addToCartRepo.existsByUserAndListingIdAndListingType(user, dto.getListingItemId(), dto.getListingType());

        if (alreadyExists) {
            return "EXISTS";
        }


        AddToCart cart = modelMapper.map(dto, AddToCart.class);
        cart.setUser(user);
        cart.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 1); // default to 1
        addToCartRepo.save(cart);

        if ("FARMED".equalsIgnoreCase(dto.getListingType())) {
            Optional<FarmedItem> optionalListing = farmedItemRepository.findById(dto.getListingItemId());
            optionalListing.ifPresent(listing -> {
                Integer currentCount = listing.getToCart();
                listing.setToCart(currentCount != null ? currentCount + 1 : 1);
                farmedItemRepository.save(listing);
            });
        }

        if ("Land".equalsIgnoreCase(dto.getListingType())) {
            Optional<LandListing> optionalListing = landListingRepo.findById(dto.getListingItemId());
            optionalListing.ifPresent(listing -> {
                Integer currentCount = listing.getToCart();
                listing.setToCart(currentCount != null ? currentCount + 1 : 1);
                landListingRepo.save(listing);
            });
        }

        if ("Vehicle".equalsIgnoreCase(dto.getListingType())) {
            Optional<VehicleListing> optionalListing = vehicleListingRepo.findById(dto.getListingItemId());
            optionalListing.ifPresent(listing -> {
                Integer currentCount = listing.getToCart();
                listing.setToCart(currentCount != null ? currentCount + 1 : 1);
                vehicleListingRepo.save(listing);
            });
        }

        return "ADDED";
    }

    @Override
    public List<AddToCartDTO> getCartItemsByUserId(Long userId) {
        return addToCartRepo.findByUserId(userId).stream()
                .map(cart -> new AddToCartDTO(
                        cart.getId(),
                        cart.getUser().getId(),
                        cart.getListingId(),
                        cart.getListingType(),
                        cart.getQuantity()
                ))
                .collect(Collectors.toList());
    }


}
