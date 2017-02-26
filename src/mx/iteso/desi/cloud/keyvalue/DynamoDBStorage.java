package mx.iteso.desi.cloud.keyvalue;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import mx.iteso.desi.cloud.hw1.Config;

public class DynamoDBStorage extends BasicKeyValueStore {

    AmazonDynamoDB client = null;

    DynamoDB dynamoDB = null;
    Table table = null;
    String dbName;

    // Simple autoincrement counter to make sure we have unique entries
    int inx;

    Set<String> attributesToGet = new HashSet<String>();

    public DynamoDBStorage(String dbName) {
        this.dbName = dbName;
        init();
    }

    @Override
    public Set<String> get(String search) {
        if (table == null) {
            System.err.println("Table doesn't exists");
            return null;
        }
        HashSet<String> all = new HashSet<>();
        ItemCollection<QueryOutcome> il = table.query("keyword", search);
        for (Item item : il) {
            all.add(item.getString("value"));
        }
        return all;
    }

    @Override
    public boolean exists(String search) {
        if (table == null) {
            System.err.println("Table doesn't exists");
            return false;
        }
        ItemCollection<QueryOutcome> il = table.query("keyword", search);
        for (Item item : il) {
            return true;
        }

        return false;
    }

    @Override
    public Set<String> getPrefix(String search) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addToSet(String keyword, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void put(String keyword, String value) {
        if (table == null) {
            System.err.println("Table doesn't exists");
            return;
        }
        try {
            table.putItem(new Item()
                    .withPrimaryKey("keyword", keyword).withInt("inx", ++inx).withString("value", value));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean supportsPrefixes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sync() {
    }

    @Override
    public boolean isCompressible() {
        return false;
    }

    @Override
    public boolean supportsMoreThan256Attributes() {
        return true;
    }

    private void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(Config.ACCESS_KEY_ID, Config.SECRET_ACCESS_KEY);
        client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Config.AMAZON_REGION).build();
        dynamoDB = new DynamoDB(client);

        table = dynamoDB.getTable(dbName);

//        if (table == null) {
        table = createTable();
//        }

    }

    private Table createTable() {
        try {
            System.out.println("Attempting to create table; please wait...");
            Table table = dynamoDB.createTable(dbName,
                    Arrays.asList(
                            new KeySchemaElement("keyword", KeyType.HASH),
                            new KeySchemaElement("inx", KeyType.RANGE)), //Sort key
                    Arrays.asList(
                            new AttributeDefinition("keyword", ScalarAttributeType.S),
                            new AttributeDefinition("inx", ScalarAttributeType.N)
                    ),
                    new ProvisionedThroughput(5L, 5L));
            table.waitForActive();
            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());
            return table;
        } catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }
        return table;
    }

}
