package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.aadfinalproject_auctionsite_.dto.FarmedListingDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.LandListing;
import lk.ijse.aadfinalproject_auctionsite_.entity.VehicleListing;
import lk.ijse.aadfinalproject_auctionsite_.repo.FarmedItemListing;
import lk.ijse.aadfinalproject_auctionsite_.repo.LandListingRepo;
import lk.ijse.aadfinalproject_auctionsite_.repo.VehicleListingRepo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;

@Service
public class AuctionStatusUpdater {

    private final FarmedItemListing farmedListingRepository;
    private final LandListingRepo landListingRepo;
    private final VehicleListingRepo vehicleListingRepo;
    private final EmailService emailService;
    private final JavaMailSender mailSender;

    public AuctionStatusUpdater(FarmedItemListing farmedListingRepository,VehicleListingRepo vehicleListingRepo,LandListingRepo landListingRepo , EmailService emailService, JavaMailSender mailSender) {
        this.farmedListingRepository = farmedListingRepository;
        this.vehicleListingRepo = vehicleListingRepo;
        this.landListingRepo = landListingRepo;
        this.emailService = emailService;
        this.mailSender = mailSender;
    }

  // Runs every day at midnight
    public void updateAuctionStatus() {
        LocalDate today = LocalDate.now();
        System.out.println(today);

        // Activate pending auctions: Check for items with "Pending" status, "bidding" sell type, and bidStartedDate equals today
        List<FarmedItem> toActivate = farmedListingRepository.findByStatusAndSellTypeAndBidStartedDate("Pending", "bidding", today);
        for (FarmedItem listing : toActivate) {
            listing.setStatus("Active");

            try {
                sendEmailWithImage(
                        listing.getUser().getEmail(),
                        "Your Auction is Now Active!",
                        "<html>" +
                                "<head>" +
                                "<style>" +
                                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                ".cta-button { background-color: #2c3e50; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                ".cta-button:hover { background-color: #34495e; }" +
                                ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "<div class='container'>" +
                                "<div class='header'>Your Auction is Now Active!</div>" +
                                "<div class='content'>" +
                                "<p>Hello,</p>" +
                                "<p>We're excited to let you know that your auction for <strong>" + listing.getTitle() + "</strong> is now live! You can start bidding now and watch the action unfold.</p>" +
                                "<p>Click below to view your auction and participate in the bidding:</p>" +
                                "<a href='your_auction_link' class='cta-button'>View Auction</a>" +
                                "</div>" +
                                "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                "<p>Best regards,</p>" +
                                "<p>The Auction Team</p>" +
                                "</div>" +
                                "</body>" +
                                "</html>",
                        listing.getMainImage()
                );
            } catch (MessagingException e) {
                System.err.println("Error sending activation email: " + e.getMessage());
            }

        }
        farmedListingRepository.saveAll(toActivate);
        System.out.println("Activated pending auctions: " + toActivate.size());

        // End active auctions: Check for items with "Active" status and "bidding" sell type, and if the end date (calculated) is today
        List<FarmedItem> toEnd = farmedListingRepository.findByStatusAndSellType("Active", "bidding");
        for (FarmedItem listing : toEnd) {
            // Calculate the end date based on bidStartedDate and duration
            LocalDate bidStartDate = listing.getBidStartedDate();
            long durationInDays = listing.getBidDuration(); // Assuming 'duration' is in days
            LocalDate endDate = bidStartDate.plusDays(durationInDays);

            // If end date is today, update the status to "End"
            if (endDate.equals(today)) {
                listing.setStatus("End");
                listing.setWinnerAssignedDate(today);

                try {
                    // Auction Ended Email
                    sendEmailWithImage(
                            listing.getUser().getEmail(),
                            "Your Auction Has Ended",
                            "<html>" +
                                    "<head>" +
                                    "<style>" +
                                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                     ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                    ".cta-button { background-color: #e74c3c; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                    ".cta-button:hover { background-color: #c0392b; }" +
                                    ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                    "</style>" +
                                    "</head>" +
                                    "<body>" +
                                    "<div class='container'>" +
                                    "<div class='header'>Your Auction Has Ended</div>" +
                                    "<div class='content'>" +
                                    "<p>Hello,</p>" +
                                    "<p>Your auction <strong>" + listing.getTitle() + "</strong> has successfully ended.</p>" +
                                    "<p>We appreciate your participation and hope you found the experience valuable. Please check your listing summary below:</p>" +
                                    "<a href='your_auction_summary_link' class='cta-button'>View Auction Summary</a>" +
                                    "</div>" +
                                    "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                    "<p>Best regards,</p>" +
                                    "<p>The Auction Team</p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>",
                            listing.getMainImage()
                    );

                    // Auction Summary Email
                    sendEmailWithImage(
                            listing.getUser().getEmail(),
                            "Auction Summary for " + listing.getTitle(),
                            "<html>" +
                                    "<head>" +
                                    "<style>" +
                                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                    ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                    ".cta-button { background-color: #3498db; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                    ".cta-button:hover { background-color: #2980b9; }" +
                                    ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                    "</style>" +
                                    "</head>" +
                                    "<body>" +
                                    "<div class='container'>" +
                                    "<div class='header'>Auction Summary for " + listing.getTitle() + "</div>" +
                                    "<div class='content'>" +
                                    "<p>Here is the summary of your auction <strong>" + listing.getTitle() + "</strong>.</p>" +
                                    "<p>Review the details below and get more insights about the outcome:</p>" +
                                    "<a href='your_auction_details_link' class='cta-button'>View Auction Details</a>" +
                                    "</div>" +
                                    "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                    "<p>Thank you for using our auction platform!</p>" +
                                    "<p>The Auction Team</p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>",
                            listing.getMainImage()
                    );
                } catch (MessagingException e) {
                    System.err.println("Error sending summary email: " + e.getMessage());
                }

            }
        }
        farmedListingRepository.saveAll(toEnd);
        System.out.println("Ended active auctions: " + toEnd.size());



        List<FarmedItem> toEnd1 = farmedListingRepository.findByStatusAndSellType("Active", "both");

        for (FarmedItem listing : toEnd1) {
            System.out.println(listing.getStatus());
            // Calculate the end date based on bidStartedDate and duration
            LocalDate bidStartDate = listing.getBidStartedDate();
            long durationInDays = listing.getBidDuration(); // Assuming 'duration' is in days
            LocalDate endDate = bidStartDate.plusDays(durationInDays);

            // If end date is today, update the status to "End"
            if (endDate.equals(today)) {
                listing.setStatus("End");
                listing.setWinnerAssignedDate(today);

                try {
                    // Auction Ended Email
                    sendEmailWithImage(
                            "akinthachandinu5@gmail.com",
                            "Your Auction Has Ended",
                            "<html>" +
                                    "<head>" +
                                    "<style>" +
                                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                    ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                    ".cta-button { background-color: #e74c3c; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                    ".cta-button:hover { background-color: #c0392b; }" +
                                    ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                    "</style>" +
                                    "</head>" +
                                    "<body>" +
                                    "<div class='container'>" +
                                    "<div class='header'>Your Auction Has Ended</div>" +
                                    "<div class='content'>" +
                                    "<p>Hello,</p>" +
                                    "<p>Your auction <strong>" + listing.getTitle() + "</strong> has successfully ended.</p>" +
                                    "<p>We appreciate your participation and hope you found the experience valuable. Please check your listing summary below:</p>" +
                                    "<a href='your_auction_summary_link' class='cta-button'>View Auction Summary</a>" +
                                    "</div>" +
                                    "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                    "<p>Best regards,</p>" +
                                    "<p>The Auction Team</p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>",
                            listing.getMainImage()
                    );

                    // Auction Summary Email
                    sendEmailWithImage(
                            "akinthachandinu5@gmail.com",
                            "Auction Summary for " + listing.getTitle(),
                            "<html>" +
                                    "<head>" +
                                    "<style>" +
                                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                    ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                    ".cta-button { background-color: #3498db; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                    ".cta-button:hover { background-color: #2980b9; }" +
                                    ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                    "</style>" +
                                    "</head>" +
                                    "<body>" +
                                    "<div class='container'>" +
                                    "<div class='header'>Auction Summary for " + listing.getTitle() + "</div>" +
                                    "<div class='content'>" +
                                    "<p>Here is the summary of your auction <strong>" + listing.getTitle() + "</strong>.</p>" +
                                    "<p>Review the details below and get more insights about the outcome:</p>" +
                                    "<a href='your_auction_details_link' class='cta-button'>View Auction Details</a>" +
                                    "</div>" +
                                    "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                    "<p>Thank you for using our auction platform!</p>" +
                                    "<p>The Auction Team</p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>",
                            listing.getMainImage()
                    );
                } catch (MessagingException e) {
                    System.err.println("Error sending summary email: " + e.getMessage());
                }

            }
            farmedListingRepository.saveAll(toEnd1);
            System.out.println("Ended both active auctions: " + toEnd1.size());
        }


    }

    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    public void updateAuctionStatus1() {
        LocalDate today = LocalDate.now();
        System.out.println(today);

        List<LandListing> pendingItems = landListingRepo.findByStatusAndAuctionStartDate("Pending", today);
        for (LandListing listing : pendingItems) {
            listing.setStatus("Active");

            try {
                sendEmailWithImage(
                        listing.getUser().getEmail(),
                        "Your Auction is Now Active!",
                        "<html>" +
                                "<head>" +
                                "<style>" +
                                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                ".cta-button { background-color: #2c3e50; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                ".cta-button:hover { background-color: #34495e; }" +
                                ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "<div class='container'>" +
                                "<div class='header'>Your Auction is Now Active!</div>" +
                                "<div class='content'>" +
                                "<p>Hello,</p>" +
                                "<p>We're excited to let you know that your auction for <strong>" + listing.getLandName() + "</strong> is now live! You can start bidding now and watch the action unfold.</p>" +
                                "<p>Click below to view your auction and participate in the bidding:</p>" +
                                "<a href='your_auction_link' class='cta-button'>View Auction</a>" +
                                "</div>" +
                                "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                "<p>Best regards,</p>" +
                                "<p>The Auction Team</p>" +
                                "</div>" +
                                "</body>" +
                                "</html>",
                        listing.getMainImage()
                );
            } catch (MessagingException e) {
                System.err.println("Error sending activation email: " + e.getMessage());
            }

        }
        landListingRepo.saveAll(pendingItems);
        System.out.println("Activated pending auctions: " + pendingItems.size());

        // End active auctions: Check for items with "Active" status and "bidding" sell type, and if the end date (calculated) is today
        List<LandListing> toEnd = landListingRepo.findByStatus("Active");
        for (LandListing listing : toEnd) {
            // Calculate the end date based on bidStartedDate and duration
            LocalDate bidStartDate = listing.getAuctionStartDate();
            long durationInDays = listing.getAuctionDuration(); // Assuming 'duration' is in days
            LocalDate endDate = bidStartDate.plusDays(durationInDays);

            // If end date is today, update the status to "End"
            if (endDate.equals(today)) {
                listing.setStatus("End");

                try {
                    // Auction Ended Email
                    sendEmailWithImage(
                            listing.getUser().getEmail(),
                            "Your Auction Has Ended",
                            "<html>" +
                                    "<head>" +
                                    "<style>" +
                                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                    ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                    ".cta-button { background-color: #e74c3c; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                    ".cta-button:hover { background-color: #c0392b; }" +
                                    ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                    "</style>" +
                                    "</head>" +
                                    "<body>" +
                                    "<div class='container'>" +
                                    "<div class='header'>Your Auction Has Ended</div>" +
                                    "<div class='content'>" +
                                    "<p>Hello,</p>" +
                                    "<p>Your auction <strong>" + listing.getLandName() + "</strong> has successfully ended.</p>" +
                                    "<p>We appreciate your participation and hope you found the experience valuable. Please check your listing summary below:</p>" +
                                    "<a href='your_auction_summary_link' class='cta-button'>View Auction Summary</a>" +
                                    "</div>" +
                                    "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                    "<p>Best regards,</p>" +
                                    "<p>The Auction Team</p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>",
                            listing.getMainImage()
                    );

                    // Auction Summary Email
                    sendEmailWithImage(
                            listing.getUser().getEmail(),
                            "Auction Summary for " + listing.getLandName(),
                            "<html>" +
                                    "<head>" +
                                    "<style>" +
                                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                    ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                    ".cta-button { background-color: #3498db; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                    ".cta-button:hover { background-color: #2980b9; }" +
                                    ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                    "</style>" +
                                    "</head>" +
                                    "<body>" +
                                    "<div class='container'>" +
                                    "<div class='header'>Auction Summary for " + listing.getLandName() + "</div>" +
                                    "<div class='content'>" +
                                    "<p>Here is the summary of your auction <strong>" + listing.getLandName() + "</strong>.</p>" +
                                    "<p>Review the details below and get more insights about the outcome:</p>" +
                                    "<a href='your_auction_details_link' class='cta-button'>View Auction Details</a>" +
                                    "</div>" +
                                    "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                    "<p>Thank you for using our auction platform!</p>" +
                                    "<p>The Auction Team</p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>",
                            listing.getMainImage()
                    );
                } catch (MessagingException e) {
                    System.err.println("Error sending summary email: " + e.getMessage());
                }

            }
        }
        landListingRepo.saveAll(toEnd);
        System.out.println("Ended active auctions: " + toEnd.size());
    }

    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    public void updateAuctionStatus2() {
        LocalDate today = LocalDate.now();
        System.out.println(today);

        // Activate pending auctions: Check for items with "Pending" status, "bidding" sell type, and bidStartedDate equals today
        List<VehicleListing> toActivate = vehicleListingRepo.findByStatusAndSellingOptionAndBidStartedDate("Pending", "bidding", today);
        for (VehicleListing listing : toActivate) {
            listing.setStatus("Active");

            try {
                sendEmailWithImage(
                        listing.getUser().getEmail(),
                        "Your Auction is Now Active!",
                        "<html>" +
                                "<head>" +
                                "<style>" +
                                "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                ".cta-button { background-color: #2c3e50; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                ".cta-button:hover { background-color: #34495e; }" +
                                ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                "</style>" +
                                "</head>" +
                                "<body>" +
                                "<div class='container'>" +
                                "<div class='header'>Your Auction is Now Active!</div>" +
                                "<div class='content'>" +
                                "<p>Hello,</p>" +
                                "<p>We're excited to let you know that your auction for <strong>" + listing.getMake() + "</strong> is now live! You can start bidding now and watch the action unfold.</p>" +
                                "<p>Click below to view your auction and participate in the bidding:</p>" +
                                "<a href='your_auction_link' class='cta-button'>View Auction</a>" +
                                "</div>" +
                                "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                "<p>Best regards,</p>" +
                                "<p>The Auction Team</p>" +
                                "</div>" +
                                "</body>" +
                                "</html>",
                        listing.getMainImage()
                );
            } catch (MessagingException e) {
                System.err.println("Error sending activation email: " + e.getMessage());
            }

        }
        vehicleListingRepo.saveAll(toActivate);
        System.out.println("Activated pending auctions: " + toActivate.size());

        // End active auctions: Check for items with "Active" status and "bidding" sell type, and if the end date (calculated) is today
        List<VehicleListing> toEnd = vehicleListingRepo.findByStatusAndSellingOption("Active", "bidding");
        for (VehicleListing listing : toEnd) {
            // Calculate the end date based on bidStartedDate and duration
            LocalDate bidStartDate = listing.getBidStartedDate();
            long durationInDays = listing.getBidDuration(); // Assuming 'duration' is in days
            LocalDate endDate = bidStartDate.plusDays(durationInDays);

            // If end date is today, update the status to "End"
            if (endDate.equals(today)) {
                listing.setStatus("End");

                try {
                    // Auction Ended Email
                    sendEmailWithImage(
                            listing.getUser().getEmail(),
                            "Your Auction Has Ended",
                            "<html>" +
                                    "<head>" +
                                    "<style>" +
                                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                    ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                    ".cta-button { background-color: #e74c3c; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                    ".cta-button:hover { background-color: #c0392b; }" +
                                    ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                    "</style>" +
                                    "</head>" +
                                    "<body>" +
                                    "<div class='container'>" +
                                    "<div class='header'>Your Auction Has Ended</div>" +
                                    "<div class='content'>" +
                                    "<p>Hello,</p>" +
                                    "<p>Your auction <strong>" + listing.getMake() + "</strong> has successfully ended.</p>" +
                                    "<p>We appreciate your participation and hope you found the experience valuable. Please check your listing summary below:</p>" +
                                    "<a href='your_auction_summary_link' class='cta-button'>View Auction Summary</a>" +
                                    "</div>" +
                                    "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                    "<p>Best regards,</p>" +
                                    "<p>The Auction Team</p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>",
                            listing.getMainImage()
                    );

                    // Auction Summary Email
                    sendEmailWithImage(
                            listing.getUser().getEmail(),
                            "Auction Summary for " + listing.getMake(),
                            "<html>" +
                                    "<head>" +
                                    "<style>" +
                                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                                    ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                                    ".cta-button { background-color: #3498db; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                                    ".cta-button:hover { background-color: #2980b9; }" +
                                    ".image { width: 100%; max-width: 300px; margin-top: 20px; border-radius: 8px; }" +
                                    "</style>" +
                                    "</head>" +
                                    "<body>" +
                                    "<div class='container'>" +
                                    "<div class='header'>Auction Summary for " + listing.getMake() + "</div>" +
                                    "<div class='content'>" +
                                    "<p>Here is the summary of your auction <strong>" + listing.getMake() + "</strong>.</p>" +
                                    "<p>Review the details below and get more insights about the outcome:</p>" +
                                    "<a href='your_auction_details_link' class='cta-button'>View Auction Details</a>" +
                                    "</div>" +
                                    "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                    "<p>Thank you for using our auction platform!</p>" +
                                    "<p>The Auction Team</p>" +
                                    "</div>" +
                                    "</body>" +
                                    "</html>",
                            listing.getMainImage()
                    );
                } catch (MessagingException e) {
                    System.err.println("Error sending summary email: " + e.getMessage());
                }

            }
        }
        vehicleListingRepo.saveAll(toEnd);
        System.out.println("Ended active auctions: " + toEnd.size());
    }


    public void sendEmailAfterSave(Object dto, String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true enables multipart for attachments
            helper.setTo(email);
            helper.setSubject("Your Listing Has Been Saved!");

            // Use reflection to get the main image filename dynamically
            String mainImageFieldName = "mainImage";  // Assuming all DTOs have a 'mainImage' field
            Method getMainImageMethod = dto.getClass().getMethod("get" + capitalize(mainImageFieldName));
            String mainImage = (String) getMainImageMethod.invoke(dto);

            // Path to the image (assuming it's in "src/main/resources/static/uploads/")
            FileSystemResource resource = new FileSystemResource(new File("src/main/resources/static/uploads/" + mainImage));

            // Use reflection to get the title, price, and other fields dynamically
            String titleField = "title";  // Customize for the DTO
            Method getTitleMethod = dto.getClass().getMethod("get" + capitalize(titleField));
            String title = (String) getTitleMethod.invoke(dto);

            String priceField = "price";
            Method getPriceMethod = dto.getClass().getMethod("get" + capitalize(priceField));
            String price = (String) getPriceMethod.invoke(dto);

            String bidDurationField = "bidDuration";
            Method getBidDurationMethod = dto.getClass().getMethod("get" + capitalize(bidDurationField));
            String bidDuration = (String) getBidDurationMethod.invoke(dto);

            // Email HTML content with embedded image
            String emailContent = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; color: #333; line-height: 1.6; background-color: #f4f4f4; padding: 20px; }" +
                    ".container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
                    ".header { text-align: center; margin-bottom: 20px; }" +
                    ".header h1 { color: #333; font-size: 24px; margin: 0; }" +
                    ".content { font-size: 16px; color: #555; }" +
                    ".content p { margin: 10px 0; }" +
                    ".highlight { color: #2d87f0; font-weight: bold; }" +
                    ".image { max-width: 100%; height: auto; border-radius: 5px; margin-top: 20px; }" +
                    ".footer { text-align: center; margin-top: 20px; font-size: 14px; color: #888; }" +
                    ".footer p { margin: 5px 0; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>" +
                    "<h1> Congratulation Your Listing is submitted!</h1>" +
                    "</div>" +
                    "<div class='content'>" +
                    "<p>Dear User,</p>" +
                    "<p>Your farmed item <span class='highlight'>" + title + "</span> has been successfully listed in the auction.</p>" +
                    "<p><span class='highlight'>Price:</span> " + price + "</p>" +
                    "<p><span class='highlight'>Bid Duration:</span> " + bidDuration + " days</p>" +
                    "<p><span class='highlight'>Auction Start:</span> " + LocalDate.now() + "</p>" +
                    "<p>Main Image:</p>" +
                    "<img class='image' src='cid:mainImage' alt='Auction Image'>" +
                    "</div>" +
                    "<div class='footer'>" +
                    "<p>Best regards,</p>" +
                    "<p>The Auction Team</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(emailContent, true); // Enable HTML content
            helper.addInline("mainImage", resource); // Attach image with CID

            mailSender.send(message);

            System.out.println("Email sent with image to " + email);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }



    private File resizeImage(File imageFile, int width, int height) throws IOException, IOException {
        BufferedImage originalImage = ImageIO.read(imageFile);
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();

        File resizedFile = new File("resized_" + imageFile.getName());
        ImageIO.write(outputImage, "png", resizedFile);

        return resizedFile;
    }

    private void sendEmailWithImage(String to, String subject, String htmlContent, String imageName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);

        // Path to the image (update this path based on your server setup)
        File imageFile = new File("src/main/resources/static/uploads/" + imageName);
        FileSystemResource resource = new FileSystemResource(imageFile);

        // Embed image in the email
        helper.setText(htmlContent, true);
        helper.addInline("mainImage", resource); // 'mainImage' is used in CID

        mailSender.send(message);
    }


}
