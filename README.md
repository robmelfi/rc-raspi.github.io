# rcraspi
Controlling Raspberry GPIO for home automation with a web/mobile app.

[![](http://img.youtube.com/vi/EWZKmvhpyBQ/0.jpg)](http://www.youtube.com/watch?v=EWZKmvhpyBQ "rcraspi")

This application was generated using JHipster 5.7.0, you can find documentation and help at [https://www.jhipster.tech](https://www.jhipster.tech).

## About

This software is intended to run on raspberry pi to control GPIO. It provides an 
interface for controlling the GPIO remotely via the network. The GPIO can be controlled via a device 
like smartphone or web browser.  
The software was designed with the following requirements in mind:
- simple, intuitive usage and interface
- lightweight, can run on simple devices

### Features

- Add a contoller for GPIO pin
- Select mode (for this version only OUTPUT)
- Select initial state (High or Low)
- Control GPIO pin remotely

### Nice to have (wishlist)

- Programmable timers for GPIO actions
- Select INPUT mode, to connect sensors like humidity and temperature sensor
- Add preset configuration for many types of sensors

<video src="video.mp4" width="320" height="200" controls preload></video>

## Development

To start your application in the dev profile, simply run:

    ./mvnw    


For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].



## Building for production

Create database on raspberry pi (mysql)

    create user 'rcraspi'@'%' identified by 'rcraspi';  
    create database rcraspi;
    GRANT ALL PRIVILEGES ON rcraspi.* TO 'rcraspi'@'%';

Building for production

    ./mvnw -Pprod package -DskipTests

Copy the package on raspberry pi

    scp target/rcraspi-1.0.war pi@piaddress:/home/pi/rcraspi

To ensure everything worked, run:

    sudo ./rcraspi-1.0.war

Refer to [Using JHipster in production][] for more details.

## Testing

To launch your application's tests, run:

    ./mvnw test

For more information, refer to the [Running tests page][].

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[JHipster 5.7.0 archive]: https://www.jhipster.tech/documentation-archive/v5.7.0

[Using JHipster in development]: https://www.jhipster.tech/development/
[Using Docker and Docker-Compose]: https://www.jhipster.tech/docker-compose
[Using JHipster in production]: https://www.jhipster.tech/production/
[Running tests page]: https://www.jhipster.tech/running-tests/


