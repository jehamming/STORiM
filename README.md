# STORiM
STOry based Instant Messaging

STORIM is Inspired by LambdaMOO (rooms, things, verbs) and Whatsapp

A server architecture (and client(s)!) for a new way of cross platform communication: Whatsapp-like chat features combined with MOO features. 
With STORIM you can connect to a Server, walk through rooms (by text or visually (2D)) and chat with others using VERBS.

# Verb examples : 
verb>la This is fun!
result>You Laugh, "This is fun!"

verb>ex You can create your own verbs!
result>You explain, "You can create your own verbs!"

# The Server Architecture is designed to be scalable: 
There are multiple servers : 
- UserDataServer - This stores all the userdata : Users/Verbs/Tiles/Things 
- MicroServer - This Server servers a number of Rooms connected to eachother using Exits. Exits can also lead to a Room on another server. Multiple MicroServers together can server a whole world of interconnected Rooms in a scalable fashion. 

# Clients
Currently only a Java Based Client exist. I plan to make multiple clients: Android/Web (maybe even IOS?). 
Note: My core hobby is the Infrastructure, I suck at GUIs ;-) 

# Screenshots
See https://github.com/jehamming/STORiM/tree/develop/screenshots

# Getting started for Development
STORIM is build in Linux, in the language Java with Maven. 

Checkout the top folder and import the pom.xml in your editor. I use IntelliJ. 
If you do a 'mvn clean install' in the top folder, the complete STORIM will be build. 
Checkout the target folders of the different modules, for the userdataserver/microserver and client/java they should contain a subfolder with the deployable software. 

# Getting started to run STORIM
Either download a release or build a SNAPSHOT yourself

Using a Release
* Download the release
* Step 1 : UserDataServer 
- Check the userdataserver.propertes file
- Run the userdataserver by running the shellscript or bat file
* Step 2 : MicroServer
- Check the server.properties file: Change the dataserver details (ip + port) according to the output of the dataserver
- Run the microserver by running the shellscript or bat file
* Step 3 : The Client
- Run the client by executing the client shell script or bat file
- Enter the url of the Microserver
- Default there is only one user: root, password root 
- Go to the admin panel and enter the admin password : admin
- Now you can add/remove users 

Using a SNAPSHOT:
* checkout a SNAPSHOT. 
* run "mvn clean install" in the root of Storim, all artifacts will be build
* In the target folder of the userdataserver,microserver and client/java folders the runnable/deployable artifacts can be found. 
* Use the steps of Running a Release 



