package ca.bcit.c7098.photogalleryapp.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import ca.bcit.c7098.photogalleryapp.common.Utilities;

@Entity
public class Photo {
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String photoPath;

    private Date date;

    public double getLatitude() {
        return Utilities.round(latitude, 0);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return Utilities.round(longitude, 0);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double latitude;
    private double longitude;

    private String caption;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


}
