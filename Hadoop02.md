Hadoop三个系列：
0.23 hadoop团队用来开发测试
1.x
2.x

查看Hadoop/HBase/jdk对应的版本：
https://hbase.apache.org/book.html#basic.prerequisites

一、ssh免密码登录<br>
ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa<br>
cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys<br>

免密码登录的原理<br>
需求：node1免密码登录到node3<br>
过程：<br>
1.node1将node1的ip地址和node1的id_dsa.pub发送给node3<br>
2.node3收到请求，将node3的authorized_keys中的公钥比对，若一致，则node3将密码发送给node1<br>
3.node1将node3发送来的密码，发送给node3，取得连接<br>

二、安装（节点名字node1,node3,node4,node5）<br>
在node1上：安装路径在/home/<br>
tar -zxvf Hadoop-2.5.1_x64.tar.gz<br>

cd Hadoop-2.5.1/etc/hodoop<br>

vim hadoop-env.sh<br>
```
export JAVA_HOME=/usr/java/jdk1.7.0_79
```

vim core-site.xml
```
<configuration>
    <property>
        <name>fs.defaultFS</name>          <!-- NameNode所在的主机和端口-->
        <value>hdfs://node1:9000</value>     <!-- RPC协议-->
</property>
<property>
    <name>hadoop.tmp.dir</name>   
    <value>/opt/hadoop-2.5</value>
</property>
</configuration>

vim hdfs-site.xml
<configuration>
    <property>
        <name>dfs.namenode.secondary.http-address</name>         
        <value>node3:50090</value>   
</property>
    <property>
        <name>dfs.namenode.secondary.https-address</name>         
        <value>node3:50091</value>   
</property>
</configuration>
```

vim slaves<br>
node3<br>
node4<br>
node5<br>

vim masters<br>
node3   <!-- SecondaryNameNode的主机名 --><br>

将所有文件拷贝到各个节点：<br>
scp -r Hadoop-2.5.1/ root@node3:/home/<br>
scp -r Hadoop-2.5.1/ root@node4:/home/<br>
scp -r Hadoop-2.5.1/ root@node5:/home/<br>

配置环境变量：
vim /etc/profile
```
export HADOOP_HOME=/home/Hadoop-2.5.1
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
```

source /etc/profile<br>

三、格式化HDFS文件系统(在NameNode上执行(此处是node1))<br>
hdfs namenode –format<br>

四、启动(在node1上启动，因为在node1上启动了ssh免密码登录)<br>
start-dfs.sh<br>

五、UI监控(NN和SNN)<br>
node1:50070<br>
node3:50090<br>

