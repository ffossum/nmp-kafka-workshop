package examples;

import examples.avro.Announcement;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Properties;

public class AnnouncementProducer {

	public static void main(final String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Please provide the configuration file path as a command line argument");
			System.exit(1);
		}

		final Properties props = ProducerExample.loadConfig(args[0]);

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

		final String topic = "public.nmp-kafka-workshop.announcements";

		String key = "Fredrik";
		Announcement value = new Announcement();
		value.setAuthor(key);
		value.setMessage("Congratulations! You have finished the NMP Kafka Workshop!");

		try (final KafkaProducer<String, Announcement> producer = new KafkaProducer<>(props)) {
			producer.send(new ProducerRecord<>(topic, key, value));
		}
	}
}
