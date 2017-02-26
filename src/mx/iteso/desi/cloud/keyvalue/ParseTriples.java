package mx.iteso.desi.cloud.keyvalue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ParseTriples {

    FileInputStream inputStream = null;
    Scanner sc = null;

    public ParseTriples(String docName) throws FileNotFoundException {
        inputStream = new FileInputStream(new File(docName));
    }

    public Triple getNextTriple() throws IOException {
        if (sc == null) {
            sc = new Scanner(inputStream, "UTF-8");
        }

        if (sc == null) {
            throw new IOException("Error while loading file");
        }

        if (!sc.hasNextLine()) {
            return null;
        }
        String str = sc.nextLine();

        while ((str != null) && str.startsWith("#") && sc.hasNextLine()) {
            str = sc.nextLine();
        }

        if (str == null) {
            return null;
        }

        int subjLAngle = 0;
        int subjRAngle = str.indexOf('>');
        int predLAngle = str.indexOf('<', subjRAngle + 1);
        int predRAngle = str.indexOf('>', predLAngle + 1);
        int objLAngle = str.indexOf('<', predRAngle + 1);
        int objRAngle = str.indexOf('>', objLAngle + 1);

        if (objLAngle == -1) {
            objLAngle = str.indexOf('\"', predRAngle + 1);
            objRAngle = str.indexOf('\"', objLAngle + 1);
        }
        String subject = null;
        String predicate = null;
        String object = null;
        try {
            subject = str.substring(subjLAngle + 1, subjRAngle);
            predicate = str.substring(predLAngle + 1, predRAngle);
            object = str.substring(objLAngle + 1, objRAngle);
        } catch (StringIndexOutOfBoundsException e) {
            return getNextTriple();
        }
        return new Triple(subject, predicate, object);
    }

    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (sc != null) {
            sc.close();
        }
    }
}
