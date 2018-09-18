package ca.bcit.c7098.photogalleryapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab buttons
        buttonEdit = findViewById(R.id.button_edit);
        buttonScrollPrev = findViewById(R.id.button_scroll_prev);
        buttonScrollNext = findViewById(R.id.button_scroll_next);
        buttonSearch = findViewById(R.id.button_search);
        buttonTakePicture = findViewById(R.id.button_take_picture);

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
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) == null) {
                    Log.e("Image Capture Error", "Cannot resolve activity");
                    return;
                }

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check result
        if (requestCode != REQUEST_IMAGE_CAPTURE || resultCode != RESULT_OK) {
            Log.e("Image Capture Error", "Capture unsuccessful.");
            return;
        }

        // Check data
        Bundle extras = data.getExtras();
        if (extras == null)
        {
            Log.e("Image Capture Error", "Unable to retrieve data from camera");
            return;
        }

        // Display image
        Bitmap imageBitmap = (Bitmap) extras.get(KEY_IMAGE_DATA);
        ImageView imageView = findViewById(R.id.image_view_main);
        imageView.setImageBitmap(imageBitmap);
    }
}
