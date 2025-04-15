package lk.ijse.aadfinalproject_auctionsite_.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private String sandbox;
    private String merchant_id;
    private String return_url;
    private String cancel_url;
    private String notify_url;
    private String order_id;
    private String items;
    private Double amount;
    private String currency;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private Long custom_1;
    private String custom_2;
    private Integer custom_3;
    private String custom_4;
    private Double custom_5;
}
