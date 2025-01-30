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