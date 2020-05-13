package ch.zhaw.prog2.io.picturedb;

import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Picture extends Record {

    private final URL url;
    private final Date date;
    private final String title;
    private final float longitude;
    private final float latitude;

    public Picture(URL url, Date date, String title, float longitude, float latitude) {
        this.id = UUID.randomUUID().toString();
        this.url = url;
        this.date = date;
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Picture(URL url, String title) {
        this(url, new Date(), title, 0, 0);
    }

    protected Picture(String id, URL url, Date date, String title, float longitude, float latitude) {
        this(url,date, title, longitude, latitude);
        this.id = id;
    }

    public URL getUrl() {
        return url;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Float.compare(picture.longitude, longitude) == 0 &&
            Float.compare(picture.latitude, latitude) == 0 &&
            url.equals(picture.url) &&
            date.equals(picture.date) &&
            title.equals(picture.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, date, title, longitude, latitude);
    }

    @Override
    public String toString() {
        return "Picture{" +
            "id=" + id +
            ", url=" + url +
            ", date=" + date +
            ", title='" + title + '\'' +
            ", longitude=" + longitude +
            ", latitude=" + latitude +
            '}';
    }

}
