package com.example.medical.store.NotificationSystem.Notification;


import com.example.medical.store.NotificationSystem.WebSocket.WebsocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private WebsocketService websocketService;

    public void notifyUser(long userId, String message, String type){

        System.out.println("[NotificationService] CALLED notifyUser()");
        System.out.println("[NotificationService] USERID: " + userId);
        System.out.println("[NotificationService] MESSAGE: " + message);
        System.out.println("[NotificationService] TYPE: " + type);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);
        notificationRepository.save(notification);

        try{
            Map<String, String> payload = new HashMap<>();
            payload.put("message",message);
            payload.put("type",type);

            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(payload);
            websocketService.sendNotificationToUser(userId,jsonPayload);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void notifyStore(int storeId, String message, String type){

        System.out.println("[NotificationService] CALLED notifyStore()");
        System.out.println("[NotificationService] STOREID: " + storeId);
        System.out.println("[NotificationService] MESSAGE: " + message);
        System.out.println("[NotificationService] TYPE: " + type);

        Notification notification = new Notification();
        notification.setStoreId(storeId);
        notification.setMessage(message);
        notification.setType(type);
        notificationRepository.save(notification);

        try{
            Map<String, String> payload = new HashMap<>();
            payload.put("message",message);
            payload.put("type",type);

            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(payload);
            websocketService.sendNotificationToStore(storeId,jsonPayload);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public boolean deleteNotification(long userId, long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notificationId);

        if(notificationOptional.isPresent()){
            Notification notification = notificationOptional.get();
            if(notification.getUserId()==userId ){
                notificationRepository.deleteById(notificationId);
                return true;
            }
        }
        return false;
    }


    public ResponseEntity<List<Notification>> allNotifications(long id) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedDesc(id);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}
