package lk.ijse.aadfinalproject_auctionsite_.controller;

import lk.ijse.aadfinalproject_auctionsite_.entity.PaymentRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/payhere")
public class PayHereController {

    @PostMapping("/checkout")
    public ResponseEntity<Object> forwardPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String payHereUrl = "https://sandbox.payhere.lk/pay/checkoutJ";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PaymentRequest> entity = new HttpEntity<>(paymentRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(payHereUrl, HttpMethod.POST, entity, String.class);

            // Ensure the response is not empty
            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new RuntimeException("Empty response from PayHere API");
            }

            // Return the response from PayHere
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();

            // Return a detailed error message as JSON
            ErrorResponse errorResponse = new ErrorResponse("Failed to process payment", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}

// ErrorResponse class to encapsulate the error details
class ErrorResponse {
    private String message;
    private String details;

    public ErrorResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }

    // Getters and setters for message and details
}
