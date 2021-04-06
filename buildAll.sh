cd client/java/ && mvn clean install && cd ../../
cd common/ && mvn clean install && cd ..
cd server/ && mvn clean install && cd ..

cp common/target/common-1.0.0-SNAPSHOT.jar ~/AndroidStudioProjects/StorimApp/app/libs/common-1.0.0-SNAPSHOT.jar
