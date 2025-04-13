package app.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {

    private String topic;

    private String body;

    private LocalDateTime dateCreated;

    private boolean unread;
}
