HA完全分布式安装：<br>

干掉/home/hadoop-2.5.1/etc/hadoop路径下的masters文件，这个文件主要是用作SecondaryName.<br>
rm -rf /home/hadoop-2.5.1/etc/hadoop/masters<br>

干掉原来非HA的Hadoop存放文件的目录<br>
rm -rf /opt/hadoop-2.5<br>

干掉hdfs-site.xml中原来非HA的配置信息。<br>
vim hdfs-site.xml
```
<configuration>
	<property>
  		<name>dfs.nameservices</name>
  		<value>mycluster</value>
	</property>
	<property>
  		<name>dfs.ha.namenodes.mycluster</name>
  		<value>nn1,nn2</value>
	</property>
	<property>
  		<name>dfs.namenode.rpc-address.mycluster.nn1</name>
 		<value>node1:8020</value>
	</property>
	<property>
  		<name>dfs.namenode.rpc-address.mycluster.nn2</name>
  		<value>node5:8020</value>
	</property>
	<property>
  		<name>dfs.namenode.http-address.mycluster.nn1</name>
  		<value>node1:50070</value>
	</property>
	<property>
  		<name>dfs.namenode.http-address.mycluster.nn2</name>
  		<value>node5:50070</value>
	</property>
	<property>
  		<name>dfs.namenode.shared.edits.dir</name>
  		<value>qjournal://node3:8485;node4:8485;node5:8485/mycluster</value>
	</property>
	<property>
  		<name>dfs.client.failover.proxy.provider.mycluster</name>
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
	  	<value>/opt/journalnode/</value>
	</property>
	<property>
   		<name>dfs.ha.automatic-failover.enabled</name>
   		<value>true</value>
 	</property>
</configuration>
```
vim core-site.xml
```
<configuration>
    <property>
        <name>fs.defaultFS</name>          <!-- NameNode所在的主机和端口-->
        <value>hdfs://mycluster</value>    <!-- RPC协议-->
    </property>
    <property>
        <name>hadoop.tmp.dir</name>   
        <value>/opt/hadoop-2.5</value>
    </property>
    <property>
   		<name>ha.zookeeper.quorum</name>
   		<value>node1:2181,node3:2181,zk3.node4:2181</value>
 	  </property>
</configuration>
```
DataNode的主机名:<br>
vim slaves<br>
node3<br>
node4<br>
node5<br>

拷贝配置文件：<br>
scp ./* root@node3:/home/hadoop-2.5.1/etc/hadoop/<br>
scp ./* root@node4:/home/hadoop-2.5.1/etc/hadoop/<br>
scp ./* root@node5:/home/hadoop-2.5.1/etc/hadoop/<br>

在node3,node4,node5三台主机上，分别启动journalNode（我选择的是node3,node4,node5为journalNode主机）：<br>
hadoop-daemon.sh start journalnode<br>
停止命令为：<br>
hadoop-daemon.sh stop journalnode<br>

随便找一个一台NameNode(假设在node5上，我是将node1和node5作为NameNode)，进行格式化：<br>
hdfs namenode -format

将/opt/hadoop-2.5/整个目录拷贝到另一个NameNode上去，这个目录包含了初始化的fsimage等文件<br>
(在node1上执行，这个命令将node5的文件拷贝到node1上来)<br>
scp -r root@node5:/opt/hadoop-2.5 /opt<br>

初始化HA，Zookeeper<br>
在任一台NameNode主机上：<br>
hdfs zkfc -formatZK<br>

启动（在免密码登录的主机上，执行）:<br>
satrt-dfs.sh<br>
它会执行：<br>
(1)start namenodes(node1,node5) datanodes(node3,node4,node5)<br>
(2)start journal nodes(node3,node4,node5)<br>
(3)start ZK Failover Controllers(node1, node5)<br>
在单台主机上启动DataNode的命令：<br>
hadoop-daemon.sh start datanode<br>

在不同的主机上jps，可能会出现以下节点信息:<br>
JournalNode<br>
DataNode<br>
NameNode<br>
QuorunPeerMain<br>

shell命令示例：<br>
hdfs dfs -mkdir /test<br>
hdfs haadmin(高可用的管理)<br>

IDEA连接：
将配置文件拷贝到项目的src路径下即可。