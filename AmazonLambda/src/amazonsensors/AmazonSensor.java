
package amazonsensors;

/**
 *
 * @author angel_banuelos
 */
public class AmazonSensor {
    
    
    private String sensorID;
    private String temperature;

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "{id:" + sensorID + ", temperature:" + temperature + "}";
    }
    
}
