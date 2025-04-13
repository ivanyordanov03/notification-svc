package app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column(nullable = false)
    private UUID userId;

    private boolean unread = true;

    private boolean archived = false;
}
