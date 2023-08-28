echo mvn clean install
mvn clean install
echo find . -name *.tar.gz -print -exec cp {} ~/Downloads/STORIM/ \;
find . -name *.tar.gz -print -exec cp {} ~/Downloads/STORIM/ \;
echo find . -name *.zip -print -exec cp {} ~/Downloads/STORIM/ \;
find . -name *.zip -print -exec cp {} ~/Downloads/STORIM/ \;
echo scp /home/jehamming/Downloads/STORIM/*.tar.gz dockerserver:/tmp
scp /home/jehamming/Downloads/STORIM/*.tar.gz dockerserver:/tmp
