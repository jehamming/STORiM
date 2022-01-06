cd common/ && mvn clean install && cd ..
cd client/java/ && mvn clean install && cd ../../
cd servercommon/ && mvn clean install && cd ..
cd loginserver/ && mvn clean install && cd ..
cd userdataserver/ && mvn clean install && cd ..
cd microserver/ && mvn clean install && cd ..

#cp common/target/common-1.0.0-SNAPSHOT.jar ~/AndroidStudioProjects/StorimApp/app/libs/common-1.0.0-SNAPSHOT.jar
