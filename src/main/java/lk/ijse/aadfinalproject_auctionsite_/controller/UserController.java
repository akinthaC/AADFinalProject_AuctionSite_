package lk.ijse.aadfinalproject_auctionsite_.controller;

import jakarta.validation.Valid;
import lk.ijse.aadfinalproject_auctionsite_.dto.AuthDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.ResponseDTO;
import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.service.UserService;
import lk.ijse.aadfinalproject_auctionsite_.util.JwtUtil;
import lk.ijse.aadfinalproject_auctionsite_.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //constructor injection
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            int res = userService.saveUser(userDTO);
            switch (res) {
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }


    @GetMapping(value = "/getUserByEmail")
    public ResponseEntity<ResponseDTO> getUserDetail(@RequestParam String email) {
        UserDTO user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Success", user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "User not found", null));
        }
    }

    @GetMapping("/getUserById")
    public ResponseEntity<ResponseDTO> getUserById(@RequestParam Long userId) {
        // Log the received userId
        System.out.println("Received userId: " + userId);

        // Your existing code logic
        UserDTO user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Success", user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "User not found", null));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users-sellers-buyers")
    public List<UserDTO> getUsersByRole() {
        // Fetch users with 'seller' role
        List<UserDTO> userDTOS = userService.getUsersByRoles("seller");

        // Fetch users with 'buyer' role
        List<UserDTO> userDTOS2 = userService.getUsersByRoles("buyer");

        // Combine both lists
        userDTOS.addAll(userDTOS2); // Add users from 'buyer' role to 'seller' users list

        // Return the combined list
        return userDTOS;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users-admin")
    public List<UserDTO> getAdmins() {

        List<UserDTO> userDTOS2 = userService.getUsersByRoles("admin");
        return userDTOS2;

    }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("userId") Long userId, @RequestBody Map<String, String> statusMap) {
        try {
            String newStatus = statusMap.get("status");
            userService.updateUserStatus(userId, newStatus);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/update-profile/{email}")
    public ResponseEntity<String> updateAdminProfile(@PathVariable String email, @RequestBody UserDTO updatedUser) {
        try {
            boolean updated = userService.updateUserProfile(email, updatedUser);
            if (updated) {
                return ResponseEntity.ok("Profile updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
        }
    }




}
