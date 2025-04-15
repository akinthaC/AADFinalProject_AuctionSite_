package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.ResponseDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.WatchlistItemDTO;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.AddToCartServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.WatchItemServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/WatchItem")
@CrossOrigin(origins = "*")
public class WatchItemController {

    @Autowired
    private WatchItemServiceImpl watchItemService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<String> addToWatchList(@RequestParam Long listingId, @RequestParam String listingType, @RequestParam String email) {
        UserDTO userDTO = userService.getUserByEmail( email);
        System.out.println(userDTO.getId());

        WatchlistItemDTO watchlistItemDTO = new WatchlistItemDTO(
                userDTO.getId(),listingId,listingType
        );

        String result =watchItemService.addToWatchList(watchlistItemDTO);
        if ("EXISTS".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Item already in watchList");
        }

        return ResponseEntity.ok("Added successfully");
    }


    @GetMapping("/getByid/{email}")
    public ResponseEntity<ResponseDTO> getCartItemsByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        List<WatchlistItemDTO> watchlistItemDTOS = watchItemService.getWatchlistItemsByUserId(userDTO.getId());
        return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched Watched Items", watchlistItemDTOS));

    }

}
