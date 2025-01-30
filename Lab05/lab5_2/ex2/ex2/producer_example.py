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