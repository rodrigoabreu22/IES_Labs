package ie.deti.ua.com.lab5_3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab53Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab53Application.class, args);
	}


	//@Bean
	//public NewTopic topic() {
	//	return TopicBuilder.name("lab05_113626")
	//			.partitions(10)
	//			.replicas(1)
	//			.build();
	//}

	//@KafkaListener(id = "myId", topics = "lab05_113626")
	//public void listen(String in) {
	//	System.out.println(in);
	//}
}
