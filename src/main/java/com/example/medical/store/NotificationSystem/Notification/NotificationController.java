package com.example.medical.store.NotificationSystem.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping(path = "/user/allNotifications/{id}")
    public ResponseEntity<List<Notification>> allNotifications(@PathVariable long id){
        return notificationService.allNotifications(id);
    }

    @DeleteMapping(path = "/user/{userId}/{notificationId}")
    public ResponseEntity<?>deleteNotification(@PathVariable long userId, @PathVariable long notificationId){

        boolean deleted = notificationService.deleteNotification(userId,notificationId);

        if(deleted){
            return ResponseEntity.ok("Notification deleted Successfully");
        } else {
            return ResponseEntity.status(404).body("Notification not found or does not belong to the user");
        }

    }


}
