package mx.iteso.desi.cloud.hw1;

import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import mx.iteso.desi.cloud.keyvalue.KeyValueStoreFactory;
import mx.iteso.desi.cloud.keyvalue.IKeyValueStorage;
import mx.iteso.desi.cloud.keyvalue.PorterStemmer;

public class QueryImages {

    IKeyValueStorage imageStore;
    IKeyValueStorage titleStore;

    public QueryImages(IKeyValueStorage imageStore, IKeyValueStorage titleStore) {
        this.imageStore = imageStore;
        this.titleStore = titleStore;
    }

    public Set<String> query(String word) {
        // TODO: Return the set of URLs that match the given word,
        //       or an empty set if there are no matches
        String keyStem = PorterStemmer.stem(word);
        HashSet<String> imageSet = new HashSet();
        if (keyStem != null && !keyStem.contains(Config.invalidTerm)) {
            Set<String> query = titleStore.get(keyStem);
            for (String row : query) {
                imageSet.add(imageStore.get(row).toString());
            }
            return imageSet;
        }

        return imageSet;
    }

    public void close() {
        imageStore.close();
        titleStore.close();
    }

    public static void main(String args[]) {
        // TODO: Add your own name here
        System.out.println("*** Alumno: Angel de Jesus Bañuelos Sahagun (Exp: MS705167 )");

        // TODO: get KeyValueStores
        IKeyValueStorage imageStore = null;
        IKeyValueStorage titleStore = null;
        String[] args1 = {"American", "National", "Standards", "Institute"};
        args = args1;
        try {

            imageStore = KeyValueStoreFactory.getNewKeyValueStore(Config.IMAGE_TABLE_NAME);
            titleStore = KeyValueStoreFactory.getNewKeyValueStore(Config.TITLE_TABLE_NAME);

            QueryImages myQuery = new QueryImages(imageStore, titleStore);

            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i] + ":");
                Set<String> result = myQuery.query(args[i]);
                Iterator<String> iter = result.iterator();
                while (iter.hasNext()) {
                    System.out.println("  - " + iter.next());
                }
            }

            myQuery.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed " + e.getMessage());
        }
    }
}
