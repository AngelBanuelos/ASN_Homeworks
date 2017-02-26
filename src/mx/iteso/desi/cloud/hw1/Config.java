/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.iteso.desi.cloud.hw1;

import com.amazonaws.regions.Regions;
import mx.iteso.desi.cloud.keyvalue.KeyValueStoreFactory;

/**
 *
 * @author parres
 */
public class Config {

    // The type of key/value store you are using. Initially set to BERKELEY;
    // will be changed to DynamoDB in some phases.
    public static final KeyValueStoreFactory.STORETYPE STORE_TYPE = KeyValueStoreFactory.STORETYPE.DYNAMODB;
    public static final String PATH_TO_DATABASE = "/Users/angel_banuelos/Downloads/LabelsDBs/db";

    public static final String PATH_TO_FILES = "/Users/angel_banuelos/Downloads/";

    // Set to your Amazon Access Key ID
    // NEVER SHARE THIS INFORMATION. SO PLEASE SET IT TO "" WHEN YOU UPLOAD YOUR HOMEWORK 
    public static final String ACCESS_KEY_ID = "";

    // Set to your Amazon Secret Access Key
    // NEVER SHARE THIS INFORMATION. SO PLEASE SET IT TO "" WHEN YOU UPLOAD YOUR HOMEWORK 
    public static final String SECRET_ACCESS_KEY = "";

    // Set to your Amazon REGION tu be used
    public static final Regions AMAZON_REGION = Regions.US_WEST_2;

    // Restrict the topics that should be indexed. For example, when this is
    // set to 'X', you should only index topics that start with an X.
    // Set this to "A" for local work, and to "Ar" for cloud tests..
    public static final String FILTER = "A";
    public static FilterProperties FILTER_CRITERIA = FilterProperties.URL_STARTS;
    public static String invalidTerm = "Invalid term";
    public static boolean TEST_MODE = true;

    public enum FilterProperties {
        URL_STARTS,
        URL_CONTAINS
    }
    public static final String TITLE_FILE_NAME = "labels_en.ttl";
    public static final String IMAGE_FILE_NAME = "images_en.ttl";

    public static final String IMAGE_TABLE_NAME = "imagesTrue";
    public static final String TITLE_TABLE_NAME = "termsTrue";

}
