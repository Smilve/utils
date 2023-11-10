package com.lvboaa.utils.kafka;

import com.lvboaa.utils.BaseTest;
import com.lvboaa.utils.kafka.config.MyPartitioner;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 *
 * @author: lv.bo
 * @create: 2023-11-09 16:18
 */
public class FirstKafkaTest {

    protected static final Logger logger = LoggerFactory.getLogger(FirstKafkaTest.class);

    /**
     *  第一次kafka生产者发送，普通异步发送
     */
    @Test
    public void testFirstASynKafkaProducer(){
        // 配置
        Properties properties = new Properties();
        // 连接kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 发送消息
        for (int i = 0; i < 5; i++) {
            kafkaProducer.send(new ProducerRecord<>("test", "hello, world"));
        }
        // 关闭资源
        kafkaProducer.close();
    }

    /**
     *  带回调的kafka生产者异步发送
     */
    @Test
    public void testCallBackASynKafkaProducer(){
        // 配置
        Properties properties = new Properties();
        // 连接kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 发送消息
        for (int i = 0; i < 5; i++) {
            kafkaProducer.send(new ProducerRecord<>("test", i+"","hello, world"), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // 发送成功
                    if (e == null){
                        logger.info("topic[{}],分区[{}]，offset[{}]数据发送成功", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                    }else {
                        e.printStackTrace();
                        logger.error("topic[{}],分区[{}]，offset[{}]数据发送失败：{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), e.getMessage());
                    }
                }
            });
        }
        // 关闭资源
        kafkaProducer.close();
    }

    /**
     *  带回调的kafka生产者异步发送
     */
    @Test
    public void testSynCallBackKafkaProducer() throws ExecutionException, InterruptedException {
        // 配置
        Properties properties = new Properties();
        // 连接kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 发送消息
        for (int i = 0; i < 5; i++) {
            RecordMetadata test = kafkaProducer.send(new ProducerRecord<>("test", "hello, world"), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // 发送成功
                    if (e == null) {
                        logger.info("topic[{}],分区[{}]，offset[{}]数据发送成功", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                    } else {
                        e.printStackTrace();
                        logger.error("topic[{}],分区[{}]，offset[{}]数据发送失败：{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), e.getMessage());
                    }
                }
            }).get();
        }
        // 关闭资源
        kafkaProducer.close();
    }

    /**
     *  生产者分区发送
     *  1.ProducerRecord如果设置了partition，就往指定的分区发送，从0开始
     *  2.如果未指定分区，指定了key，使用key的hashcode与分区数取余得到对应的分区数
     *  3.如果key和partition都未指定，则使用默认的Sticky Partition(黏性分区)，随机选择一个分区，并尽可能一直使用该分区，等batch满了或已完成再随机选择一个分区使用（和第一次选的分区不一样）
     */
    @Test
    public void testPartitionKafkaProducer() throws InterruptedException {
        // 配置
        Properties properties = new Properties();
        // 连接kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 发送消息
        for (int i = 0; i < 50; i++) {
            kafkaProducer.send(new ProducerRecord<>("test", "hello, world 1"),  new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // 发送成功
                    if (e == null) {
                        logger.info("topic[{}],分区[{}]，offset[{}]数据发送成功", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                    } else {
                        e.printStackTrace();
                        logger.error("topic[{}],分区[{}]，offset[{}]数据发送失败：{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), e.getMessage());
                    }
                }
            });
            Thread.sleep(2000);
        }
        // 关闭资源
        kafkaProducer.close();
    }

    /**
     *  自定义的分区发送
     * @throws InterruptedException
     */
    @Test
    public void testMyPartitionKafkaProducer() throws InterruptedException {
        // 配置
        Properties properties = new Properties();
        // 连接kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
        // 创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 发送消息
        for (int i = 0; i < 5; i++) {
            String value;
            if (i % 2 == 1){
                value = "lvboaa"+i;
            }else {
                value = "helloworld" + i;
            }
            kafkaProducer.send(new ProducerRecord<>("test", value),  new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // 发送成功
                    if (e == null) {
                        logger.info("topic[{}],分区[{}]，offset[{}]数据发送成功", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                    } else {
                        e.printStackTrace();
                        logger.error("topic[{}],分区[{}]，offset[{}]数据发送失败：{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), e.getMessage());
                    }
                }
            });
        }
        // 关闭资源
        kafkaProducer.close();
    }


    /**
     *  提高kafka生产环境效率的配置
     */
    @Test
    public void testOptimizeProdKafkaProducer() throws InterruptedException {
        // 配置
        Properties properties = new Properties();
        // 连接kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());

        // batch.size：批次大小，默认 16K
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16 * 1024);
        // linger.ms：等待时间，默认 0
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // RecordAccumulator：缓冲区大小，默认 32M：buffer.memory
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 32*1024*1024);
        // compression.type：压缩，默认 none，可配置值 gzip、snappy、lz4 和 zstd
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        // 创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 发送消息
        for (int i = 0; i < 5; i++) {
            String value;
            if (i % 2 == 1){
                value = "lvboaa" + i;
            }else {
                value = "helloworld" + i;
            }
            kafkaProducer.send(new ProducerRecord<>("test", value),  new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // 发送成功
                    if (e == null) {
                        logger.info("topic[{}],分区[{}]，offset[{}]数据发送成功", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                    } else {
                        e.printStackTrace();
                        logger.error("topic[{}],分区[{}]，offset[{}]数据发送失败：{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), e.getMessage());
                    }
                }
            });
        }
        // 关闭资源
        kafkaProducer.close();
    }
}
