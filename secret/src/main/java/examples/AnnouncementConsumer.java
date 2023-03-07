package examples;

import examples.avro.Announcement;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class AnnouncementConsumer {

	public static void main(final String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Please provide the configuration file path as a command line argument");
			System.exit(1);
		}

		final Properties props = ProducerExample.loadConfig(args[0]);

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
		props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

		final String topic = "public.nmp-kafka-workshop.announcements";

		props.put(ConsumerConfig.GROUP_ID_CONFIG, "fredrik-fossum.announcement-consumer");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		System.out.println("Starting Kafka consumer");
		try (final Consumer<String, Announcement> consumer = new KafkaConsumer<>(props)) {
			consumer.subscribe(List.of(topic));
			while (true) {
				ConsumerRecords<String, Announcement> records = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, Announcement> record : records) {
					Announcement value = record.value();
					System.out.printf("Announcement from %s: \"%s\"%n", value.getAuthor(), value.getMessage());
				}
			}
		}
	}
}
