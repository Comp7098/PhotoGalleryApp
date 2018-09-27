package ca.bcit.c7098.photogalleryapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

@Entity
public class GalleryItem {
    @PrimaryKey
    private int uid;

    @ColumnInfo(name="image_path")
    Bitmap bm;

    @ColumnInfo(name="date")
    private String date;

    @ColumnInfo(name="location")
    private String location;


}
