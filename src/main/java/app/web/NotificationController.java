package app.web;

import app.model.Notification;
import app.service.NotificationService;
import app.web.dto.NotificationRequest;
import app.web.dto.NotificationResponse;
import app.web.dto.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> processNotification(@RequestBody NotificationRequest notificationRequest) {

        Notification notification = notificationService.create(notificationRequest);
        NotificationResponse response = Mapper.mapNotificationToNotificationResponse(notification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@RequestParam("userId") UUID userId) {

        List<NotificationResponse> notifications = notificationService.getNotifications(userId)
                .stream()
                .map(Mapper::mapNotificationToNotificationResponse)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notifications);
    }

    @PutMapping
    public ResponseEntity<Void> archiveUserNotifications(@RequestParam("userId") UUID userId) {

        notificationService.clearNotifications(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }
}
