package andrei.assignment1.controllers;

import andrei.assignment1.dtos.energy.WebSocketInDTO;
import andrei.assignment1.dtos.energy.WebSocketOutDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/hello")
    @SendTo("/topic")
    public WebSocketOutDTO greeting(WebSocketInDTO message) throws Exception {
        //Thread.sleep(1000); // simulated delay
        WebSocketOutDTO out = new WebSocketOutDTO();
        out.setMessage("Hello, " + message.getName() + "!");
        return out;
    }
}
