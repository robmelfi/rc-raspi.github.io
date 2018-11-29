./mvnw -Pprod package -DskipTests

scp target/rcraspi-0.0.1-SNAPSHOT.war pi@192.168.10.202:/home/pi/rcraspi



liquibase in dev
./mvnw liquibase:diff
