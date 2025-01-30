# Caderno Lab5

kafka documentation: https://kafka.apache.org/documentation/

## 5.1

docker-compose.yml to setup kafka 
```
version: '3'
name: lab05
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.4
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181
  
  kafka:
    image: confluentinc/cp-kafka:7.4.4
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_LOG_RETENTION_MS: 10000
      KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS: 5000


  kafdrop:
    image: obsidiandynamics/kafdrop:4.0.2
    ports:
      - "9009:9000"
    environment:
      KAFKA_BROKERCONNECT: "kafka:9092" # Kafka Broker Network Addresses (host:port,host:port)
      SERVER_SERVLET_CONTEXTPATH: "/"
```

Create a kafka topic. In this case the topic "lab05"
```
$ docker exec lab05-kafka-1 kafka-topics --create --topic lab05 --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
```
The topics can be checked in kafdrop: http://localhost:9009/

![image](https://github.com/user-attachments/assets/94b170e6-2448-4929-b2e7-6af2a02d6cee)

Create consumer for the topic "lab05". 
```
$ docker exec lab05-kafka-1 kafka-console-consumer --topic lab05 --from-beginning --bootstrap-server kafka:9092
```
Using the above command, it will be possible to consume all the messages sent over to this
topic. Additionally, we used --from-beginning to consume all messages sent over the topic from
the beginning.

Publishing the data to this Kafka topic (open other terminal):
```
$ docker exec -it lab05-kafka-1 bash
$ kafka-console-producer --topic lab05 --broker-list kafka:9092
Type any messag
```

The messages typed will be received by the consumer:
Producer terminal:
![image](https://github.com/user-attachments/assets/e9baf5c3-a6f9-4527-92ac-029a4c98a278)
First consumer terminal:
![image](https://github.com/user-attachments/assets/befe126b-889d-4aca-95d5-c4bc18fd9af8)
Second consumer terminal (created later):
![image](https://github.com/user-attachments/assets/5cdadfeb-0ccc-4161-9d88-dcd290acca18)

Stopped the consumer:


#### alinea f)
If I add multiple consumers they will also receive the messages of the subscribed topic (in this case lab05).
Consumers will not receive messages sent before they start, they only start receiving messages in the moment they join.

If I stop a consumer and run it again it will receive the messages of the subscribed topic sent when it was desactivated when a new message is sent.


## 5.2
Install poetry:
```bash
$ sudo apt install python3-poetry
```

To create a poetry project and get its tools you should run: 
```bash
$ poetry new [project_name]
$ poetry add pendulum
```
Then import kafka:
```bash
$  pip install kafka-python
```

Create a simple producer and consumer:
- producer
```python
from kafka import KafkaProducer
import json

TOPIC = "lab05_113626"
nMec = 113626

def generate_fibonacci_sequence(n):
    sequence = []
    a, b = 0, 1
    while a <= n:
        sequence.append(a)
        a, b = b, a + b
    return sequence

def main():
    producer = KafkaProducer(
        bootstrap_servers='localhost:29092',
        value_serializer=lambda v: json.dumps(v).encode('utf-8')
    )

    sequence = generate_fibonacci_sequence(nMec)

    for num in sequence:
        message = {'nMec': '113626', 'generatedNumber': num, 'type': 'fibonacci'}
        producer.send(TOPIC, message)
        print(f"Produced: {message}")

    producer.flush()
    producer.close()

if __name__ == "__main__":
    main()
```

- consumer:
```
from kafka import KafkaConsumer
import json

TOPIC = "lab05_113626"

def main():
    consumer = KafkaConsumer(
        TOPIC,
        bootstrap_servers='localhost:29092',
        auto_offset_reset='earliest',
        enable_auto_commit=True,
        value_deserializer=lambda v: json.loads(v.decode('utf-8'))
    )

    for message in consumer:
        print(f"Consumed: {message.value}")

if __name__ == "__main__":
    main()
```

make sure to include the docker-compose.yml in the project to run the kafka container, the portainer should look like this:
![image](https://github.com/user-attachments/assets/07336cb7-fcd2-49e5-b02a-0032bd3e7443)

To build the poetry project:
```bash
$ poetry build
```
Run the consumer and producer:
```bash
$ python3 consumer_example.py
$ python3 producer_example.py
```

Producer execution output:

![image](https://github.com/user-attachments/assets/b5ecdcf9-51f6-4f15-9a65-3d59e36d2bfa)

Consumer execution output:

![image](https://github.com/user-attachments/assets/aa376116-bffe-4d06-8f3f-172657faad76)

Can be checked on kafdrop on the topic page:

![image](https://github.com/user-attachments/assets/667b10c7-98a4-491e-a29b-7b3caf8c3654)

#### c)
Last message: {'nMec': '113626', 'generatedNumber': 75025, 'type': 'fibonacci'}
The consumer received all messages.

#### d) 
Kafka organizes messages into topics, with each topic divided into multiple partitions. Consumers retrieve messages from individual partitions within a topic. Kafka uses offsets—sequential integers starting at zero and increasing by one for each stored message—to track which messages have been read by consumers.

For example, if a consumer reads the first five messages from a partition, Kafka marks the offset up to 4 as committed (following a zero-based sequence) based on the configuration. When the consumer resumes, it begins reading from the next offset, which is 5.


## 5.3
