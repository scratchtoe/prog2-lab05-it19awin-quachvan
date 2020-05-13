package ch.zhaw.prog2.io.picturedb;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the PictureDatasource Interface storing the data in
 * Character Separated Values (CSV) format, where each line consists of a record
 * whose fields are separated by the DELIMITER ";"
 * See example file: db/picture-data.csv
 */
public class FilePictureDatasource implements PictureDatasource {
    private static final Logger LOGGER = Logger.getLogger(PictureDatasource.class.getPackageName()); //log.properties werden in der mainklasse initialisiert

    private static final String DELIMITER = "; ";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat DF = new SimpleDateFormat(DATE_FORMAT);
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private File file;
    private File tempFile;
    /**
     * Creates the FilePictureDatasource with the given file as datafile.
     *
     * @param filepath of the file to use as database file.
     * @throws IOException if accessing or creating the file failes
     */
    public FilePictureDatasource(String filepath) throws IOException {
        LOGGER.log(Level.CONFIG, "Creating FilePictureDataSource.");
        file = new File(filepath);
        if(!file.exists()) throw new FileNotFoundException();
        LOGGER.log(Level.FINER, "filepath is set: {0}", filepath);
        LOGGER.log(Level.FINEST, "Ich darf nicht dargestellt werden");
        LOGGER.log(Level.INFO, "Created new instance of FilePictureDataSource");
    }


    @Override
    public void insert(Picture picture) throws IOException {
        LOGGER.log(Level.FINE, "Method insert called");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, CHARSET, true))) {
            writer.write(picture.getId() + DELIMITER +
                DF.format(picture.getDate()) + DELIMITER +
                picture.getLongitude() + DELIMITER +
                picture.getLatitude() + DELIMITER +
                picture.getTitle() + DELIMITER +
                picture.getUrl());
            writer.newLine();
        }
        LOGGER.log(Level.INFO, "Picture inserted: {0}", picture.toString());
    }

    @Override
    public void update(Picture picture) throws RecordNotFoundException, IOException {
        LOGGER.log(Level.FINE, "Method update called");
        tempFile = new File(file.getParent(), "picture-data-temp.csv");
        if(tempFile.exists()) tempFile.delete();
        tempFile.createNewFile();
        boolean recordFound = false;
        try(BufferedReader reader = new BufferedReader(new FileReader(file, CHARSET));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, CHARSET, true))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] record = line.split(DELIMITER);
                if(record[0].equals(picture.getId())) {
                    writer.write(picture.getId() + DELIMITER +
                        DF.format(picture.getDate()) + DELIMITER +
                        picture.getLongitude() + DELIMITER +
                        picture.getLatitude() + DELIMITER +
                        picture.getTitle() + DELIMITER +
                        picture.getUrl() + "\n");
                    recordFound = true;
                }
                else {
                    writer.write(line);
                    writer.newLine();
                }
            }

        }
        if(!recordFound){
            String errorMessage = "No picture found.";
            tempFile.delete();
            LOGGER.log(Level.WARNING,"{0}", errorMessage);
            throw new RecordNotFoundException(errorMessage);
        }
        file.delete();
        tempFile.renameTo(file);
        LOGGER.log(Level.INFO, "Picture updated: {0}", picture.toString());
    }

    @Override
    public void delete(Picture picture) throws RecordNotFoundException, IOException {
        LOGGER.log(Level.FINE, "Method delete called");
        tempFile = new File(file.getParent(), "picture-data-temp.csv");
        if(tempFile.exists()) tempFile.delete();
        tempFile.createNewFile();
        boolean recordFound = false;
        try(BufferedReader reader = new BufferedReader(new FileReader(file, CHARSET));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, CHARSET, true))) {
            String line;
            while(!recordFound && (line = reader.readLine()) != null) {
                String[] record = line.split(DELIMITER);
                if(record[0].equals(picture.getId())) {
                    recordFound = true;
                }
                else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
        if(!recordFound){
            String errorMessage = "No picture found.";
            LOGGER.log(Level.SEVERE, "AAHAHAHAHAHAHAHA! Kritische Situation");
            LOGGER.log(Level.WARNING,"{0}", errorMessage);
            tempFile.delete();
            throw new RecordNotFoundException(errorMessage);
        }
        file.delete();
        tempFile.renameTo(file);
        LOGGER.log(Level.INFO, "Picture deleted: {0}", picture.toString());
    }

    @Override
    public int count() throws IOException {
        LOGGER.log(Level.FINE, "Method insert called");
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while(reader.readLine() != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Picture findById(String id) throws IOException, ParseException, RecordNotFoundException {
        LOGGER.log(Level.FINE, "Method findById called");
        try(BufferedReader reader = new BufferedReader(new FileReader(file, CHARSET))) {
            Picture picture = null;
            String line;
            boolean recordFound = false;
            boolean searching = true;
            while(searching && (line = reader.readLine()) != null) {
                String[] record = line.split(DELIMITER);
                if (id.equals(record[0])) {
                    picture = new Picture(record[0],
                        new URL(record[5]),
                        DF.parse(record[1]),
                        record[4],
                        Float.parseFloat(record[2]),
                        Float.parseFloat(record[3]));
                    searching = false;
                    recordFound = true;
                }
            }
            if(!recordFound) throw new RecordNotFoundException("Record not found!");
            return picture;
        }
    }

    @Override
    public Collection<Picture> findAll() throws IOException, ParseException {
        LOGGER.log(Level.FINE, "Method findAll called");
        Collection<Picture> collection = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file, CHARSET))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] record = line.split(DELIMITER);
                collection.add(new Picture(record[0],
                    new URL(record[5]),
                    DF.parse(record[1]),
                    record[4],
                    Float.parseFloat(record[2]),
                    Float.parseFloat(record[3])));
            }
        }
        return collection;
    }

    @Override
    public Collection<Picture> findByPosition(float longitude, float latitude, float deviation) throws IOException, ParseException {
        LOGGER.log(Level.FINE, "Method findByPosition called");
        Collection<Picture> collection = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitter = line.split(DELIMITER);
                Picture picture = new Picture(
                    splitter[0],
                    (new URL(splitter[5])),
                    DF.parse(splitter[1]),
                    splitter[4],
                    Float.valueOf(splitter[2]),
                    Float.valueOf(splitter[3]));

                float latitudeMin = latitude - deviation;
                float latitudeMax = latitude + deviation;
                float longitudeMin = longitude - deviation;
                float longitudeMax = longitude + deviation;

                if ((picture.getLatitude() > latitudeMin) && (picture.getLatitude() < latitudeMax) && (picture.getLongitude() > longitudeMin) && (picture.getLongitude() < longitudeMax)) {
                    collection.add(picture);
                }
            }
        }
        return collection;
    }

}
