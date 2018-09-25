package ca.bcit.c7098.photogalleryapp.data;

import android.graphics.Bitmap;
import android.media.Image;

public class ImageData {
    private String mCaption;
    private String mTimestamp;
    private String mLocation;
    private byte[] mImage;

    public ImageData(byte[] image, String caption, String timestamp, String location) {
        mImage = image;
        mCaption = caption;
        mTimestamp = timestamp;
        mLocation = location;
    }
}
