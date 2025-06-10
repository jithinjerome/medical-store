package com.example.medical.store.NotificationSystem.WebSocket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebsocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(Long userId, String message){
        System.out.println("[WebsocketService] Sending message to /topic/notifications/" + userId + ": " + message);
        messagingTemplate.convertAndSend("/topic/user-notifications/" + userId,message);
    }
    public void sendNotificationToStore(int storeId, String message){
        System.out.println("[WebsocketService] Sending message to /topic/notifications/" + storeId + ": " + message);
        messagingTemplate.convertAndSend("/topic/store-notifications/" + storeId,message);
    }
}
