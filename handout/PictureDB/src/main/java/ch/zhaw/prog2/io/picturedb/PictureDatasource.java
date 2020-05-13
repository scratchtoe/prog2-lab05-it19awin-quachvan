package ch.zhaw.prog2.io.picturedb;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

public interface PictureDatasource extends GenericDatasource<Picture> {
    /**
     * Retrieves all images close to a a certain position.
     * All images with a deviation from the exact coordinates are returned.
     * This includes all objects in a square range
     * from [longitude - deviation / latitude - deviation]
     * to   [longitude + deviation / latitude + deviation]
     * @param longitude longitude coordinate of the center of the area
     * @param latitude  latitude coordinate of the center of the area
     * @param deviation deviation from the center of the area in longitude and latitude direction
     * @return Collection of all Picture records in the area
     */
    Collection<Picture>findByPosition(float longitude, float latitude, float deviation) throws IOException, ParseException;
}
