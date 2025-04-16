package lk.ijse.aadfinalproject_auctionsite_.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class InquiryDTO {
    private Long id;
    private String orderId;
    private String email;
    private String message;
    private LocalDate inquiryDate;
    private String responed;
}
