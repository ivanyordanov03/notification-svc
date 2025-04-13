package app.web;

import app.model.Notification;
import app.service.NotificationService;
import app.web.dto.NotificationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerApiTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void processNotification_withValidRequest_shouldReturnCreated() throws Exception {
        // Given
        NotificationRequest request = NotificationRequest.builder()
                .userId(UUID.randomUUID())
                .topic("Test Message")
                .body("Test Body")
                .dateCreated(LocalDateTime.now())
                .build();

        Notification mockNotification = Notification.builder()
                .id(UUID.randomUUID())
                .topic(request.getTopic())
                .body(request.getBody())
                .userId(request.getUserId())
                .dateCreated(LocalDateTime.now())
                .build();;

        when(notificationService.create(any(NotificationRequest.class)))
                .thenReturn(mockNotification);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getUserNotifications_withNoNotifications_shouldReturnOkWithEmptyList() throws Exception {

        UUID userId = UUID.randomUUID();
        when(notificationService.getNotifications(eq(userId))).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/notifications")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void archiveUserNotifications_shouldReturnEmptyBody() throws Exception {

        mockMvc.perform(delete("/api/v1/notifications")
                        .param("userId", UUID.randomUUID().toString()))
                .andExpect(content().string(""));
    }

    @Test
    void markAllAsRead_withValidUserId_shouldReturnOk() throws Exception {

        UUID userId = UUID.randomUUID();

        mockMvc.perform(put("/api/v1/notifications")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(notificationService, times(1)).markAllAsRead(userId);
    }
}