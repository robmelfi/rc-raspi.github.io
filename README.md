# rcraspi
Controlling Raspberry GPIO for home automation with a web/mobile app.

**Click [here](https://www.youtube.com/watch?v=b8VcnN9qe1E) or on the image below to view youtube video.**

[![](http://img.youtube.com/vi/b8VcnN9qe1E/0.jpg)](https://www.youtube.com/watch?v=b8VcnN9qe1E "rcraspi")

This application was generated using JHipster 5.7.2, you can find documentation and help at [JHipster Homepage and latest documentation][].

## About

This software is intended to run on raspberry pi to control GPIO. It provides an 
interface for controlling the GPIO remotely via the network. The GPIO can be controlled via a device 
like smartphone or web browser.  
The software was designed with the following requirements in mind:
- simple, intuitive usage and interface
- lightweight, can run on simple devices

## Release notes

### Release 1.3
- Programmable timers for GPIO output actions

### Release 1.1
- Add preset configuration for DHT11 sensor (Humidity Temperature sensor)
- Auto select INPUT mode when sensor DHT11 is added

### Release 1.0

- Add a contoller for GPIO pin
- Select mode (for this version only OUTPUT)
- Select initial state (High or Low)
- Control GPIO pin remotely

## Nice to have (wish list)

- Push refresh when timer change GPIO status (in v1.3 the status is update with pull request every minute)
- Add preset configuration for other many types of sensors

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1.  [Node.js][]: We use Node to run a development web server and build the project.
    Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    npm install

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    npm start

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### Service workers

Service workers are commented by default, to enable them please uncomment the following code.

- The service worker registering script in index.html

```html
<script>
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker
        .register('./service-worker.js')
        .then(function() { console.log('Service Worker Registered'); });
    }
</script>
```

Note: workbox creates the respective service worker and dynamically generate the `service-worker.js`

## Building for production

create database on raspberry pi (mysql)

    create user 'rcraspi'@'%' identified by 'rcraspi';  
    create database rcraspi;
    GRANT ALL PRIVILEGES ON rcraspi.* TO 'rcraspi'@'%';

To optimize the rcraspi application for production, run:

    ./mvnw -Pprod package -DskipTests

Copy the package on raspberry pi

    scp target/rcraspi-version.war pi@piaddress:/home/pi/rcraspi

To ensure everything worked, run:

    sudo ./rcraspi-version.war

Then navigate to http://piaddress:8081 in your browser.

Refer to [Using JHipster in production][] for more details.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    npm test

For more information, refer to the [Running tests page][].

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
[Using JHipster in production]: https://www.jhipster.tech/production/
[Running tests page]: https://www.jhipster.tech/running-tests/
[node.js]: https://nodejs.org/
[webpack]: https://webpack.github.io/
