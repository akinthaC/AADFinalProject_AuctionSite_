package lk.ijse.aadfinalproject_auctionsite_.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class HomePageCountDTO {
    private long totalUsers;
    private long totalSellers;
    private long totalAdmins;
    private long totalOrders;
    private long totalListings;
    private long totalTasks;
    private long totalHoldPayments;
    private long totalPendingPayments;
    private long totalDonePayments;




}
