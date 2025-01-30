package ie.deti.ua.com.lab5_3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import ie.deti.ua.com.lab5_3.model.Message;

@Service
public class ConsumerService {
    @Autowired
    private static final String TOPIC = "lab05_113626";

    @KafkaListener(topics = TOPIC, groupId = "listener")
    public void consume(Message message) {
        System.out.println(String.format("Message recieved -> %s", message));
    }
}