package com.enjoyf.platform.util.publish;

import com.enjoyf.platform.util.FiveProps;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/15
 */
public class KafkaConsumer {

    private ExecutorService executor;
    private ConsumerConnector consumerConnector;


    public KafkaConsumer(FiveProps fiveProps, KafkaConsumerProcessListener listener, String topic, String groupId,int threadNum) {

        String zookeeperConnect = fiveProps.get("kafka.zookeeper.connect");
//        String groupId = fiveProps.get("kafka.group.id");
        String zkSyncTimeMill = fiveProps.get("kafka.zookeeper.sync.time.ms", "200");
        String zkSessionTimeoutMill = fiveProps.get("kafka.zookeeper.session.timeout.ms", "400");
        String autoCommitIntervalMill = fiveProps.get("kafka.auto.commit.interval.ms", "1000");

        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeperConnect);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", zkSessionTimeoutMill);
        props.put("zookeeper.sync.time.ms", zkSyncTimeMill);
        props.put("auto.commit.interval.ms", autoCommitIntervalMill);

        ConsumerConfig config = new ConsumerConfig(props);
        consumerConnector = Consumer.createJavaConsumerConnector(config);

        process(listener, topic, threadNum);
    }

    private void process(final KafkaConsumerProcessListener listener, String topic, int threadNum) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(topic, threadNum);

        Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams = consumerConnector.createMessageStreams(map);
        List<KafkaStream<byte[], byte[]>> streams = topicMessageStreams.get(topic);

        executor = Executors.newFixedThreadPool(threadNum);
        for (final KafkaStream stream : streams) {
            executor.submit(new Runnable() {
                public void run() {
                    listener.process(stream);
                }
            });
        }
    }

    public void shutdown() {
        if (consumerConnector != null) consumerConnector.shutdown();
        if (executor != null) executor.shutdown();
    }


   public interface KafkaConsumerProcessListener {
        void process(KafkaStream stream);
    }


//    public static void main(String[] args) {
//        FiveProps fiveProps = Props.instance().getServProps();
//        KafkaConsumer consumer = new KafkaConsumer(fiveProps, new KafkaConsumerProcessListener() {
//            @Override
//            public void process(KafkaStream stream) {
//                ConsumerIterator iter = stream.iterator();
//
//                //该循环会持续从Kafka读取数据，直到手工的将进程进行中断
//                while (iter.hasNext()) {
//                    StringBuffer sb = new StringBuffer();
//                    MessageAndMetadata metaData = iter.next();
//                    if (metaData.message() == null) {
//                        continue;
//                    }
//                    String message = new String((byte[]) metaData.message());
//                    sb.append("Part: " + metaData.partition() + " ");
//                    sb.append("Key: " + metaData.key() + " ");
//                    sb.append("offset: " + metaData.offset() + " ");
//                    sb.append("Message: " + message + " ");
//                    sb.append("\n");
//                    System.out.println(sb.toString());
//                }
//            }
//        }, "pclog","liuhao-group", 1);
//    }
}
