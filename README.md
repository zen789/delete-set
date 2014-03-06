#Delete Set Data

A Set is part of the Aerospike [data model](https://docs.aerospike.com/display/V3/Data+Model). It is similar to a table in a relational database, but more like a [mathematical set](http://en.wikipedia.org/wiki/Set_(mathematics)). More often than not, you will store your data in a set.

During development, you will want to load data into a Set, test your application, and restore the database back to the starting point of your test. Often you will want to “drop the table” or, in Aerospike terms, delete the data from a Set.

##Problem
There is no documented way to delete the data from a Set. You could create a “save point” by using [asbackup](https://docs.aerospike.com/pages/viewpage.action?pageId=3807608) to backup your database prior to the test run, then use [asrestore](https://docs.aerospike.com/pages/viewpage.action?pageId=3807609) to restore your database to the initial state. But this could be a painful and officious process, plus having side effects on other developers using the Aerospike cluster.
##Solution
A utility that deletes the data in a Set written in Java so it can run anywhere java can run. It uses the Scan ([Scan Namespace](https://docs.aerospike.com/display/V3/Key-Value+Store)) API to scan through a Set and delete each record.

The source code for this solution is available on GitHub, at 
https://github.com/aerospike/delete-set 

The utility, names as-delete-set, requires the Aerospike Java client, which will be downloaded from Maven Central as part of the build.

##Build instructions
Maven is required to build as-delete-set. From the root directory of the project, issue the following command:
```
mvn clean package
```	
Two JAR files will be produced in the directory 'target', these are:
* as-delete-set-<version>-jar-with-dependencies.jar - this is a runnable jar complete with all the dependencies packaged.
* as-delete-set-<version>.jar - this is runnable jar, but it will expect to locate it's dependencies via maven.

##Run as-delete-set
While this utility can run on any machine, it should be run on machine that has excellent network bandwidth to the cluster.

The JAR files are runnable JARS. Here is an example command to delete the data in the Set 'demo':
```
java -jar as-delete-set-1.0.0-jar-with-dependencies.jar -h p3 -p 3000 -s demo
```
or
```
java -jar as-delete-set-1.0.0.jar -h p3 -p 3000 -s demo
```
##Options
These are the options:
```
-h,--host <arg>  Server hostname (default: localhost)
-p,--port <arg>  Server port (default: 3000)
-s,--set  <arg>  Set to delete (default: test)
-u,--usage       Print usage.
```


