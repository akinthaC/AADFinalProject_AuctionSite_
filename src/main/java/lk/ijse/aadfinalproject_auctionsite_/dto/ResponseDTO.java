package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class   ResponseDTO {
    private int code;                // Status code (e.g., 200 for success)
    private String message;          // Message providing information about the result
    private Object data;             // Data related to the response (vehicle info, etc.)

}
