package andrei.assignment1.controllers;


import andrei.assignment1.dtos.ChatMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @MessageMapping("/greeting")
    public void greeting(ChatMessageDTO message) {
        System.out.println("Message received: " + message);
        if(message.getMessage().getPosition() != null){
            if(message.getMessage().getPosition().equals("left")) {
                message.getMessage().setPosition("right");
            } else {
                message.getMessage().setPosition("left");
            }
        }
        // Construct the destination topic
        String destination = "/topic/" + message.getDestinationUserId();
        // Send the message to the specific topic
        messagingTemplate.convertAndSend(destination, message);
    }
}
