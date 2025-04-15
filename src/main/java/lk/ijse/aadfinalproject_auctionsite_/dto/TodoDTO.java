package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    private Long id;
    private String title;
    private String description;
    private String assignAdminEmail;
    private String completed; // Can be changed to Boolean if needed
    private LocalDate date;

}
