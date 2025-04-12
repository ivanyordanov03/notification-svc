package app.service;

import app.model.Notification;
import app.repository.NotificationRepository;
import app.web.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSenderImpl mailSender;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, JavaMailSenderImpl mailSender) {

        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    public Notification create (NotificationRequest notificationRequest) {

        Notification notification;

        if (notificationRequest.getEmail() != null) {
            notification = sendEmail(notificationRequest);
        } else {
            notification = Notification.builder()
                    .title(notificationRequest.getTitle())
                    .body(notificationRequest.getBody())
                    .userId(notificationRequest.getUserId())
                    .dateCreated(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        }
        return notification;
    }

    private Notification sendEmail(NotificationRequest notificationRequest) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notificationRequest.getEmail());
        message.setSubject(notificationRequest.getTitle());
        message.setText(notificationRequest.getBody());

        String status;
        String errorMessage = "";
        try {
            mailSender.send(message);
            status = "was sent successfully";
        } catch (Exception e) {
            status = "failed";
            errorMessage =  " due to "  + e.getMessage();
        }
        Notification notification = Notification.builder()
                .title("Email notification %s".formatted(status))
                .body("Email notification to email %s %s%s".formatted(notificationRequest.getEmail(), status, errorMessage))
                .userId(notificationRequest.getUserId())
                .dateCreated(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

        return notification;
    }

    public List<Notification> getNotifications(UUID userId) {

        return notificationRepository.findAllByUserIdAndArchivedIsFalseOrderByDateCreatedDesc(userId);
    }

    public void clearNotifications(UUID userId) {

        getNotifications(userId).stream()
                .filter(n -> (!n.isUnread()))
                .forEach(notification -> {
                    notification.setArchived(true);
                    notificationRepository.save(notification);
                });
    }
}
