# STORiM
STOry based Instant Messaging

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

See also https://github.com/jehamming/STORiM/blob/develop/STORIM-Architecture.jpg 

# Clients
Currently only a Java Based Client exist. I plan to make multiple clients: Android/Web (maybe even IOS?). 
Note: My core hobby is the Infrastructure, I suck at GUIs ;-) 

# Screenshots
See https://github.com/jehamming/STORiM/blob/develop/screenshots/Screen002.png 


STORIM is Inspired by LambdaMOO (rooms, things, verbs) and Whatsapp


