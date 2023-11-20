# 连vpn了连不上git
```md
git config --global http.proxy http://127.0.0.1:10809
git config --global http.proxy http://127.0.0.1:10809
```

## windows下kafka命令

```md
# 启动zookeeper命令
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
# 启动kafka命令
bin\windows\kafka-server-start.bat config\server.properties
# 创建topic命令:设置副本数为1，表示直接一个副本(leader本身)
bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic test
# 查看所有topic的命令
bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list
# 开启消费者的命令
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test
# 查看topic的详细信息
bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --topic test --describe

```