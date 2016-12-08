package master2016;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class TwitterKafkaConsumer {
	
	
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");props.put("group.id", "MYGROUP");
		props.put("enable.auto.commit", "true");props.put("auto.commit.interval.ms", "1000");props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		try{ consumer.subscribe(Arrays.asList("master2016-replicated-java","myTopic"));
		while (true) {
		ConsumerRecords<String, String> records = consumer.poll(10);
		for (ConsumerRecord<String, String> record : records){
		System.out.print("Topic: " + record.topic() + ", ");
		System.out.print("Partition: " + record.partition() + ", "); System.out.print("Key: " + record.key() + ", ");
		System.out.println("Value: " + record.value() + ", ");
		}
		}
		}catch (Exception e){e.printStackTrace();}
		finally { consumer.close();}
		}
}