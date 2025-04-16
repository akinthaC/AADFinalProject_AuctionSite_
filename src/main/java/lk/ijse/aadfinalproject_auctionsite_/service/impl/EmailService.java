
package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendInquiryEmail(String toEmail, String orderId, String messageContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String subject = "📬 Inquiry Response – Order ID: " + orderId;
            String htmlMsg = "<div style='font-family: Arial, sans-serif; padding: 20px;'>" +
                    "<h2 style='color: #2E86C1;'>Response to Your Inquiry</h2>" +
                    "<p>Hi,</p>" +
                    "<p>Thank you for contacting us. This is a response to your inquiry related to:</p>" +
                    "<p><strong>Order ID:</strong> " + orderId + "</p>" +
                    "<p><strong>Message:</strong><br>" + messageContent + "</p>" +
                    "<br><p style='color: #555;'>If you have more questions, feel free to reply to this email.</p>" +
                    "<br><p>Best Regards,<br><strong>Support Team</strong></p>" +
                    "</div>";

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlMsg, true);
            helper.setFrom("your-email@gmail.com"); // change this to your sender email

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendSellerEmail(String email, String orderId, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject("🛒  Order Notification - Order ID: " + orderId);

            String htmlContent = "<div style='font-family:Arial,sans-serif; padding:20px; background:#f9f9f9; border-radius:8px;'>"
                    + "<h2 style='color:#4CAF50;'>New Message Regarding Order ID: " + orderId + "</h2>"
                    + "<p style='font-size:16px; color:#333;'>Hello Seller,</p>"
                    + "<p style='font-size:15px; background:#fff3cd; padding:10px; border:1px solid #ffeeba; border-radius:4px;'>"
                    + message + "</p>"
                    + "<p style='font-size:14px; color:#888;'>Please take necessary action as soon as possible.</p>"
                    + "<hr style='margin:20px 0;'>"
                    + "<p style='font-size:14px; color:#555;'>Regards,<br><strong>Auction Site Team</strong></p>"
                    + "</div>";

            helper.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
            System.out.println("Modern styled email sent successfully to " + email);

        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}
