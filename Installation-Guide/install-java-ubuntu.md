# Download and install JAVA 
1. Add the repository where the java installer scripts are maintained. 
```
sudo add-apt-repository ppa:webupd8team/java
```

2. Update the system package index.
```	
sudo apt-get update
```

3. Install Java
```	
sudo apt install oracle-java8-installer
```	

4. Set Java environment variable.
```
sudo apt install oracle-java8-set-default
```

5. Check is Java installed ?
```
java –version 
```
On executing this command, you should see output similar to the following: 
```
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
```

6. To Know the location where the Java is installed by running command
```
'locate java' or 'which java'
This will install the full JDK under **/usr/lib/jvm/java-8-subdirectory**. 
```

