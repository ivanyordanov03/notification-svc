package app.event.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InAppNotificationEvent {

    private UUID userId;

    private UUID senderId;

    private String topic;

    private String body;

    private LocalDateTime dateCreated;
}
