package com.enjoyf.platform.util.publish;


import com.enjoyf.platform.util.FiveProps;
import org.apache.kafka.clients.producer.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/15
 */
public class KafkaProducer {

    private Producer<String, String> producer;

    public KafkaProducer(FiveProps fiveProps) {

        String metadataBrokerList = fiveProps.get("kafka.producer.metadata.broker.list");
        String serializerClass = fiveProps.get("kafka.serializer.class");
        String keySerializerClass = fiveProps.get("kafka.key.serializer.class");
        String valueSerializerClass = fiveProps.get("kafka.value.serializer.class");
//        String partitionerClass = fiveProps.get("kafka.partitioner.class");
        int requestRequiredAcks = fiveProps.getInt("kafka.request.required.acks", 1);



        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("metadata.broker.list",metadataBrokerList);
        properties.put("bootstrap.servers", metadataBrokerList);
        properties.put("serializer.class", serializerClass);
        // key.serializer.class默认为serializer.class

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);

        // 可选配置，如果不配置，则使用默认的partitioner
//        properties.put("partitioner.class", partitionerClass);
        // 触发acknowledgement机制，否则是fire and forget，可能会引起数据丢失
        // 值为0,1,-1,可以参考
        // http://kafka.apache.org/08/configuration.html
        properties.put("request.required.acks", requestRequiredAcks);

        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);
    }


    public Future<RecordMetadata> send(String topic, String value){
        ProducerRecord<String, String> producerRecord=  new ProducerRecord<String, String>(topic,value);
        return producer.send(producerRecord);
    }

    public void close(){
        producer.close();

    }


}
