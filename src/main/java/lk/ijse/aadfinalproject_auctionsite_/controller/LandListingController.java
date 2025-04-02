package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.FarmedListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.LandListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.ResponseDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.LandListing;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.AuctionStatusUpdater;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.FarmedItemServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.LandListingServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.util.VarList;
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
@RequestMapping("/api/listings/Land")
@CrossOrigin(origins = "*")
public class LandListingController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuctionStatusUpdater auctionStatusUpdater;

    @Autowired
    private LandListingServiceImpl landListingService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    @PostMapping
    public ResponseEntity<ResponseDTO> saveOrUpdateLands(
            @RequestParam(value = "id", required = false) Long id, // Used for updates
            @RequestParam("landName") String landName,
            @RequestParam("location") String location,
            @RequestParam("currentUse") String currentUse,
            @RequestParam("size") String size,
            @RequestParam("topography") String topography,
            @RequestParam("waterAvailability") String waterAvailability,
            @RequestParam("access") String access,
            @RequestParam("soilQuality") String soilQuality,
            @RequestParam("features") String features,
            @RequestParam("richTextEditor") String detailedDescription,
            @RequestParam("price") Double price,
            @RequestParam("startPrice") Double startPrice,
            @RequestParam("reservePrice") Double reservePrice,
            @RequestParam("auctionDuration") Integer auctionDuration,
            @RequestParam("paymentTerms") String paymentTerms,
            @RequestParam(value = "auctionStartDate", required = false) String bidStartedDate,
            @RequestParam("mainImage") MultipartFile mainImage,
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
                }else {
                    status = "Pending";
                }
            } else {
                status = "Pending";
            }

            System.out.println(email);
            UserDTO userDTO = userService.getUserByEmail( email);
            System.out.println(userDTO.getId());

            LandListingDTO landListingDTO = new LandListingDTO(
                    landName, location, currentUse, size, topography,
                    waterAvailability, access, soilQuality, features, detailedDescription, price, startPrice,
                    reservePrice, auctionDuration, paymentTerms, bidStart, uniqueMainImageName,otherImageNames,LocalDateTime.now(),status,"active",userDTO.getId()
            );


            System.out.println(id);

            if (id != null) {
                // Update existing item
                System.out.println("Updating item with ID: " + id);

                // Make sure the update logic is properly handled in the service method
                LandListing updated = landListingService.updateItem(id, landListingDTO);

                // Check if update was successful
                System.out.println(updated);
                if (updated != null) {
                    return ResponseEntity.ok(new ResponseDTO(201, "Item updated successfully", null));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(400, "Item update failed", null));
                }
            } else {
                System.out.println("ID not provided, saving new item...");
                // Save new item if ID is not provided
                boolean isSaved = landListingService.saveLandListing(landListingDTO);

                if (isSaved){
                    auctionStatusUpdater.sendEmailAfterSave(landListingDTO,email);

                }

                return isSaved
                        ? ResponseEntity.ok(new ResponseDTO(201, "FarmedItem saved successfully!", null))
                        : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(400, "FarmedItem not saved!", null));
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
            List<LandListingDTO> lands = landListingService.getAllLandListing();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched All Vehicles", lands));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Long id) {
        LandListingDTO itemDTO = landListingService.getItemById(id);
        System.out.println("lllll" + itemDTO);
        if (itemDTO != null) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched By Id", itemDTO));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Cant Fetched By Id", null));
        }
    }



    @DeleteMapping("/Delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        landListingService.deleteItem(id);
        return "Item deleted successfully";
    }


}