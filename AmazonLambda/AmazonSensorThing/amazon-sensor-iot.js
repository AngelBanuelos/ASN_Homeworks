var iot = require('aws-iot-device-sdk');
var minTemp = 0;
var maxTemp = 50;

var minSensorID = 1;
var maxSensorID = 100;


var device = iot.device({
    keyPath: __dirname + '/TempSensor.private.key',
    certPath: __dirname + '/TempSensor.cert.pem',
    caPath: __dirname + '/root-CA.crt',
    clientID: 'Prueba',
    region: 'us-west-2'
});

device.on('connect', function () {
    console.log('connect');
    device.subscribe('testIN', function (error, result) {
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


function sense(sensorID) {
    console.log('Sending Temperature');
    device.publish('AmazonSensorThing', JSON.stringify(
            {sensorID: sensorID, temperature: getTemp()}));
}

function getTemp() {
    return "" + (Math.floor(Math.random() * maxTemp) + minTemp);
}

function getSensorID() {
    return "Sen-" + (Math.floor(Math.random() * maxSensorID) + minSensorID);
}

function start() {
    setInterval(function (){ sense(getSensorID()) }, 5000);
}


start();



