//package com.example.notification_service;
//
//import com.example.notification_service.entity.Notification;
//import com.example.notification_service.repository.NotificationRepository;
//import com.example.notification_service.service.impl.NotificationServiceImpl;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static javax.management.Query.eq;
//import static javax.management.Query.times;
//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
//import static jdk.jfr.internal.jfc.model.Constraint.any;
//
//@SpringBootTest
//class NotificationServiceApplicationTests {
//    @Mock
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Mock
//    private NotificationRepository notificationRepository;
//
//    @Mock
//    private ThreadPoolTaskScheduler taskScheduler;
//
//    @InjectMocks
//    private NotificationServiceImpl notificationService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateNotifications() {
//        String fileName = "contacts.csv";
//        String eventName = "Event A";
//        Timestamp sendTime = Timestamp.valueOf(LocalDateTime.now().plusHours(1));
//
//        notificationService.createNotifications(fileName, eventName, sendTime);
//
//        verify(kafkaTemplate, times(1)).send(eq("contact-request-topic"), anyString());
//        verify(kafkaTemplate, times(1)).send(eq("event-request-topic"), anyString());
//    }
//
//    @Test
//    public void testSendNotifications() {
//        List<Notification> notifications = new ArrayList<>();
//        Notification notification = new Notification();
//        notification.setId(1L);
//        notification.setEventName("Test Event");
//        notification.setPhoneNumber("123456789");
//        notification.setStatus("PENDING");
//        notifications.add(notification);
//
//        when(notificationRepository.findByNotificationTimeBeforeAndStatus(any(LocalDateTime.class), eq("PENDING")))
//                .thenReturn(notifications);
//
//        notificationService.sendNotifications();
//
//        verify(notificationRepository, times(1)).save(any(Notification.class));
//        verify(kafkaTemplate, times(1)).send(eq("sms-notifications-topic"), anyString());
//    }
//}
