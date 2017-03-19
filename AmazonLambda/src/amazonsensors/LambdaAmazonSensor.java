/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazonsensors;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author angel_banuelos
 */
public class LambdaAmazonSensor implements RequestHandler<AmazonSensor, String> {
    
    private final static String TABLE_NAME = "BitacoraLectura";
    private final static String SENSOR_FIELD = "sensorID";
    private final static String TEMPERATURE_FIELD = "temperature";

    @Override
    public String handleRequest(AmazonSensor sensor, Context cntxt) {
        
        AmazonDynamoDB amazonDDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2).build();
        
        Map<String, AttributeValue> item = new HashMap();
        item.put(SENSOR_FIELD, new AttributeValue(sensor.getSensorID()));
        item.put(TEMPERATURE_FIELD, new AttributeValue(sensor.getTemperature()));
        
        amazonDDB.putItem(TABLE_NAME,item);
        
        return sensor.toString();
    }
   
    
}
