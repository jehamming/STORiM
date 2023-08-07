#!/bin/sh
cd common/ && mvn clean && cd ..
cd client/java/ && mvn clean && cd ../../
cd servercommon/ && mvn clean && cd ..
cd loginserver/ && mvn clean && cd ..
cd userdataserver/ && mvn clean && cd ..
cd microserver/ && mvn clean && cd ..

#cp common/target/common-1.0.0-SNAPSHOT.jar ~/AndroidStudioProjects/StorimApp/app/libs/common-1.0.0-SNAPSHOT.jar
