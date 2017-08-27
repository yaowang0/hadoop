cd etc/hadoop/<br>

vim yarn-site.xml
```
<configuration>
	<property>
	   <name>yarn.resourcemanager.ha.enabled</name>
	   <value>true</value>
	</property>
	<property>
	   <name>yarn.resourcemanager.cluster-id</name>
	   <value>cluster1</value>
	</property>
	<property>
	   <name>yarn.resourcemanager.ha.rm-ids</name>
	   <value>rm1,rm2</value>
	</property>
	<property>
	   <name>yarn.resourcemanager.hostname.rm1</name>
	   <value>node1</value>
	</property>
	<property>
	   <name>yarn.resourcemanager.hostname.rm2</name>
	   <value>node5</value>
	</property>
	<property>
	   <name>yarn.resourcemanager.zk-address</name>
	   <value>node1:2181,node3:2181,node4:2181</value>
	</property>
	<property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>
```

vim mapred-site.xml
```
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
```
scp ./* root@node3:/home/hadoop-2.5.1/etc/hadoop/<br>
scp ./* root@node4:/home/hadoop-2.5.1/etc/hadoop/<br>
scp ./* root@node5:/home/hadoop-2.5.1/etc/hadoop/<br>

启动(在node1上，免密码登录)：<br>
start-yarn.sh<br>
它会启动：<br>
resourcemanager (它没有启动备用的resourcemanager)<br> 
node3:nodemanager<br>
node4:nodemanager<br>
node5:nodemanager<br>

备用的resourcemanager启动：<br>
yarn-daemon.sh start resourcemanager<br>

jps:可以看到<br>
ResourceManager<br>

UI:<br>
node1:8088<br>
