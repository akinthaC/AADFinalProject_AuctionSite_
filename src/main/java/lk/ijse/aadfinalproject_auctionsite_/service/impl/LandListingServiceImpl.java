package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.FarmedListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.LandListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.LandListing;
import lk.ijse.aadfinalproject_auctionsite_.repo.LandListingRepo;
import lk.ijse.aadfinalproject_auctionsite_.service.LandListingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LandListingServiceImpl implements LandListingService {
    @Autowired
    private LandListingRepo landListingRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public LandListingDTO getItemById(Long id) {
        Optional<LandListing> itemOptional = landListingRepo.findById((id));
        System.out.println(itemOptional.get());
        return itemOptional.map(item -> modelMapper.map(item, LandListingDTO.class)).orElse(null);
    }

    @Override
    public List<LandListingDTO> getAllLandListing() {
        List<LandListing> landListings = landListingRepo.findAll();

        return landListings.stream().map(landListing -> {
            // Convert the FarmedItem to DTO using ModelMapper
            LandListingDTO dto = modelMapper.map(landListing, LandListingDTO.class);

            // Convert Hibernate PersistentBag to a List<String> if otherImages is not null
            if (landListing.getOtherImages() != null) {
                List<String> imagePaths = landListing.getOtherImages()
                        .stream()
                        .collect(Collectors.toList()); // Already Strings, so no need for transformation
                dto.setOtherImages(imagePaths);
            }

            // Convert Hibernate PersistentBag to a List<Double> if bidAmounts is not null
            if (landListing.getBidAmounts() != null) {
                List<Double> bidAmountsList = landListing.getBidAmounts()
                        .stream()
                        .collect(Collectors.toList()); // Collect the bid amounts into a List<Double>
                dto.setBidAmounts(bidAmountsList);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public LandListing updateItem(Long id, LandListingDTO landListingDTO) {
        Optional<LandListing> existingItemOpt = landListingRepo.findById(id);
        if (existingItemOpt.isPresent()) {
            LandListing existingItem = existingItemOpt.get();
            System.out.println(id);


            existingItem.setId(id);

            existingItem.setLandName(landListingDTO.getLandName());
            existingItem.setLocation(landListingDTO.getLocation());
            existingItem.setCurrentUse(landListingDTO.getCurrentUse());
            existingItem.setDetailedDescription(landListingDTO.getDetailedDescription());
            existingItem.setSize(landListingDTO.getSize());
            existingItem.setTopography(landListingDTO.getTopography());
            existingItem.setWaterAvailability(landListingDTO.getWaterAvailability());
            existingItem.setAccess(landListingDTO.getAccess());
            existingItem.setSoilQuality(landListingDTO.getSoilQuality());
            existingItem.setFeatures(landListingDTO.getFeatures());
            existingItem.setStatus(landListingDTO.getStatus());
            existingItem.setDeletes(landListingDTO.getDeletes());
            existingItem.setMainImage(landListingDTO.getMainImage());
            existingItem.setOtherImages(landListingDTO.getOtherImages());
            existingItem.setPaymentTerms(landListingDTO.getPaymentTerms());
            existingItem.setAuctionStartDate(landListingDTO.getAuctionStartDate());
            existingItem.setListingDate(landListingDTO.getListingDate());
            existingItem.setPrice(landListingDTO.getPrice());
            existingItem.setStartPrice(landListingDTO.getStartPrice());
            existingItem.setReservePrice(landListingDTO.getReservePrice());
            existingItem.setAuctionDuration(landListingDTO.getAuctionDuration());

            return landListingRepo.save(existingItem);
        } else {
            return null;
        }

    }

    @Override
    public boolean saveLandListing(LandListingDTO landListingDTO) {
        try {
            // Convert DTO to entity
            LandListing landListing = modelMapper.map(landListingDTO, LandListing.class);
            landListingRepo.save(landListing);  // Save to DB
            return true;
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteItem(Long id) {
        if (landListingRepo.existsById(id)) {
            landListingRepo.deleteById(id);
        } else {
            throw new RuntimeException("Item not found with ID: " + id);
        }
    }

    @Override
    public List<LandListingDTO> getActiveLandListings() {
        List<LandListing> activeListings = landListingRepo.findByStatus("Active");
        return activeListings.stream()
                .map(landListing -> modelMapper.map(landListing, LandListingDTO.class))  // Convert using ModelMapper
                .collect(Collectors.toList());
    }

    @Override
    public List<LandListingDTO> getPendingAuctionItems() {
        List<LandListing> pendingItems = landListingRepo.findByStatus("PENDING");
        return pendingItems.stream()
                .map(item -> modelMapper.map(item, LandListingDTO.class))
                .collect(Collectors.toList());
    }
}
