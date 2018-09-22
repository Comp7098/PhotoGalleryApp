package ca.bcit.c7098.photogalleryapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

    // Path of the current image being displayed
    private String mCurrentPhotoPath;

    // Gallery
    private List<ImageData> mImageList;
    private RecyclerView imageGalleryView;
    private ImageGalleryAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create gallery
//        mPhotoPaths = new ArrayList<>();

        // Grab buttons
        buttonEdit = findViewById(R.id.button_edit);
        buttonScrollPrev = findViewById(R.id.button_scroll_prev);
        buttonScrollNext = findViewById(R.id.button_scroll_next);
        buttonSearch = findViewById(R.id.button_search);
        buttonTakePicture = findViewById(R.id.button_take_picture);

        // Create RecyclerView for viewing the image along with the caption, timestamp and location
        imageGalleryView = findViewById(R.id.image_gallery);
        imageGalleryView.setHasFixedSize(true);
        imageGalleryView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new ImageGalleryAdapter(mImageList);
        imageGalleryView.setAdapter(mAdapter);

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
                // something like:
                // imageGallery.showNext();

            }
        });

        buttonScrollPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to the previous picture in the gallery
                // imageGallery.showPrevious();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = Utilities.createImageFile(getApplicationContext());
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
            // save the picture in the gallery
        }
    }





}
