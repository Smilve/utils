package com.lvboaa.utils.kafka.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * Description:
 * 1. 实现接口 Partitioner
 * 2. 实现 3 个方法:partition,close,configure
 * 3. 编写 partition 方法,返回分区号
 *
 * @author: lv.bo
 * @create: 2023-11-10 10:24
 */
public class MyPartitioner implements Partitioner {

    /**
     * 返回信息对应的分区号
     * @param topic 主题
     * @param key 消息的 key
     * @param keyBytes 消息的 key 序列化后的字节数组
     * @param value 消息的 value
     * @param valueBytes 消息的 value 序列化后的字节数组
     * @param cluster 集群元数据可以查看分区信息
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String finalValue = value.toString();
        int partition = 0;
        if (finalValue.contains("lvboaa")){
            partition = 1;
        }
        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
