package ca.bcit.c7098.photogalleryapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Request codes
    static final int REQUEST_IMAGE_CAPTURE = 1001;

    // Image data string
    public static final String KEY_IMAGE_DATA = "data";

    // UI Elements
    private Button buttonEdit;
    private Button buttonScrollPrev;
    private Button buttonScrollNext;
    private Button buttonSearch;
    private Button buttonTakePicture;

    // Image related UI elements
    private ImageView mImageView;
    private TextView mTimeStamp;
    private TextView mCaption;
    private TextView mLocation;

    // Path of the current image being displayed
    private String mCurrentPhotoPath;

    // Gallery
    private List<String> mPhotoPaths;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create gallery
        mPhotoPaths = new ArrayList<>();

        // Grab buttons
        buttonEdit = findViewById(R.id.button_edit);
        buttonScrollPrev = findViewById(R.id.button_scroll_prev);
        buttonScrollNext = findViewById(R.id.button_scroll_next);
        buttonSearch = findViewById(R.id.button_search);
        buttonTakePicture = findViewById(R.id.button_take_picture);

        // Grab image data
        mImageView = findViewById(R.id.image_view_main);
        mCaption = findViewById(R.id.caption);
        mTimeStamp = findViewById(R.id.timestamp);
        mLocation = findViewById(R.id.location);

        // Assign Listeners
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                startActivity(intent);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        buttonScrollNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to the next picture in the gallery

            }
        });

        buttonScrollPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to the previous picture in the gallery
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred
                Log.e("Image file error", "Cannot create image file");
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check result
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && mCurrentPhotoPath != null) {
            // Get the dimensions of the View
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            mImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * @return A full sized image file of the photo that was taken
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

}
