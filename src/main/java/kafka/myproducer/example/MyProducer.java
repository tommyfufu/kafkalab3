package kafka.myproducer.example;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MyProducer {
    private static final Logger log = LoggerFactory.getLogger(MyProducer.class);

    public static class CustomPartitioner implements Partitioner{
        @Override
        public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
            List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
            int numPartitions = partitions.size();
            if (keyBytes != null) {
                return 0;
            }
            return Utils.toPositive(Utils.murmur2(valueBytes)) % (numPartitions);
        }
        @Override
        public void close() {
            // No operation needed for this example
        }
        @Override
        public void configure(Map<String, ?> configs) {
            // No configuration needed for this example
        }
    }

    public static void main(String[] args){
        log.info("I am a Kafka Producer");
        String bootstrapServers = "140.113.151.61:9092";

        String topic = "question3-v1-topic";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,  bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,  StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            String message = "Message " + i;
            log.info(message);
            producer.send(new ProducerRecord<>(topic, Integer.toString(i), message));
        }
        producer.flush();
        producer.close();
    }

}
