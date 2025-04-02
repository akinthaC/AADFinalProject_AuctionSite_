package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.*;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.VehicleListing;
import lk.ijse.aadfinalproject_auctionsite_.service.VehicleListingService;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.AuctionStatusUpdater;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/listings/Vehicle")
public class VehicleListingController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuctionStatusUpdater auctionStatusUpdater;

    @Autowired
    private VehicleListingService vehicleListingService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";


    @PostMapping()
    public ResponseEntity<?> createCarListing(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam("make") String make,
            @RequestParam("year") String year,
            @RequestParam("miniDesc") String miniDesc,
            @RequestParam("mileage") double mileage,
            @RequestParam("fuel") String fuel,
            @RequestParam("transmission") String transmission,
            @RequestParam("sellType") String sellType,
            @RequestParam("price") double price,
            @RequestParam("startingBid") double startingBid,
            @RequestParam("bidDuration") int bidDuration,
            @RequestParam(value = "bidStartDate", required = false) String bidStartedDate,
            @RequestParam("termsAccepted") boolean termsAccepted,
            @RequestParam("richTextEditor") String detailedDescription,
            @RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestParam(value = "otherImages", required = false) MultipartFile[] otherImages,
            @RequestParam("email") String email
    ) throws IOException {
        try {
            String uniqueMainImageName = mainImage.isEmpty() ? null : System.currentTimeMillis() + "_" + mainImage.getOriginalFilename();
            if (uniqueMainImageName != null) {
                Path mainImagePath = Paths.get(uploadDirectory, uniqueMainImageName);
                Files.createDirectories(mainImagePath.getParent());
                Files.write(mainImagePath, mainImage.getBytes());
            }

            List<String> otherImageNames = new ArrayList<>();
            if (otherImages != null) {
                for (MultipartFile otherImage : otherImages) {
                    String uniqueOtherImageName = System.currentTimeMillis() + "_" + otherImage.getOriginalFilename();
                    Path otherImagePath = Paths.get(uploadDirectory, uniqueOtherImageName);
                    Files.createDirectories(otherImagePath.getParent());
                    Files.write(otherImagePath, otherImage.getBytes());
                    otherImageNames.add(uniqueOtherImageName);
                }
            }

            String status = "Pending";
            LocalDate bidStart = null;
            if (bidStartedDate != null && !bidStartedDate.isEmpty()) {
                bidStart = LocalDate.parse(bidStartedDate);
                System.out.println(bidStart);
                System.out.println(LocalDate.now());
                if (bidStart.isBefore(LocalDate.now()) || bidStart.isEqual(LocalDate.now())) {
                    status = "Active";
                } else {
                    status = "Pending";
                }
            } else {
                status = "Active";
            }


            System.out.println(email);
            UserDTO userDTO = userService.getUserByEmail(email);
            System.out.println(userDTO.getId());


            VehicleListingDTO vehicleListingDTO = new VehicleListingDTO(
                    vehicleType, make, year, miniDesc, mileage, fuel, transmission, sellType, price, startingBid, bidDuration, detailedDescription, uniqueMainImageName, otherImageNames, termsAccepted, LocalDateTime.now(), status, "active", userDTO.getId(),bidStart
            );
            System.out.println(id);

            if (id != null) {
                // Update existing item
                System.out.println("Updating item with ID: " + id);

                // Make sure the update logic is properly handled in the service method
                VehicleListing updated = vehicleListingService.updateItem(id, vehicleListingDTO);

                // Check if update was successful
                System.out.println(updated);
                if (updated != null) {
                    return ResponseEntity.ok(new ResponseDTO(201, "Vehicle updated successfully", null));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(400, "Item update failed", null));
                }
            } else {
                System.out.println("ID not provided, saving new item...");
                // Save new item if ID is not provided
                boolean isSaved = vehicleListingService.saveVehicle(vehicleListingDTO);

                if (isSaved){
                    auctionStatusUpdater.sendEmailAfterSave(vehicleListingDTO,email);

                }

                return isSaved
                        ? ResponseEntity.ok(new ResponseDTO(201, "Vehicle saved successfully!", null))
                        : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(400, "Vehicle not saved!", null));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(500, "File upload failed: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(500, "Failed to process request: " + e.getMessage(), null));
        }
    }


        @GetMapping("/getAll")
        public ResponseEntity<ResponseDTO> getAllVehicles() {
            try {
                List<VehicleListingDTO> lands = vehicleListingService.getAllVehicleListing();
                return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched All Vehicles", lands));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
            }
        }


    @GetMapping("/getById/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Long id) {
        VehicleListingDTO itemDTO = vehicleListingService.getItemById(id);
        System.out.println("lllll"+itemDTO);
        if (itemDTO != null) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched By Id", itemDTO));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"Cant Fetched By Id", null));
        }
    }


    @DeleteMapping("/Delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        vehicleListingService.deleteItem(id);
        return "Item deleted successfully";
    }

}
