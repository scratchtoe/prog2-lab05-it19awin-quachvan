package ch.zhaw.prog2.io.picturedb;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class FilePictureDatasourceTest {
    /*
    e. Schreiben Sie die Testklasse FilePictureDatasourceTest, in welcher Sie Tests für die verschiedenen
       Methoden implementieren. Für jede modifizierende Methode muss mindestens ein positiver und ein negativer
       Test geschrieben werden.
       Modifizierende Methoden: insert(), update(), delete()
     */
    private static final String DELIMITER = "; ";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat DF = new SimpleDateFormat(DATE_FORMAT);
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Test
    void testInsertPositive() {
        try {
            File file = new File("./src/test/resources/testInsert.csv");
            if(file.exists()) file.delete();
            file.createNewFile();
            FilePictureDatasource datasource = new FilePictureDatasource("./src/test/resources/testInsert.csv");
            datasource.insert(new Picture("1",
                new URL("https://static.geo.de/bilder/62/dd/63111/article_image_big/panda-cb-18781595.jpg"),
                DF.parse("2020-05-12 19:29:05"),
                "Great Bori",
                0.0f,
                0.0f));
            BufferedReader reader = new BufferedReader(new FileReader(new File("./src/test/resources/testInsert.csv"), CHARSET));
            String[] record = reader.readLine().split(DELIMITER);
            reader.close();
            assertEquals("1", record[0]);
        } catch(IOException | ParseException e) {
            fail();
        }
    }

    @Test
    void testInsertNegative() {
        try {
            File file = new File("./src/test/resources/testInsert.csv");
            if(file.exists()) file.delete();
            file.createNewFile();
            assertThrows(NullPointerException.class, () -> {
                FilePictureDatasource datasource = new FilePictureDatasource("./src/test/resources/testInsert.csv");
                datasource.insert(new Picture("1",
                    new URL("https://static.geo.de/bilder/62/dd/63111/article_image_big/panda-cb-18781595.jpg"),
                    DF.parse(null),
                    "Great Bori",
                    0.0f,
                    0.0f));
            });
            BufferedReader reader = new BufferedReader(new FileReader(new File("./src/test/resources/testInsert.csv"), CHARSET));
            assertNull(reader.readLine());
            reader.close();
        } catch(IOException e) {
            fail();
        }
    }

    @Test
    void testDeletePositiv()
    {
        try {
            File file = new File("./src/test/resources/testDelete.csv");
            if(file.exists()) file.delete();
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, CHARSET, true));
            writer.write("1; 2020-05-12 19:29:05; 0.0; 0.0; Great Bori; https://www.wienerzeitung.at/_em_daten/_cache/image/1xgKsc-DerrZZAqoImD5xiBsZMu52b_cQmvyNY-rTHrnvaskF07cvjbCET3jPykeJ75B9f0FMKO3auYjSyQQdjKcBZpLQ-0yA8C5LMdPLpuEYKTrUjjU5yjg/190719-1711-948-0900-220324-200703untenr.jpg");
            writer.close();
            FilePictureDatasource datasource = new FilePictureDatasource("./src/test/resources/testDelete.csv");
            datasource.delete(new Picture("1",
                new URL("https://static.geo.de/bilder/62/dd/63111/article_image_big/panda-cb-18781595.jpg"),
                DF.parse("2020-05-12 19:29:05"),
                "Great Bori",
                0.0f,
                0.0f));
            BufferedReader reader = new BufferedReader(new FileReader(new File("./src/test/resources/testDelete.csv"), CHARSET));
            assertNull(reader.readLine());
            reader.close();
        } catch(IOException | ParseException | RecordNotFoundException e) {
            fail();
        }
    }

    @Test
    void testDeleteNegative()
    {
        try {
            File file = new File("./src/test/resources/testDelete.csv");
            if(file.exists()) file.delete();
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, CHARSET, true));
            writer.write("1; 2020-05-12 19:29:05; 0.0; 0.0; Great Bori; https://static.geo.de/bilder/62/dd/63111/article_image_big/panda-cb-18781595.jpg");
            writer.close();
            assertThrows(RecordNotFoundException.class, () -> {
                FilePictureDatasource datasource = new FilePictureDatasource("./src/test/resources/testDelete.csv");
                datasource.delete(new Picture("2",
                    new URL("https://static.geo.de/bilder/62/dd/63111/article_image_big/panda-cb-18781595.jpg"),
                    DF.parse("2020-05-12 19:29:05"),
                    "Great Bori",
                    0.0f,
                    0.0f));
            });
            BufferedReader reader = new BufferedReader(new FileReader(new File("./src/test/resources/testDelete.csv"), CHARSET));
            String[] record = reader.readLine().split(DELIMITER);
            reader.close();
            assertEquals("1", record[0]);

        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testCount()
    {
        try {
            String filepath = "./src/test/resources/test-count.csv";
            FilePictureDatasource fpd = new FilePictureDatasource(filepath);
            assertEquals(5, fpd.count());
        } catch (IOException e) {
            fail();
        }
    }

    @Test   //Nicht sicher was es hier für negative Tests geben soll, lese leeres File
    void testCountEmptyFile()
    {
        try {
            String filepath = "./src/test/resources/test-empty-file.csv";
            ArrayList<Picture> pictureList = new ArrayList<>();
            FilePictureDatasource fpd = new FilePictureDatasource(filepath);
            assertEquals(0, fpd.count());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testFindByID() throws IOException, ParseException, RecordNotFoundException
    {
        String filepath = "./src/test/resources/test-find-by-id.csv";
        FilePictureDatasource fpd = new FilePictureDatasource(filepath);
        String sDate1 = "2020-05-12 22:54:45";
        Date date1 = new SimpleDateFormat(DATE_FORMAT).parse(sDate1);

        assertEquals(new Picture("abab", new URL(
            "https://einfachtierisch.de/media/cache/article_teaser/cms/2013/11/Hund-Jung-Silvester.jpg?327905"),
            date1, "Suesser Hund", 21.0f, 20.0f), fpd.findById("abab"));
    }

    @Test
    void testFindByIDInvalid()
    {
        try {
            String filepath = "./src/test/resources/test-find-by-id.csv";
            FilePictureDatasource fpd = new FilePictureDatasource(filepath);

            Exception exception = assertThrows(RecordNotFoundException.class, () -> {
                fpd.findById("aaaaaaaaaaaa");
            });
            String expectedMessage = "Record not found!";
            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        } catch(IOException e) {
            fail();
        }
    }

    @Test
    void testFindAll() throws ParseException, IOException
    {
        String filepath = "./src/test/resources/test-find-all.csv";
        FilePictureDatasource fpd = new FilePictureDatasource(filepath);
        ArrayList<Picture> pictureList = new ArrayList<>();

        String sDate1 = "2020-05-12 22:53:59";
        String sDate2 = "2020-05-12 22:54:45";
        String sDate3 = "2020-05-12 22:57:06";

        Date date1 = new SimpleDateFormat(DATE_FORMAT).parse(sDate1);
        Date date2 = new SimpleDateFormat(DATE_FORMAT).parse(sDate2);
        Date date3 = new SimpleDateFormat(DATE_FORMAT).parse(sDate3);

        pictureList.add(new Picture("cda", new URL("https://partner-hund.de/sites/partner-hund.de/files/grund-kommandos-hund.jpg"),
            date1, "Aaron", 20.0f, 30.0f));
        pictureList.add(new Picture("abab", new URL("https://einfachtierisch.de/media/cache/article_teaser/cms/2013/11/Hund-Jung-Silvester.jpg?327905"),
            date2, "Suesser Hund", 21.0f, 20.0f));
        pictureList.add(new Picture("aabb", new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbCBSuz4tB9xCh50Ho7b6_Wc4BG00aoirmYDoXx077KBrQwBdO&usqp=CAU"),
            date3, "Rex", 50.0f, 40.0f));

        assertEquals(pictureList, fpd.findAll());
    }

    @Test //Nicht sicher was es hier für negative Tests geben soll, lese leeres File
    void testFindAllInvalid()
    {
        String filepath = "./src/test/resources/test-empty-file.csv";
        ArrayList<Picture> pictureList = new ArrayList<>();
        try {
            FilePictureDatasource fpd = new FilePictureDatasource(filepath);
            assertEquals(pictureList, fpd.findAll());
        } catch(IOException | ParseException e) {
            fail();
        }
    }


    @Test
    void testFindByPosition() throws ParseException, IOException
    {
        String filepath = "./src/test/resources/test-find-by-position.csv";
        FilePictureDatasource fpd = new FilePictureDatasource(filepath);
        ArrayList<Picture> pictureList = new ArrayList<>();

        String sDate1 = "2020-05-12 22:53:59";
        String sDate2 = "2020-05-12 22:54:45";
        String sDate3 = "2020-05-12 22:57:06";

        Date date1 = new SimpleDateFormat(DATE_FORMAT).parse(sDate1);
        Date date2 = new SimpleDateFormat(DATE_FORMAT).parse(sDate2);
        Date date3 = new SimpleDateFormat(DATE_FORMAT).parse(sDate3);
        pictureList.add(new Picture("cda", new URL("https://partner-hund.de/sites/partner-hund.de/files/grund-kommandos-hund.jpg"),
            date1, "Aaron", 20.0f, 29.0f));
        pictureList.add(new Picture("abab", new URL("https://einfachtierisch.de/media/cache/article_teaser/cms/2013/11/Hund-Jung-Silvester.jpg?327905"),
            date2, "Suesser Hund", 21.0f, 20.0f));
        pictureList.add(new Picture("aabb", new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSbCBSuz4tB9xCh50Ho7b6_Wc4BG00aoirmYDoXx077KBrQwBdO&usqp=CAU"),
            date3, "Rex", 30.0f, 20.0f));

        assertEquals(pictureList, fpd.findByPosition(25.0f, 20.0f, 10.0f));
    }

    @Test //Nicht sicher was es hier für negative Tests geben soll, lese leeres File
    void testFindByPositionInvalid()
    {
        String filepath = "./src/test/resources/test-empty-file.csv";
        ArrayList<Picture> pictureList = new ArrayList<>();
        try {
            FilePictureDatasource fpd = new FilePictureDatasource(filepath);
            assertEquals(pictureList, fpd.findByPosition(5.0f, 5.0f, 5.0f));
        } catch (IOException | ParseException e) {
            fail();
        }
    }

    @Test
    void testUpdatePositive()
    {
        try {
            fillTestUpdateCSV();
            Picture greatestBori = new Picture("1",
                new URL("https://static.geo.de/bilder/62/dd/63111/article_image_big/panda-cb-18781595.jpg"),
                DF.parse("2020-05-12 19:29:05"),
                "Even greater Bori",
                0.0f,
                0.0f);

            FilePictureDatasource datasource = new FilePictureDatasource("./src/test/resources/testUpdate.csv");
            datasource.update(greatestBori);

            BufferedReader reader = new BufferedReader(new FileReader(new File("./src/test/resources/testUpdate.csv"), CHARSET));
            String[] record = reader.readLine().split(DELIMITER);
            reader.close();
            assertEquals("Even greater Bori", record[4]);
        } catch (IOException | ParseException | RecordNotFoundException e) {
            fail();
        }
    }

    @Test
    void testUpdateNegative()
    {
        try {
            fillTestUpdateCSV();
            Picture greatestBori = new Picture("2",
                new URL("https://static.geo.de/bilder/62/dd/63111/article_image_big/panda-cb-18781595.jpg"),
                DF.parse("2020-05-12 19:29:05"),
                "Even greater Bori",
                0.0f,
                0.0f);

            FilePictureDatasource datasource = new FilePictureDatasource("./src/test/resources/testUpdate.csv");
            assertThrows(RecordNotFoundException.class, () -> {
                datasource.update(greatestBori);
            });
        } catch (IOException | ParseException e) {
            fail();
        }
    }

    private void fillTestUpdateCSV() throws IOException, ParseException
    {
        File file = new File("./src/test/resources/testUpdate.csv");
        if(file.exists()) file.delete();
        file.createNewFile();
        FilePictureDatasource datasource = new FilePictureDatasource("./src/test/resources/testUpdate.csv");
        datasource.insert(new Picture("1",
            new URL("https://static.geo.de/bilder/62/dd/63111/article_image_big/panda-cb-18781595.jpg"),
            DF.parse("2020-05-12 19:29:05"),
            "Great Bori",
            0.0f,
            0.0f));
    }
}
