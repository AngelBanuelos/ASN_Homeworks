var iot = require('aws-iot-device-sdk');
var device = iot.device({
    keyPath: __dirname + '/TempSensor.private.key',
    certPath: __dirname + '/TempSensor.cert.pem',
    caPath: __dirname + '/root-CA.crt',
    clientID: 'Prueba',
    region: 'us-west-2'
});

device.on('connect', function () {
    console.log('connect');
    device.subscribe('AmazonSensorThing', function (error, result) {
        console.log(result);
    })
});

device.on('close', function () {
    console.log('close');
});

device.on('reconnect', function () {
    console.log('reconnect');
});

device.on('offline', function () {
    console.log('offline');
});

device.on('message', function (topic, payload) {
    console.log('message', topic, payload.toString());
});

