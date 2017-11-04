package com.enjoyf.platform.util.publish.test;/**
 * Created by ericliu on 16/8/15.
 */

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.publish.KafkaConsumer;
import com.enjoyf.platform.util.publish.KafkaProducer;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/15
 */
public class Main {
    public static void main(String[] args) {
        FiveProps fiveProps = Props.instance().getServProps();
        KafkaConsumer consumer = new KafkaConsumer(fiveProps, new KafkaConsumer.KafkaConsumerProcessListener() {
            @Override
            public void process(KafkaStream stream) {
                ConsumerIterator iter = stream.iterator();

                //该循环会持续从Kafka读取数据，直到手工的将进程进行中断
                while (iter.hasNext()) {
                    StringBuffer sb = new StringBuffer();
                    MessageAndMetadata metaData = iter.next();
                    if (metaData.message() == null) {
                        continue;
                    }
                    String message = new String((byte[]) metaData.message());
                    sb.append("Part: " + metaData.partition() + " ");
                    sb.append("Key: " + metaData.key() + " ");
                    sb.append("offset: " + metaData.offset() + " ");
                    sb.append("Message: " + message + " ");
                    sb.append("\n");
                    System.out.println(sb.toString());
                }
            }
        }, "pclog", "liuhao-group", 1);

        KafkaProducer producer=new KafkaProducer(fiveProps);
        Future<RecordMetadata> future=producer.send("pclog","liuhao");

        while (true){
            if(future.isDone()){
                producer.close();
                System.out.println("send complete");
                break;
            }
        }
    }
}
