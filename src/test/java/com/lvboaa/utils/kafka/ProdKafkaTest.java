package com.lvboaa.utils.kafka;

import com.alibaba.fastjson.JSONObject;
import com.lvboaa.utils.BaseTest;
import com.lvboaa.utils.kafka.config.MyPartitioner;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Description:
 *
 * @author: lv.bo
 * @create: 2023-11-10 11:10
 */
public class ProdKafkaTest {

    protected static final Logger logger = LoggerFactory.getLogger(ProdKafkaTest.class);
    /**
     *  提高kafka生产环境效率的配置：提高生产者的吞吐量
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
            kafkaProducer.send(new ProducerRecord<>("test", "lvboaa"),  new Callback() {
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
     *  数据可靠性：ack机制
     *  acks:0,1(默认),-1（all）
     *  0：发消息不需要等待数据落盘应答，就可以发送下一条消息了（不可靠，可能会造成数据丢失，效率高）
     *  1：需要等待leader节点应答（不能完全保证可靠性，当leader应答后，还未同步到副本节点就挂了，就会造成数据丢失；效率中等，一般用于日志传输）
     *  -1：需要等待leader和ISR队列的所有节点应答（效率低，一般用于和钱相关的）
     *  （ISR队列：能够和leader保持同步的所有副本+leader；OSR:不能和leader保持同步的副本节点；AR:一个分区的所有节点(包括leader和副本节点)；AR=ISR+OSR）
     *
     * 问题：acks设置为-1,有一个副本因为故障，一直不能和leader同步，怎么解决呢
     *  如果Follower长时间未向Leader发送通信请求或同步数据，则该Follower将被踢出ISR。该时间阈值由 replica.lag.time.max.ms参
     * 数设定，默认30s。例如2超时，(leader:0, isr:0,1)。
     *
     * 数据完全可靠条件 = ACK级别设置为-1 + 分区副本大于等于2 + ISR里应答的最小副本数量大于等于2
     * 可能会发送多次，造成数据重复
     */
    @Test
    public void testProdACKSKafkaProducer() throws InterruptedException {
        // 配置
        Properties properties = new Properties();
        // 连接kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.put(ProducerConfig.ACKS_CONFIG, "-1");
        // 重试次数，默认为int的最大值
        properties.put(ProducerConfig.RETRIES_CONFIG, "3");

        // 创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        // 发送消息
        for (int i = 0; i < 5; i++) {
            kafkaProducer.send(new ProducerRecord<>("test", "lvboaa"),  new Callback() {
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
     *  数据去重：保证数据幂等性和开启事务(事务必须要保证幂等性是开启的)
     *  幂等性：不管数据发送了多少次，服务端都只会接收一次
     *  重复数据判断标准：有<PID, Partition, SeqNumber>相同主键的消息提交，broker只会持久化一次
     *      PID:kafka每次重启都会分配一个新的
     *      Partition：表示分区
     *      SeqNumber：是单调递增的
     *   所以幂等性只能保证单分区内是幂等的：开启参数：enable.idempotence（默认开启为true,false为关闭）
     *
     */
    @Test
    public void testProdIdempotenceKafkaProducer() throws InterruptedException {
        // 配置
        Properties properties = new Properties();
        // 连接kafka集群
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        // 指定key和value的序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.put(ProducerConfig.ACKS_CONFIG, "-1");
        // 重试次数，默认为int的最大值
        properties.put(ProducerConfig.RETRIES_CONFIG, "3");
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction_id_0");

        // 创建kafka生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        kafkaProducer.initTransactions();
        kafkaProducer.beginTransaction();
        // 发送消息
        try{
            for (int i = 0; i < 5; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("hello", "world");
                kafkaProducer.send(new ProducerRecord<>("test", jsonObject.toJSONString()));
            }
            int i = 1/0;
            kafkaProducer.commitTransaction();
        }catch (Exception e){
            logger.error("发送消息出错:" + e.getMessage(), e);
            kafkaProducer.abortTransaction();
        } finally {
            // 关闭资源
            kafkaProducer.close();
        }

    }

    /**
     *  数据有序：只能保证单分区内数据有序，多分区不能保证（保证全局有序，可以把消息发到一个分区去），一般不考虑
     *  kafka设计出来是更适用于高吞吐、流式、大数据的场景，对数据的有序性要求较为宽松，如需要强有序性，kafka可能不是最佳选择
     *  未开启幂等性：max.in.flight.requests.per.connection=1
     *  开启幂等性：max.in.flight.requests.per.connection=5  （kafka服务端会缓存producer发来的最近5个request的元数据，故无论如何，都可以保证最近5个request的数据都是有序的）
     */

}
