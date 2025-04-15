package lk.ijse.aadfinalproject_auctionsite_.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @GetMapping("/test1")
    @PreAuthorize("hasAuthority('mainadmin')")
    public String checkMainAdmin() {
        return "passed~! mainadmin";
    }

    @GetMapping("/test2")
    @PreAuthorize("hasAuthority('admin')")
    public String checkAdmin() {
        return "passed~! admin";
    }

    @GetMapping("/test3")
    @PreAuthorize("hasAuthority('seller')")
    public String checkSeller() {
        return "passed~! Seller";
    }

    @GetMapping("/test4")
    @PreAuthorize("hasAuthority('buyer')")
    public String checkBuyer() {
        return "passed~! Buyer";
    }


}
