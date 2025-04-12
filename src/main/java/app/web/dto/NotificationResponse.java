package app.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {

    private String title;

    private String body;

    private LocalDateTime dateCreated;
}
