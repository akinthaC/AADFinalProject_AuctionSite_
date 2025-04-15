package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.AddToCartWatchListCountDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.ResponseDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.AddToCart;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.AddToCartServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import lk.ijse.aadfinalproject_auctionsite_.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/AddToCart")
@CrossOrigin(origins = "*")
public class AddToCartController {

    @Autowired
    private AddToCartServiceImpl addToCartService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public AddToCartWatchListCountDTO getListingCounts(@RequestParam Long listingId) {
        return addToCartService.getCountsByListingId(listingId);
    }

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestParam Long listingId, @RequestParam String listingType, @RequestParam String email) {
        UserDTO userDTO = userService.getUserByEmail( email);
        System.out.println(userDTO.getId());

        AddToCartDTO addToCartDTO = new AddToCartDTO(
                userDTO.getId(),listingId,listingType,1
        );

        String result =addToCartService.addToCart(addToCartDTO);
        if ("EXISTS".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Item already in cart");
        }

        return ResponseEntity.ok("Added successfully");
    }

    @GetMapping("/getByid/{email}")
    public ResponseEntity<ResponseDTO> getCartItemsByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        List<AddToCartDTO> cartItems = addToCartService.getCartItemsByUserId(userDTO.getId());
        return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched Cart Items", cartItems));

    }


}
