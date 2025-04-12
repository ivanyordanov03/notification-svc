package app.web.dto.mapper;

import app.model.Notification;
import app.web.dto.NotificationResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Mapper {

    public static NotificationResponse mapNotificationToNotificationResponse(Notification notification) {

        return NotificationResponse.builder()
                .title(notification.getTitle())
                .body(notification.getBody())
                .dateCreated(notification.getDateCreated())
                .build();
    }
}
