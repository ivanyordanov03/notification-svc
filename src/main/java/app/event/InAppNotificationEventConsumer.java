package app.event;

import app.event.payload.InAppNotificationEvent;
import app.service.NotificationService;
import app.web.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InAppNotificationEventConsumer {

    private final NotificationService notificationService;

    @Autowired
    public InAppNotificationEventConsumer(NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "in-app-notification-event.v1", groupId = "notification-svc")
    public void consumeEvent(InAppNotificationEvent event) {

        notificationService.create(Mapper.mapInAppNotificationEventToNotificationRequest(event));
        log.info("Successfully consumed notification event for user with id [%s]".formatted(event.getUserId()));
    }
}
