package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationRequest {

    @NotNull
    private UUID userId;
    @NotBlank
    private String topic;
    @NotBlank
    private String body;

    private String email;

    private LocalDateTime dateCreated;
}
