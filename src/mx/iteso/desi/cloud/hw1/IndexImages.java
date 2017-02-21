package mx.iteso.desi.cloud.hw1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import mx.iteso.desi.cloud.keyvalue.IKeyValueStorage;
import mx.iteso.desi.cloud.keyvalue.KeyValueStoreFactory;
import mx.iteso.desi.cloud.keyvalue.ParseTriples;
import mx.iteso.desi.cloud.keyvalue.PorterStemmer;
import mx.iteso.desi.cloud.keyvalue.Triple;

public class IndexImages {

    ParseTriples parser;
    IKeyValueStorage imageStore, titleStore;

    public IndexImages(IKeyValueStorage imageStore, IKeyValueStorage titleStore) {
        this.imageStore = imageStore;
        this.titleStore = titleStore;
    }

    public void run(String imageFileName, String titleFileName) throws FileNotFoundException, IOException {

        String path = Config.pathToDatabase + imageFileName;//titleFileName;//

        FileInputStream inputStream = null;
        Scanner sc = null;
        try {

            inputStream = new FileInputStream(new File(path));
            sc = new Scanner(inputStream, "UTF-8");
            int stop = 10000;
            long count = 0;
            List<String> lines = new ArrayList<>();
            List<Triple> tipleList = new LinkedList<Triple>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                stop--;
                line = line.replace('<', ' ').trim();
//                lines.add(line);
                String[] helper = line.split(">");
                if (helper != null && helper.length > 3) {
                    Triple t = new Triple(helper[0], helper[1], helper[2]);
                    tipleList.add(t);
                } else{
                    System.out.println("" + Arrays.toString(helper));
                }
                System.out.println("" + Arrays.toString(helper));
//                if (line.contains("PaulBasa")) {
//                    System.out.println(line);
//                    lines.add(line);
//                }
                if (stop == 0) {
//                    Path file = Paths.get(Config.pathToDatabase +  "file" + count + ".txt");
//                    Files.write(file, lines, Charset.forName("UTF-8"));
                    System.out.println("File Created" + ++count);
                    stop = 10000;
//                    lines = new ArrayList<>();
                }
            }
            for (String line : lines) {
                System.out.println(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    public void close() {
        //TODO: close the databases;
    }

    public static void main(String args[]) {
        // TODO: Add your own name here
        System.out.println("*** Alumno: Angel de Jesus Bañuelos Sahagun (Exp:MS705167 )");
        try {

            IKeyValueStorage imageStore = KeyValueStoreFactory.getNewKeyValueStore(Config.storeType,
                    "images");
            IKeyValueStorage titleStore = KeyValueStoreFactory.getNewKeyValueStore(Config.storeType,
                    "terms");

            IndexImages indexer = new IndexImages(imageStore, titleStore);
            indexer.run(Config.imageFileName, Config.titleFileName);

            System.out.println("Indexing completed ");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to complete the indexing pass -- exiting");
        }
    }
}
