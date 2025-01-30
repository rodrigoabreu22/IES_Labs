package ie.deti.ua.com.lab5_3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ie.deti.ua.com.lab5_3.service.ProducerService;
import ie.deti.ua.com.lab5_3.model.Message;
@RestController
@RequestMapping(value = "/kafka")
public class ProducerController {
    private final ProducerService producerService;

    @Autowired
    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestBody Message message) {
        this.producerService.sendMessage(message);
    }
}