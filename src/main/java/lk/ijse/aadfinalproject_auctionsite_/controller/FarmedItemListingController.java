package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.FarmedListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.ResponseDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import lk.ijse.aadfinalproject_auctionsite_.service.UserService;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.AuctionStatusUpdater;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.FarmedItemServiceImpl;

import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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
@RequestMapping("/api/listings")
@CrossOrigin(origins = "*")

public class FarmedItemListingController {

    @Autowired
    private AuctionStatusUpdater auctionStatusUpdater;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FarmedItemServiceImpl listingService;

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";


    @PostMapping
    public ResponseEntity<ResponseDTO> saveOrUpdateFarmedItem(
            @RequestParam(value = "id", required = false) Long id, // Used for updates
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("miniDesc") String miniDescription,
            @RequestParam("richTextEditor") String detailedDescription,
            @RequestParam("sellType") String sellType,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "startingBid", required = false) Double startingBid,
            @RequestParam(value = "bidDuration", required = false) Integer bidDuration,
            @RequestParam("mainImage") MultipartFile mainImage,
            @RequestParam(value = "otherImages", required = false) MultipartFile[] otherImages,
            @RequestParam("termsAccepted") boolean termsAccepted,
            @RequestParam(value = "bidStartDate", required = false) String bidStartedDate,
            @RequestParam(value = "qty", required = false) Integer qty,
            @RequestParam(value = "landDescription", required = false) String landDescription,
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
                status = "Active";
            }


            System.out.println(email);
            UserDTO userDTO = userService.getUserByEmail( email);
            System.out.println(userDTO.getId());


            FarmedListingDTO farmedItemDTO = new FarmedListingDTO(
                    title, category, miniDescription, detailedDescription, sellType,
                    price, startingBid, bidDuration, qty, landDescription, status, "active",
                    uniqueMainImageName, otherImageNames, termsAccepted, bidStart, LocalDateTime.now(),userDTO.getId()
            );
            System.out.println(id);

            if (id != null) {
                // Update existing item
                System.out.println("Updating item with ID: " + id);

                // Make sure the update logic is properly handled in the service method
                FarmedItem updated = listingService.updateItem(id, farmedItemDTO);

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
                boolean isSaved = listingService.saveFarmedItem(farmedItemDTO);

                if (isSaved){
                    auctionStatusUpdater.sendEmailAfterSave(farmedItemDTO,email);

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

    @DeleteMapping("/Delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        listingService.deleteItem(id);
        return "Item deleted successfully";
    }

    @GetMapping("/active")
    public ResponseEntity<ResponseDTO> getActiveFarmedListings() {
        try {
            // Fetch active listings
            List<FarmedListingDTO> activeFarmedListings = listingService.getActiveFarmedListings();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched Active Listings", activeFarmedListings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<ResponseDTO> getPendingFarmedListings() {
        try {
            // Fetch active listings
            List<FarmedListingDTO> pendingFarmedListings = listingService.getPendingAuctionItems();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched Pending Listings", pendingFarmedListings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getListingById(@PathVariable Long id) {
        try {
            FarmedListingDTO listing = listingService.getListingById(id);
            return ResponseEntity.ok(
                    new ResponseDTO(
                            HttpStatus.OK.value(),
                            "Listing retrieved successfully",
                            listing
                    )
            );
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseDTO(
                            HttpStatus.NOT_FOUND.value(),
                            "Listing not found",
                            null
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseDTO(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error fetching listing: " + e.getMessage(),
                            null
                    )
            );
        }
    }

    /*@GetMapping("/active-auction-items")
    public List<FarmedListingDTO> getActiveAuctionItems() {
        return listingService.getActiveAuctionItems();
    }

    // Get ended auction items
    @GetMapping("/ended-auction-items")
    public List<FarmedListingDTO> getEndedAuctionItems() {
        return listingService.getEndedAuctionItems();
    }

    // Get pending auction items
    @GetMapping("/pending-auction-items")
    public List<FarmedListingDTO> getPendingAuctionItems() {
        return listingService.getPendingAuctionItems();
    }*/

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllVehicles() {
        try {
            List<FarmedListingDTO> farmedListing = listingService.getAllFarmedListing();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched All Vehicles", farmedListing));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Long id) {
        FarmedListingDTO itemDTO = listingService.getItemById(id);
        System.out.println("lllll"+itemDTO);
        if (itemDTO != null) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched By Id", itemDTO));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"Cant Fetched By Id", null));
        }
    }

   /* @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateListing(@PathVariable("id") Long id, @RequestBody FarmedItem updatedItem) {
        try {
            // Call the service to update the item in the database
            FarmedItem updated = listingService.updateItem(id, updatedItem);
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Item updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Failed to update item",null));
        }
    }*/

    // Get all listings
   /* @GetMapping
    public ResponseEntity<List<FarmedItem>> getAllListings() {
        List<FarmedItem> listings = listingService.getAllListings();
        return ResponseEntity.ok(listings);
    }

    // Get a listing by ID
    @GetMapping("/{id}")
    public ResponseEntity<FarmedItem> getListingById(@PathVariable Long id) {
        Optional<FarmedItem> listing = listingService.getListingById(id);
        return listing.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a listing by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }*/

}
