node1<br>
node3<br>
node4<br>
node5<br>

关闭防火墙<br>
service iptable stop<br>

路径：/home/
tar -zxvf zookeeper-3.4.6.tar.gz<br>

cd zookeeper0-3.4.6/conf/<br>
vim zoo.cfg
```
tickTime=2000
dataDir=/opt/zookeeper <!-- zookeeper会自动创建此目录 -->
clientPort=2181
initLimit=5
syncLimit=2
server.1=node1:2888:3888
server.2=node3:2888:3888
server.3=node4:2888:3888
```
mkdir /opt/zookeeper<br>
cd /opt/zookeeper/<br>
vim myid<br>
1<br> <!-- 每台主机依次是1 2 3 -->

cd /home/zookeeper/<br>
scp -r zookeeper-3.4.6 root@node3:/home/<br>
scp -r zookeeper-3.4.6 root@node4:/home/<br>

启动zookeeper(在每个node上操作):<br>
cd bin<br>
./zkServer.sh start<br>

在当前目录下有zookeeper.out日志。<br>
zookeeper启动后，其内存数据库启动，通过zkCli.sh可以访问。<br>

