package app.web.mapper;

import app.event.payload.InAppNotificationEvent;
import app.model.Notification;
import app.web.dto.NotificationRequest;
import app.web.dto.NotificationResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Mapper {

    public static NotificationResponse mapNotificationToNotificationResponse(Notification notification) {

        return NotificationResponse.builder()
                .topic(notification.getTopic())
                .body(notification.getBody())
                .dateCreated(notification.getDateCreated())
                .build();
    }

    public static NotificationRequest mapInAppNotificationEventToNotificationRequest(InAppNotificationEvent event) {

        return NotificationRequest.builder()
                .topic(event.getTopic())
                .body(event.getBody())
                .userId(event.getUserId())
                .dateCreated(event.getDateCreated())
                .build();
    }
}
