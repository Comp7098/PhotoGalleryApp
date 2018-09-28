package ca.bcit.c7098.photogalleryapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ca.bcit.c7098.photogalleryapp.BuildConfig;
import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.Utilities;
import ca.bcit.c7098.photogalleryapp.data.AppDatabase;
import ca.bcit.c7098.photogalleryapp.data.Photo;

public class MainActivity extends AppCompatActivity {

    // Request codes
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
    private List<Photo> photos;
    private RecyclerView imageGalleryView;
    private ImageGalleryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AppDatabase db;

    private PhotoViewModel photoViewModel;

    private int lastVisibleItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load database

        // Grab buttons
        buttonEdit = findViewById(R.id.button_edit);
        buttonScrollPrev = findViewById(R.id.button_scroll_prev);
        buttonScrollNext = findViewById(R.id.button_scroll_next);
        buttonSearch = findViewById(R.id.button_filter);
        buttonTakePicture = findViewById(R.id.button_take_picture);

        // Populate the list of images
//        File pictureDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File[] files = pictureDir.listFiles();


        imageGalleryView = findViewById(R.id.image_gallery);
        imageGalleryView.setHasFixedSize(true);

        //TODO: Fix RecyclerView scrolling extremely slowly
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imageGalleryView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageGalleryAdapter(this);
        imageGalleryView.setAdapter(mAdapter);

        imageGalleryView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("recyclerview", "current state: " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("recyclerview", "moved by x = " + dx + ", y = " + dy);
            }
        });
       // LinearSnapHelper snapHelper = new LinearSnapHelper();
      //  snapHelper.attachToRecyclerView(imageGalleryView);


        // Get ViewModel and observe the data provider
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        photoViewModel.getAllPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                //TODO: Only storing one item at a time for some reason
                Log.d("recyclerview","Item count: " + mAdapter.getItemCount());
                mAdapter.setData(photos);
            }
        });



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
                mAdapter.notifyItemChanged(1);

            }
        });

        buttonScrollPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
                Uri photoUri = FileProvider.getUriForFile(this, authority, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check result
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Photo p = new Photo();
            p.setCaption("");
            p.setDate("");
            p.setLocation("");
            p.setPhotoPath(mCurrentPhotoPath);
            photoViewModel.insert(p);
        }
    }
}
