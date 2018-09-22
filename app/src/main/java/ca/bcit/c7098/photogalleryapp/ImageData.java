package ca.bcit.c7098.photogalleryapp;

import android.media.Image;

public class ImageData {
    private String mCaption;
    private String mTimestamp;
    private String mLocation;
    private Image mImage;

    public ImageData(Image image, String caption, String timestamp, String location) {
        mImage = image;
        mCaption = caption;
        mTimestamp = timestamp;
        mLocation = location;
    }
}
