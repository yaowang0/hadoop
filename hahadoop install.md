Hadoop2.5 HA搭建<br>
四台机器：hadoop1，hadoop2，hadoop3，hadoop4<br>

Hadoop1	 NN(Y)          ZK(Y)   ZKFC(Y) <br>
Hadoop2	 NN(Y)	DN(Y)	  ZK(Y)	  ZKFC(Y)	  JN(Y)	  RM(Y)	  NM（任务管理）(Y)<br>
Hadoop3		      DN(Y)		ZK(Y)		          JN(Y)           NM（任务管理）(Y)<br>
Hadoop4		      DN(Y)		                  JN(Y)           NM（任务管理）(Y)<br>

1.core-site.xml <br>
```
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://bjsxt</value>
  </property>
  <property>
     <name>ha.zookeeper.quorum</name>
     <value>hadoop1:2181,hadoop2:2181,hadoop3:2181</value>
  </property>
  <property>
    <name>hadoop.tmp.dir</name>
    <value>/opt/hadoop</value>
  </property>
</configuration>
```

2.hdfs-site.xml
```
<configuration>
  <property>
    <name>dfs.nameservices</name>
    <value>bjsxt</value>
  </property>
  <property>
    <name>dfs.ha.namenodes.bjsxt</name>
    <value>nn1,nn2</value>
  </property>
  <property>
    <name>dfs.namenode.rpc-address.bjsxt.nn1</name>
    <value>hadoop1:8020</value>
  </property>
  <property>
    <name>dfs.namenode.rpc-address.bjsxt.nn2</name>
    <value>hadoop2:8020</value>
  </property>
  <property>
    <name>dfs.namenode.http-address.bjsxt.nn1</name>
    <value>hadoop1:50070</value>
  </property>
  <property>
    <name>dfs.namenode.http-address.bjsxt.nn2</name>
    <value>hadoop2:50070</value>
  </property>
  <property>
    <name>dfs.namenode.shared.edits.dir</name>
    <value>qjournal://hadoop2:8485;hadoop3:8485;hadoop4:8485/bjsxt</value
  >
  </property>
  <property>
    <name>dfs.client.failover.proxy.provider.bjsxt</name>
    <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
  </property>
  <property>
    <name>dfs.ha.fencing.methods</name>
    <value>sshfence</value>
  </property>
  <property>
    <name>dfs.ha.fencing.ssh.private-key-files</name>
    <value>/root/.ssh/id_dsa</value>
  </property>
  <property>
    <name>dfs.journalnode.edits.dir</name>
    <value>/opt/hadoop/data</value>
  </property>
  <property>
     <name>dfs.ha.automatic-failover.enabled</name>
     <value>true</value>
  </property>
</configuration>
```
3. 准备zookeeper<br>
a)三台zookeeper：hadoop1，hadoop2，hadoop3<br>
b)编辑zoo.cfg配置文件<br>
```
dataDir=/opt/zookeeper
server.1=hadoop1:2888:3888
server.2=hadoop2:2888:3888
server.3=hadoop3:2888:3888
```
c)在dataDir目录中创建一个myid的文件，文件内容分别为1，2，3<br>

4.配置hadoop中的slaves<br>

5.启动三个zookeeper：./zkServer.sh start<br>

6.启动三个JournalNode：./hadoop-daemon.sh start journalnode<br>

7.在其中一个namenode上格式化：hdfs namenode -format<br>

8.把刚刚格式化之后的元数据拷贝到另外一个namenode上<br>
a)启动刚刚格式化的namenode   hadoop-daemon.sh start namenode<br>
b)在没有格式化的namenode上执行：hdfs namenode -bootstrapStandby<br>
c)启动第二个namenode<br>

9.在其中一个namenode上初始化zkfc：hdfs zkfc -formatZK<br>

10.停止上面节点：stop-dfs.sh<br>

11.全面启动：start-dfs.sh<br>
