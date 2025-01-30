package ie.deti.ua.com.lab5_3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import ie.deti.ua.com.lab5_3.model.Message;

@Service
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "lab05_113626";

    public void sendMessage(Message message)
    {
        Message new_msg = new Message();
        new_msg.setType(message.getType());
        new_msg.setNMec(message.getNMec());
        new_msg.setGeneratedNumber(message.getGeneratedNumber());

        System.out.println(String.format("Produced -> %s ",new_msg));
        this.kafkaTemplate.send(TOPIC, message);
    }
}
