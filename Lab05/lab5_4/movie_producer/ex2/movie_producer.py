from kafka import KafkaProducer
import json
import random
import time

TOPIC = "lab05_113626"
nMec = 113626

quotes = [
    {"quote": "May the Force be with you.", "movie": {"title": "Star Wars", "year": 1977}},
    {"quote": "I'm going to make him an offer he can't refuse.", "movie": {"title": "The Godfather", "year": 1972}},
    {"quote": "You talking to me?", "movie": {"title": "Taxi Driver", "year": 1976}},
    {"quote": "I'll be back.", "movie": {"title": "The Terminator", "year": 1984}},
    {"quote": "Here's looking at you, kid.", "movie": {"title": "Casablanca", "year": 1942}},
    {"quote": "I am your father.", "movie": {"title": "Star Wars", "year": 1980}},
    {"quote": "Do, or do not. There is no try.", "movie": {"title": "Star Wars", "year": 1980}},
    {"quote": "Help me, Obi-Wan Kenobi. You're my only hope.", "movie": {"title": "Star Wars", "year": 1977}},
    {"quote": "The Force will be with you, always.", "movie": {"title": "Star Wars", "year": 1977}},
    {"quote": "Revenge is a dish best served cold.", "movie": {"title": "The Godfather", "year": 1972}},
    {"quote": "Keep your friends close, but your enemies closer.", "movie": {"title": "The Godfather Part II", "year": 1974}},
    {"quote": "Never hate your enemies. It affects your judgment.", "movie": {"title": "The Godfather Part III", "year": 1990}},
    {"quote": "I believe in America.", "movie": {"title": "The Godfather", "year": 1972}},
    {"quote": "Someday a real rain will come and wash all this scum off the streets.", "movie": {"title": "Taxi Driver", "year": 1976}},
    {"quote": "I’m God’s lonely man.", "movie": {"title": "Taxi Driver", "year": 1976}},
    {"quote": "I’m the guy who’s gonna save you.", "movie": {"title": "Taxi Driver", "year": 1976}},
    {"quote": "Come with me if you want to live.", "movie": {"title": "The Terminator", "year": 1984}},
    {"quote": "The future is not set. There is no fate but what we make for ourselves.", "movie": {"title": "Terminator 2: Judgment Day", "year": 1991}},
    {"quote": "Hasta la vista, baby.", "movie": {"title": "Terminator 2: Judgment Day", "year": 1991}},
    {"quote": "I’ll be back.", "movie": {"title": "The Terminator", "year": 1984}},
    {"quote": "We’ll always have Paris.", "movie": {"title": "Casablanca", "year": 1942}},
    {"quote": "Round up the usual suspects.", "movie": {"title": "Casablanca", "year": 1942}},
    {"quote": "Of all the gin joints in all the towns in all the world, she walks into mine.", "movie": {"title": "Casablanca", "year": 1942}},
    {"quote": "I stick my neck out for nobody.", "movie": {"title": "Casablanca", "year": 1942}},
    {"quote": "I fought all my life. I never thought I'd be in this position.", "movie": {"title": "Creed", "year": 2015}},
    {"quote": "I’m not a mistake, I’m a choice.", "movie": {"title": "Creed", "year": 2015}},
    {"quote": "You don't know me. You don't know what I've been through.", "movie": {"title": "Creed", "year": 2015}},
    {"quote": "You don't have to do this alone.", "movie": {"title": "Creed", "year": 2015}},
    {"quote": "I ain't scared of nobody.", "movie": {"title": "Creed", "year": 2015}},
    {"quote": "I need you, because you're the one who can teach me what it means to be a champion.", "movie": {"title": "Creed", "year": 2015}},
]


def generate_quote():
    return random.choice(quotes)

def main():
    producer = KafkaProducer(
        bootstrap_servers='localhost:29092',
        value_serializer=lambda v: json.dumps(v).encode('utf-8')
    )

    print("Starting Kafka producer...")

    try:
        while True:
            quote = generate_quote()
            message = {
                "quote": quote["quote"],
                "movie": {
                    "title": quote["movie"]["title"],
                    "year": quote["movie"]["year"]
                }
            }

            # Send the quote to the Kafka topic
            producer.send(TOPIC, message)
            print(f"Produced: {message}")

            # Random sleep between 5 to 10 seconds
            time.sleep(random.uniform(5, 10))
    except KeyboardInterrupt:
        print("Stopping Kafka producer.")
    finally:
        producer.flush()
        producer.close()

if __name__ == "__main__":
    main()
