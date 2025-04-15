package lk.ijse.aadfinalproject_auctionsite_.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
    private String description;
    private String addressLine1;
    private String addressLine2;
    private Double latitude;
    private Double longitude;
    private String status;
}
