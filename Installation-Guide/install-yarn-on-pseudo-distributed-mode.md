# Step 1: Download and install JAVA 
You can verify java installation using the following command 
```
java –version 
```
On executing this command, you should see output similar to the following: 
```
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
```

If java is not installed, Use the below command to begin the installation of Java 
Java8
```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
sudo apt-get install oracle-java8-set-default
java -version
```
This will install the full JDK under **/usr/lib/jvm/java-8-subdirectory**. (For Hadoop 2, the recommended version of Java can be found at [HadoopJavaVersions](http://wiki.apache.org/hadoop/HadoopJavaVersionsi) )



# Step 2: Download Hadoop
- Download the latest distribution from the [Hadoop website](http://hadoop.apache.org/) 
  ```
  cd /opt wget http://apache.mirrors.spacedump.net/hadoop/common/stable/hadoop-2.7.2.tar.gz
  ```
- Next create and extract the package in /opt/yarn: 
  ```
  mkdir /opt/yarn
  cd /opt/yarn 
  tar xvzf /opt/hadoop-2.7.2.tar.gz
  ```
	
# Step 3: SSH Configuration
- Install SSH using below command
  ```
  sudo apt–get install openssh-server
  ```
- Check is installed or not 
  ```
  $ssh localhost 
  If not installed – Error msg:  ssh: connect to host localhost port 22: Connection refused.
  ```
- Setup SSH
  ```
  $ ssh-keygen -t rsa 
  $ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys 
  $ chmod 0600 ~/.ssh/authorized_keys 
  ```
# Step 4: Set Classpath 
- Set Classpath for JAVA, run below command or add below statement in .bashrc to set classpath for JAVA
  ```
  export JAVA_HOME=/usr/lib/jvm/java-6-sun
  ```
- Set Classpath for HADOOP YARN, add below statement in .bashrc
  ```
  export HADOOP_HOME="/opt/yarn/hadoop-2.7.2" # Change this to where you unpacked hadoop to.
  ```
- Other applications building on top of Hadoop might expect other environment variables. So need to add below also:
  ```
  export HADOOP_YARN_HOME=$HADOOP_HOME 
  export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$HADOOP_HOME/lib/native 
  export PATH=$PATH:$HADOOP_HOME/bin:HADOOP_HOME/sbin
  export HADOOP_MAPRED_HOME=$HADOOP_HOME 
  export HADOOP_COMMON_HOME=$HADOOP_HOME 
  export HADOOP_HDFS_HOME=$HADOOP_HOME 
  export YARN_HOME=$HADOOP_HOME 
  export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop 
  export YARN_CON_DIR=$HADOOP_HOME/etc/hadoop
  ```  	
# Step 5: HDFS Configuration  
- Configure core-site.xml 
  ```
	cd $HADOOP_HOME/etc/hadoop 
	vi core-site.xml 
	<configuration>      
		<property>           
			<name>fs.defaultFS</name>           
			<value>hdfs://localhost:9000</value>      
		</property> 
	</configuration> 
  ```	
- Configure hdfs-site.xml 
  ```
	cd $HADOOP_HOME/etc/hadoop 
	vi hdfs-site.xml 
	<configuration>      
		<property>           
			<name>dfs.replication</name> 
			<value>1</value>      
		</property> 
		<property>         
			<name>dfs.datanode.data.dir</name>
			<value>file:///opt/yarn/hadoop-2.7.2/hdfs/dn</value>			
		</property>
		<property>         
			<name>dfs.namenode.name.dir</name>         
			<value>file:///opt/yarn/hadoop-2.7.2/hdfs/nn</value>    
		</property>
		<property>
			<name>fs.checkpoint.dir</name>
			<value>file:/opt/yarn/hadoop-2.7.2/hdfs/snn</value>			
		</property>
		<property>           
			<name>fs.checkpoint.edits.dir</name>
			<value>file:/opt/yarn/hadoop-2.7.2/hdfs/snn</value>
		</property> 
	</configuration>
  ```	
# Step 6: YARN Configuration 
- To  configure  YARN,  the  relevant  file  is $HADOOP_HOME/etc/hadoop/yarn-site.xml.For a single-node installation of YARN you'll want to add the following to that file:
  ```
  <configuration>      
  	<property>           
  		<name>yarn.nodemanager.aux-services</name>           
  		<value>mapreduce_shuffle</value>      
  	</property>      
  	<property>           
  		<name>yarn.nodemanager.aux-services.mapreduce.shuffle.class</name>
  		<value>org.apache.hadoop.mapred.ShuffleHandler</value>
  	</property> 
  </configuration>
  ```
# Step 7: Modify Java Heap Sizes 
- Edit etc/hadoop/hadoop-env.sh file to reflect the following # (Don't forget to remove the "#" at the beginning od the line.): 
  ```
	HADOOP_HEAPSIZE=500 
	HADOOP_NAMENODE_INIT_HEAPSIZE="500" 
  ```
- Edit the mapred-env.sh to reflect the following: 
  ```
  HADOOP_JOB_HISTORYSERVER_HEAPSIZE=250
  ```
- Edit yarn-env.sh to reflect the following: 
  ```
	JAVA_HEAP_MAX=-Xmx500m 
	YARN_HEAPSIZE=500
  ```	
# Step 8: Format HDFS 
- Format the namenode directory (DO THIS ONLY ONCE, THE FIRST TIME) 
  ```
  $HADOOP_HOME/bin/hdfs namenode -format
  ```
- IF the command worked, you should see the following near the end of a long list of messages: 
  ```
  INFO  common.Storage:  Storage  directory  /opt/yarn/hadoop-2.7.2/hdfs/nn has been successully formatted.
  ```
	
# Step 9: Start the Services 
#### Start HDFS daemons 
- Start the namenode daemon 
` $HADOOP_HOME/sbin/hadoop-daemon.sh start namenode `
- Start the datanode daemon 
` $HADOOP_HOME/sbin/hadoop-daemon.sh start datanode `
- Start the secondary namenode daemon 
` $HADOOP_HOME/sbin/hadoop-daemon.sh start secondarynamenode `
- Start all HDFS services cluster-wide
` $HADOOP_HOME/sbin/start-dfs.sh `

#### Start YARN daemons
- Start the resourcemanager daemon 
` $HADOOP_HOME/sbin/yarn-daemon.sh start resourcemanager `
- Start the nodemanager daemon 
` $HADOOP_HOME/sbin/yarn-daemon.sh start nodemanager `
- Start all YARN services clluster-wide 
` $HADOOP_HOME/sbin/start-yarn.sh `
	
#### Check the Services are running 
- Issue  jps  command  to  see  that  all  the  services are running.
  $jps

# Step 10: Stop Services 
- All  Hadoop  services  can  be  stopped  using  the  hadoop-daemon.sh script.
```
	For example, to stop the datanode service enter the following
		./hadoop-daemon.sh stop datanode      
		./yarn-daemon.sh stop nodemanager
```		
# Step 11: Verify the Running Services Using the Web Interface
- HDFS
` http://localhost:5007 `
- ResourceManager (YARN) 
` firefox http://localhost:8088 `
	
# Step 11: Configure MapReduce
- Add the following to the end of yarn-site.xml: 
```
	<property>         
		<name>yarn.nodemanager.aux-services</name>   
		<value>mapreduce_shuffle</value>       
		<description>shuffle  service  that  needs  to be  set  for  Map Reduce to run </description>
	</property> 
```
With this configuration, you should already be able to run MapReduce jobs. 
- To test your installation, run the sample "pi"
` yarn jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.2.0.jar pi 16 1000 `

	


