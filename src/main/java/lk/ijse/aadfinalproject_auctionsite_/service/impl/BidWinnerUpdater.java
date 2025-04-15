package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.aadfinalproject_auctionsite_.entity.FarmedItem;
import lk.ijse.aadfinalproject_auctionsite_.entity.PlaceBid;
import lk.ijse.aadfinalproject_auctionsite_.repo.FarmedItemListing;
import lk.ijse.aadfinalproject_auctionsite_.repo.LandListingRepo;
import lk.ijse.aadfinalproject_auctionsite_.repo.PlaceOrderRepo;
import lk.ijse.aadfinalproject_auctionsite_.repo.VehicleListingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BidWinnerUpdater {
    private final FarmedItemListing farmedListingRepository;
    private final LandListingRepo landListingRepo;
    private final VehicleListingRepo vehicleListingRepo;
    private final EmailService emailService;
    private final JavaMailSender mailSender;

    @Autowired
    private PlaceOrderRepo placeOrderRepo;



    public BidWinnerUpdater(FarmedItemListing farmedListingRepository, LandListingRepo landListingRepo, VehicleListingRepo vehicleListingRepo, EmailService emailService, JavaMailSender mailSender) {
        this.farmedListingRepository = farmedListingRepository;
        this.landListingRepo = landListingRepo;
        this.vehicleListingRepo = vehicleListingRepo;
        this.emailService = emailService;
        this.mailSender = mailSender;
    }


    @Scheduled(cron = "0 * * * * *") // Every day at 1 AM
    public void checkPendingWinners() throws MessagingException {
        List<FarmedItem> pendingListings = farmedListingRepository.findByStatusAndSellTypeAndSold("End", "both", false);

        System.out.println("pendingListings: " + pendingListings.size());
        LocalDate today = LocalDate.now();

        for (FarmedItem listing : pendingListings) {
            System.out.println(listing);
            if (listing.getWinnerAssignedDate() != null) {
                long daysSinceAssigned = ChronoUnit.DAYS.between(listing.getWinnerAssignedDate(), today);
                System.out.println(daysSinceAssigned);

                if (daysSinceAssigned <= 5) {
                    // Give 5 days to the first bidder
                    handleBidderAttempt(listing, 0, today);
                } else if (daysSinceAssigned <= 10) {
                    // If 5 days passed, give 5 more days to the second bidder
                    handleBidderAttempt(listing, 1, today);
                } else if (daysSinceAssigned <= 15) {
                    // If 10 days passed, give 5 more days to the third bidder
                    handleBidderAttempt(listing, 2, today);
                } else {
                    // If 15 days passed, set status to expired
                    listing.setStatus("Expired");
                    listing.setSold(false);
                    listing.setCurrentWinningBidId(null);
                    listing.setWinnerAssignedDate(null);

                    farmedListingRepository.save(listing);

                    System.out.println("No successful bidders for listing " + listing.getId());
                }

                // Send reminder email if 3 days have passed since winner was assigned
                sendReminderEmailsForBidder(listing, daysSinceAssigned);
            }
        }
    }

    private void handleBidderAttempt(FarmedItem listing, int attemptIndex, LocalDate today) throws MessagingException {
        int currentAttempt = listing.getWinnerAttemptCount();
        if (currentAttempt == attemptIndex) {
            // Get top 3 bids again, but limit to the next bidder
            PageRequest pageRequest = PageRequest.of(0, 3);
            List<PlaceBid> topBids = placeOrderRepo.findTop3Bids(listing.getId(), "farmed", pageRequest);

            if (topBids.size() > attemptIndex) {
                PlaceBid nextBid = topBids.get(attemptIndex);
                System.out.println(nextBid.getUser().getEmail());
                System.out.println(nextBid.getBidAmount());

                // Update the listing with the next winning bid
                listing.setCurrentWinningBidId(nextBid.getId());
                listing.setWinnerAttemptCount(currentAttempt + 1);
                listing.setWinnerAssignedDate(today);

                farmedListingRepository.save(listing);
                System.out.println(nextBid.getUser().getEmail());

                // Send email to next bidder
                sendEmailWithImage(
                        nextBid.getUser().getEmail(),
                        "Congratulations! You're the Winning Bidder!",
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
                                "<div class='header'>Congratulations! You're the Winning Bidder!</div>" +
                                "<div class='content'>" +
                                "<p>Good news! You're the winning bidder for <strong>" + listing.getTitle() + "</strong>!</p>" +
                                "<p>We are excited to let you know that your bid has been selected as the highest for this auction. You now have 5 days to complete your purchase.</p>" +
                                "<p>Your winning bid amount is: <strong>" + nextBid.getBidAmount() + "</strong></p>" +
                                "<p>Click below to complete your purchase and finalize the transaction:</p>" +
                                "<a href='your_auction_link' class='cta-button'>Complete Your Purchase</a>" +
                                "</div>" +
                                "<img src='cid:mainImage' class='image' alt='Auction Image'>" +
                                "<p>Best regards,</p>" +
                                "<p>The Auction Team</p>" +
                                "</div>" +
                                "</body>" +
                                "</html>",
                        listing.getMainImage() // Path to the main image
                );
            }
        }
    }

    // Send reminder emails for each bidder after 3, 6, and 9 days
    private void sendReminderEmailsForBidder(FarmedItem listing, long daysSinceAssigned) throws MessagingException {
        if (listing.getWinnerAttemptCount() >= 1 && daysSinceAssigned == 3) {
            // First bidder reminder (3 days after winner assignment)
            sendReminderEmail(listing, 0);
        } else if (listing.getWinnerAttemptCount() >= 2 && daysSinceAssigned == 6) {
            // Second bidder reminder (6 days after winner assignment)
            sendReminderEmail(listing, 1);
        } else if (listing.getWinnerAttemptCount() >= 3 && daysSinceAssigned == 9) {
            // Third bidder reminder (9 days after winner assignment)
            sendReminderEmail(listing, 2);
        }
    }

    private void sendReminderEmail(FarmedItem listing, int bidderIndex) throws MessagingException {
        // Get the corresponding bidder (first, second, or third)
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<PlaceBid> topBids = placeOrderRepo.findTop3Bids(listing.getId(), "farmed", pageRequest);

        if (topBids.size() > bidderIndex) {
            PlaceBid bidder = topBids.get(bidderIndex);
            String email = bidder.getUser().getEmail();
            String message = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; text-align: center; }" +
                    ".container { max-width: 600px; margin: 30px auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); }" +
                    ".header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }" +
                    ".content { font-size: 16px; line-height: 1.6; color: #555; margin-bottom: 30px; }" +
                    ".cta-button { background-color: #2c3e50; color: #fff; padding: 12px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; }" +
                    ".cta-button:hover { background-color: #34495e; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<div class='header'>Reminder: Complete Your Auction Purchase</div>" +
                    "<div class='content'>" +
                    "<p>Dear Bidder,</p>" +
                    "<p>We noticed that you have won the auction for <strong>" + listing.getTitle() + "</strong> but haven't completed the purchase yet.</p>" +
                    "<p>Don't forget to finalize your transaction! You have 2 more days to complete your purchase.</p>" +
                    "<p>Click below to finalize the purchase:</p>" +
                    "<a href='your_auction_link' class='cta-button'>Complete Your Purchase</a>" +
                    "</div>" +
                    "<p>Best regards,</p>" +
                    "<p>The Auction Team</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            sendEmailWithImage(
                    email,
                    "Reminder: Complete Your Auction Purchase",
                    message,
                    listing.getMainImage() // Path to the main image
            );

            System.out.println("Reminder email sent to " + email);
        }
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
