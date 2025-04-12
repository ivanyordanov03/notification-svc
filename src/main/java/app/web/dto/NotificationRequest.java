package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class NotificationRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotNull
    private UUID userId;

    private String email;
}
