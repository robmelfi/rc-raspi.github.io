### Create database

create user 'rcraspi'@'%' identified by 'rcraspi';  
create database rcraspi;
GRANT ALL PRIVILEGES ON rcraspi.* TO 'rcraspi'@'%';


### Create package for production
./mvnw -Pprod package -DskipTests

Copy on raspberry:

scp target/rcraspi-0.0.1-SNAPSHOT.war pi@192.168.10.202:/home/pi/rcraspi

sudo ./rcraspi-0.0.1-SNAPSHOT.war


### Liquibase
liquibase in dev
./mvnw liquibase:diff
