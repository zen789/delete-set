#Aerospike delete set

This is a simple utility that delets all the records in a Set in an Aerospike cluster.

This utility uses the Scan operation to retrive each record digest and the Delete operation to remove the record.

##Build
This project is build using Maven. TO build a runnable JAR use the following command:

  mvn clean package

##Running the utility
While this utility can run on any machine, it should be run on machine that has excellent network bandwidth to the cluster

To run:

  java -jar delete-set-<version>.jar
  
or

  java -jar delete-set-<version-jar-with-dependencies.jar

Options:
-h,--host <arg>       Server hostname (default: localhost)
-n,--namespace <arg>  Namespace (default: test)
-p,--port <arg>       Server port (default: 3000)
-s,--set <arg>        Set to delete (default: test)
-u,--usage            Print usage.


