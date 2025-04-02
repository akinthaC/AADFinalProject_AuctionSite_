package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.FarmedListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.repo.FarmedItemListing;
import lk.ijse.aadfinalproject_auctionsite_.service.FarmedItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmedItemServiceImpl implements  FarmedItemService {
    @Autowired
    private FarmedItemListing farmedItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean saveFarmedItem(FarmedListingDTO farmedItemDTO) {
        try {
            // Convert DTO to entity
            FarmedItem farmedItem = modelMapper.map(farmedItemDTO, FarmedItem.class);
            farmedItemRepository.save(farmedItem);  // Save to DB
            return true;
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return false;
        }
    }


    public List<FarmedListingDTO> getActiveAuctionItems() {
        List<FarmedItem> activeItems = farmedItemRepository.findByStatus("ACTIVE");
        return activeItems.stream()
                .map(item -> modelMapper.map(item, FarmedListingDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FarmedListingDTO> getAllFarmedListing() {
        // Fetch all farmed items from the database
        List<FarmedItem> farmedItems = farmedItemRepository.findAll();

        return farmedItems.stream().map(farmedItem -> {
            // Convert the FarmedItem to DTO using ModelMapper
            FarmedListingDTO dto = modelMapper.map(farmedItem, FarmedListingDTO.class);

            // Convert Hibernate PersistentBag to a List<String> if otherImages is not null
            if (farmedItem.getOtherImages() != null) {
                List<String> imagePaths = farmedItem.getOtherImages()
                        .stream()
                        .collect(Collectors.toList()); // Already Strings, so no need for transformation
                dto.setOtherImages(imagePaths);
            }

            // Convert Hibernate PersistentBag to a List<Double> if bidAmounts is not null
            if (farmedItem.getBidAmounts() != null) {
                List<Double> bidAmountsList = farmedItem.getBidAmounts()
                        .stream()
                        .collect(Collectors.toList()); // Collect the bid amounts into a List<Double>
                dto.setBidAmounts(bidAmountsList);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public FarmedListingDTO getItemById(Long id) {
        Optional<FarmedItem> itemOptional = farmedItemRepository.findById((id));
        System.out.println(itemOptional.get());
        return itemOptional.map(item -> modelMapper.map(item, FarmedListingDTO.class)).orElse(null);
    }

    @Override
    public FarmedItem updateItem(Long id, FarmedListingDTO farmedItemDTO) {
        Optional<FarmedItem> existingItemOpt = farmedItemRepository.findById(id);
        if (existingItemOpt.isPresent()) {
            FarmedItem existingItem = existingItemOpt.get();
            System.out.println(id);


            existingItem.setId(id);

            existingItem.setTitle(farmedItemDTO.getTitle());
            existingItem.setCategory(farmedItemDTO.getCategory());
            existingItem.setMiniDescription(farmedItemDTO.getMiniDescription());
            existingItem.setDetailedDescription(farmedItemDTO.getDetailedDescription());
            existingItem.setSellType(farmedItemDTO.getSellType());
            existingItem.setPrice(farmedItemDTO.getPrice());
            existingItem.setStartingBid(farmedItemDTO.getStartingBid());
            existingItem.setBidDuration(farmedItemDTO.getBidDuration());
            existingItem.setQty(farmedItemDTO.getQty());
            existingItem.setLandDesk(farmedItemDTO.getLandDesk());
            existingItem.setStatus(farmedItemDTO.getStatus());
            existingItem.setDeletes(farmedItemDTO.getDeletes());
            existingItem.setMainImage(farmedItemDTO.getMainImage());
            existingItem.setOtherImages(farmedItemDTO.getOtherImages());
            existingItem.setTermsAccepted(farmedItemDTO.isTermsAccepted());
            existingItem.setBidStartedDate(farmedItemDTO.getBidStartedDate());
            existingItem.setListingDate(farmedItemDTO.getListingDate());

            return farmedItemRepository.save(existingItem);
        } else {
            return null;
        }
    }

    @Override
    public void deleteItem(Long id) {
        if (farmedItemRepository.existsById(id)) {
            farmedItemRepository.deleteById(id);
        } else {
            throw new RuntimeException("Item not found with ID: " + id);
        }
    }

    @Override
    public List<FarmedItem> getActiveListings() {
        return farmedItemRepository.findByStatus("Active");
    }

    @Override
    public List<FarmedListingDTO> getActiveFarmedListings() {
        List<FarmedItem> activeListings = farmedItemRepository.findByStatus("Active");
        return activeListings.stream()
                .map(farmedListing -> modelMapper.map(farmedListing, FarmedListingDTO.class))  // Convert using ModelMapper
                .collect(Collectors.toList());
    }

    @Override
    public FarmedListingDTO getListingById(Long id) throws ChangeSetPersister.NotFoundException {
        return farmedItemRepository.findById(id)
                .map(listing -> {
                    FarmedListingDTO dto = modelMapper.map(listing, FarmedListingDTO.class);
                    // Add any custom mappings here
                    dto.setUserId(Long.valueOf(listing.getUser().getFirstName() + " " + listing.getUser().getLastName()));
                    return dto;
                })
                .orElseThrow(() -> new ChangeSetPersister.NotFoundException());
    }




    // Fetch ended auction items
    public List<FarmedListingDTO> getEndedAuctionItems() {
        List<FarmedItem> endedItems = farmedItemRepository.findByStatus("ENDED");
        return endedItems.stream()
                .map(item -> modelMapper.map(item, FarmedListingDTO.class))
                .collect(Collectors.toList());
    }

    // Fetch pending auction items
    public List<FarmedListingDTO> getPendingAuctionItems() {
        List<FarmedItem> pendingItems = farmedItemRepository.findByStatus("PENDING");
        return pendingItems.stream()
                .map(item -> modelMapper.map(item, FarmedListingDTO.class))
                .collect(Collectors.toList());
    }
}
