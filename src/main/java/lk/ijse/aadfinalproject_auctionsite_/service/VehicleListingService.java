package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.VehicleListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.VehicleListing;

import java.util.List;

public interface VehicleListingService {


    VehicleListing updateItem(Long id, VehicleListingDTO vehicleListingDTO);

    boolean saveVehicle(VehicleListingDTO vehicleListingDTO);

    List<VehicleListingDTO> getAllVehicleListing();

    VehicleListingDTO getItemById(Long id);

    void deleteItem(Long id);
}
