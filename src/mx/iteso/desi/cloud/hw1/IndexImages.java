package mx.iteso.desi.cloud.hw1;

import java.io.FileNotFoundException;
import java.io.IOException;
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

        String absoluteImagePath = Config.PATH_TO_FILES + imageFileName;
        String absolutetitlePath = Config.PATH_TO_FILES + titleFileName;
        ParseTriples parseImages = new ParseTriples(absoluteImagePath);
        ParseTriples parseTitle = new ParseTriples(absolutetitlePath);
        try {
            int limit = 1_000_000;
            int counter = 0;
            Triple t = null;
            while (counter != limit && (t = parseImages.getNextTriple()) != null) {
                String subject = t.getSubject();
                if (filter(subject) && !imageStore.exists(subject)) {
                    imageStore.put(subject, t.getObject());
                    counter++;
                }
            }
            System.out.println("Images added " + counter);
            counter = 0;
            while (counter != limit && (t = parseTitle.getNextTriple()) != null) {
                String subject = t.getSubject();
                if (imageStore.exists(subject)) {
                    String term = t.getObject();
                    String[] termisA = term.split(" ");
                    for (String key : termisA) {
                        String keyStem = PorterStemmer.stem(key);
                        if (keyStem != null && !keyStem.contains(Config.invalidTerm)) {
                            titleStore.put(keyStem, subject);
                            counter++;
                        }
                    }
                }
            }
            System.out.println("Titles added " + counter);
        } finally {
            parseImages.close();
            parseTitle.close();
        }
    }

    private boolean filter(String subject) {
        boolean result = false;
        if (subject == null) {
            return result;
        }
        switch (Config.FILTER_CRITERIA) {
            case URL_STARTS:
                if (subject.substring(subject.lastIndexOf("/") + 1).toUpperCase().startsWith(Config.FILTER.toUpperCase())) {
                    result = true;
                }
                break;
            case URL_CONTAINS:
                if (subject.substring(subject.lastIndexOf("/") + 1).toUpperCase().contains(Config.FILTER.toUpperCase())) {
                    result = true;
                }
                break;
        }
        return result;
    }

    public void close() {
        imageStore.close();
        titleStore.close();
    }

    public static void main(String args[]) {
        // TODO: Add your own name here
        System.out.println("*** Alumno: Angel de Jesus Bañuelos Sahagun (Exp:MS705167 )");
        try {

            IKeyValueStorage imageStore = KeyValueStoreFactory.getNewKeyValueStore(Config.STORE_TYPE,
                    Config.IMAGE_TABLE_NAME);
            IKeyValueStorage titleStore = KeyValueStoreFactory.getNewKeyValueStore(Config.STORE_TYPE,
                    Config.TITLE_TABLE_NAME);

            IndexImages indexer = new IndexImages(imageStore, titleStore);
            indexer.run(Config.IMAGE_FILE_NAME, Config.TITLE_FILE_NAME);
            
            System.out.println("Indexing completed ");
            indexer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to complete the indexing pass -- exiting");
        }
    }
}
