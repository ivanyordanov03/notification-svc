package app.notification;

import app.model.Notification;
import app.repository.NotificationRepository;
import app.service.NotificationService;
import app.web.dto.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceUTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private JavaMailSenderImpl mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void create_withoutEmail_shouldSaveRegularNotification() {

        NotificationRequest request = new NotificationRequest(
                UUID.randomUUID(), "Test Topic", "Test Body", null, null);

        when(notificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Notification result = notificationService.create(request);

        assertNotNull(result);
        assertEquals(request.getTopic(), result.getTopic());
        assertEquals(request.getBody(), result.getBody());
        assertEquals(request.getUserId(), result.getUserId());
        assertFalse(result.isUnread());
        assertFalse(result.isArchived());
        verify(notificationRepository).save(any(Notification.class));
        verifyNoInteractions(mailSender);
    }

    @Test
    void create_withEmail_shouldSendEmailAndSaveNotification() {

        NotificationRequest request = new NotificationRequest(
                UUID.randomUUID(), "Test Topic", "Test Body", "test@example.com", null);

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        Notification result = notificationService.create(request);

        assertNotNull(result);
        assertTrue(result.getTopic().contains("was sent successfully"));
        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void create_withEmailFailure_shouldSaveFailedNotification() {

        NotificationRequest request = new NotificationRequest(
                UUID.randomUUID(), "Test Topic", "Test Body", "test@example.com", null);

        doThrow(new MailException("SMTP error") {}).when(mailSender).send(any(SimpleMailMessage.class));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        Notification result = notificationService.create(request);

        assertNotNull(result);
        assertTrue(result.getTopic().contains("failed due to"));
        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void markAllAsRead_shouldUpdateAllNotificationsToRead() {

        UUID userId = UUID.randomUUID();
        Notification notification1 = new Notification();
        notification1.setUnread(true);
        Notification notification2 = new Notification();
        notification2.setUnread(true);
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationRepository.findAllByUserIdAndArchivedIsFalseOrderByDateCreatedDesc(userId)).thenReturn(notifications);
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.markAllAsRead(userId);

        verify(notificationRepository).findAllByUserIdAndArchivedIsFalseOrderByDateCreatedDesc(userId);
        verify(notificationRepository, times(2)).save(any(Notification.class));
        assertFalse(notification1.isUnread());
        assertFalse(notification2.isUnread());
    }
}
