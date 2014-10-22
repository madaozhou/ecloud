#!/bin/sh
ls -al
mvn install:install-file -DgroupId=httpclient -DartifactId=httpclient -Dversion=4.3.5 -Dpackaging=jar -Dfile=/lib/httpclient-4.3.5.jar
mvn install:install-file -DgroupId=httpcore -DartifactId=httpcore -Dversion=4.3.2 -Dpackaging=jar -Dfile=/lib/httpcore-4.3.2.jar
mvn install:install-file -DgroupId=javax.json -DartifactId=javax.json -Dversion=1.0.4 -Dpackaging=jar -Dfile=/lib/javax.json-1.0.4.jar
mvn install:install-file -DgroupId=jline -DartifactId=jline -Dversion=2.13-SNAPSHOT -Dpackaging=jar -Dfile=/lib/jline-2.13-SNAPSHOT.jar
