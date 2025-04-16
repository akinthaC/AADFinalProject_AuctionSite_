package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.FarmedListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.LandListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.VehicleListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.LandListing;
import lk.ijse.aadfinalproject_auctionsite_.entity.VehicleListing;
import lk.ijse.aadfinalproject_auctionsite_.repo.VehicleListingRepo;

import lk.ijse.aadfinalproject_auctionsite_.service.VehicleListingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleListingServiceImpl implements VehicleListingService {

    @Autowired
    private VehicleListingRepo vehicleListingRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Convert CarListing entity to CarListingDTO using ModelMapper



    @Override
    public VehicleListing updateItem(Long id, VehicleListingDTO vehicleListingDTO) {
        Optional<VehicleListing> existingItemOpt = vehicleListingRepository.findById(id);
        if (existingItemOpt.isPresent()) {
            VehicleListing existingItem = existingItemOpt.get();
            System.out.println(id);


            existingItem.setId(id);

            existingItem.setVehicleType(vehicleListingDTO.getVehicleType());
            existingItem.setMake(vehicleListingDTO.getMake());
            existingItem.setYearOfManufacture(vehicleListingDTO.getYearOfManufacture());
            existingItem.setMiniDescription(vehicleListingDTO.getMiniDescription());
            existingItem.setMileage(vehicleListingDTO.getMileage());
            existingItem.setFuelType(vehicleListingDTO.getFuelType());
            existingItem.setTransmission(vehicleListingDTO.getTransmission());
            existingItem.setSellingOption(vehicleListingDTO.getSellingOption());
            existingItem.setPrice(vehicleListingDTO.getPrice());
            existingItem.setStartingBidPrice(vehicleListingDTO.getStartingBidPrice());
            existingItem.setBidDuration(vehicleListingDTO.getBidDuration());
            existingItem.setDetailedDescription(vehicleListingDTO.getDetailedDescription());
            existingItem.setMainImage(vehicleListingDTO.getMainImage());
            existingItem.setOtherImages(vehicleListingDTO.getOtherImages());
            existingItem.setListingDate(vehicleListingDTO.getListingDate());
            existingItem.setStatus(vehicleListingDTO.getStatus());
            existingItem.setDeletes(vehicleListingDTO.getDeletes());
            existingItem.setBidStartedDate(vehicleListingDTO.getBidStartedDate());


            return vehicleListingRepository .save(existingItem);
        } else {
            return null;
        }
    }

    @Override
    public boolean saveVehicle(VehicleListingDTO vehicleListingDTO) {
        try {
            // Convert DTO to entity
            VehicleListing vehicleListing = modelMapper.map(vehicleListingDTO, VehicleListing.class);
            vehicleListingRepository.save(vehicleListing);  // Save to DB
            return true;
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<VehicleListingDTO> getAllVehicleListing() {
        List<VehicleListing> vehicleListings = vehicleListingRepository.findAll();

        return vehicleListings.stream().map(vehicleListing -> {
            // Convert the FarmedItem to DTO using ModelMapper
            VehicleListingDTO dto = modelMapper.map(vehicleListing, VehicleListingDTO.class);

            // Convert Hibernate PersistentBag to a List<String> if otherImages is not null
            if (vehicleListing.getOtherImages() != null) {
                List<String> imagePaths = vehicleListing.getOtherImages()
                        .stream()
                        .collect(Collectors.toList()); // Already Strings, so no need for transformation
                dto.setOtherImages(imagePaths);
            }

            // Convert Hibernate PersistentBag to a List<Double> if bidAmounts is not null
            if (vehicleListing.getBidAmounts() != null) {
                List<Double> bidAmountsList = vehicleListing.getBidAmounts()
                        .stream()
                        .collect(Collectors.toList()); // Collect the bid amounts into a List<Double>
                dto.setBidAmounts(bidAmountsList);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public VehicleListingDTO getItemById(Long id) {
        Optional<VehicleListing> itemOptional = vehicleListingRepository.findById((id));
        System.out.println(itemOptional.get());
        return itemOptional.map(item -> modelMapper.map(item, VehicleListingDTO.class)).orElse(null);
    }

    @Override
    public void deleteItem(Long id) {
        if (vehicleListingRepository.existsById(id)) {
            vehicleListingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Item not found with ID: " + id);
        }
    }

    @Override
    public List<VehicleListingDTO> getActiveLandListings() {
        List<VehicleListing> activeListings = vehicleListingRepository.findByStatus("Active");
        return activeListings.stream()
                .map(landListing -> modelMapper.map(landListing, VehicleListingDTO.class))  // Convert using ModelMapper
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleListingDTO> getPendingAuctionItems() {
        List<VehicleListing> pendingItems = vehicleListingRepository.findByStatus("PENDING");
        return pendingItems.stream()
                .map(item -> modelMapper.map(item, VehicleListingDTO.class))
                .collect(Collectors.toList());

    }

}
