package kafka.myconsumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.TreeMap;

public class MyConsumer{
    private static final Logger log = LoggerFactory.getLogger(MyConsumer.class);
    public static void main(String[] args) {
        log.info("I am a Kafka Consumer");

        String bootstrapServers = "140.113.151.61:9092";
        String groupId = "q3-consumer";

        String topic = "question3-v1-topic";

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // get a reference to the current thread
        final Thread mainThread = Thread.currentThread();
        TreeMap<Integer, String> orderedMessages = new TreeMap<>();

        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                consumer.wakeup();

                // join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    log.info(e.toString());
                }
            }
        });

        try{
            consumer.subscribe(Collections.singletonList(topic));
            System.out.println("Start listen incoming messages ...");
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    try {
                        String value = record.value();
                        int order = Integer.parseInt(value.split(" ")[1]);
                        orderedMessages.put(order, value);
                        log.info("Key: " + record.key() + ", Value: " + record.value());
                        log.info("Partition: " + record.partition() + ", Offset:" + record.offset());
                    } catch (NumberFormatException e) {
                        log.error("Error parsing sequence number from message: " + record.value(), e);
                    }
                }

                for (String message : orderedMessages.values()) {
                    System.out.println("Received in order: " + message);
                }
                orderedMessages.clear();
            }
        }catch(WakeupException e){
            log.info("Wake up exception!");
        }catch (Exception e) {
            log.error("Unexpected exception", e);
        }
        finally{
            consumer.close();
            log.info("The consumer is now closed.");
        }
    }

}
