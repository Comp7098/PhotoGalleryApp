package ca.bcit.c7098.photogalleryapp.ui;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationProvider;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.bcit.c7098.photogalleryapp.BuildConfig;
import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.common.Utilities;
import ca.bcit.c7098.photogalleryapp.data.AppDatabase;
import ca.bcit.c7098.photogalleryapp.data.Photo;
import ca.bcit.c7098.photogalleryapp.data.PhotoRepository;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_LOCATION = 1;
    // Request codes
    static final int REQUEST_IMAGE_CAPTURE = 1;

    // Image data string
    public static final String KEY_IMAGE_DATA = "data";

    // UI Elements
    private Button buttonEdit;
    // TODO: Remove next & previous butons because they don't make sense any more.
    private Button buttonScrollPrev;
    private Button buttonScrollNext;
    private Button buttonSearch;
    private Button buttonTakePicture;
    private EditText editCaption;

    // Path of the current image being displayed
    private String mCurrentPhotoPath;
    private double mLatitude;
    private double mLongitude;

    // Gallery
    private List<Photo> photos;
    private RecyclerView recyclerView;
    private PhotoGalleryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private AppDatabase db;

    private PhotoViewModel photoViewModel;

    private FusedLocationProviderClient mFusedLocationClient;
    private boolean locationEnabled;

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

        editCaption = findViewById(R.id.caption);

        recyclerView = findViewById(R.id.image_gallery);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PhotoGalleryAdapter(this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
      //  snapHelper.attachToRecyclerView(recyclerView);


        // Get ViewModel and observe the data provider
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        photoViewModel.getAllPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationEnabled = true;
                }
            }
        }
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
    protected void onPause() {
        super.onPause();
        if (photoViewModel != null)
            photoViewModel.updateAll();
    }

    @Override
    public void onBackPressed() {
        if (photoViewModel != null) {
            photoViewModel.updateAll();
        }
        super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check result
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // TODO: Create a location provider
            String timestamp = new SimpleDateFormat("EEE, MMM d yyyy", Locale.CANADA).format(new Date());
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            } else {
//                LocationCallback callback = new LocationCallback() {
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        super.onLocationResult(locationResult);
//
//                    }
//                };
//                LocationRequest request = new LocationRequest();
//                mFusedLocationClient.requestLocationUpdates(request, callback, null);
//                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, listener);
                Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLatitude = task.getResult().getLatitude();
                            mLongitude = task.getResult().getLongitude();
                        }
                    }
                });
            }

            Photo p = new Photo();
          //  p.setCaption("");
            p.setDate(timestamp);
            p.setLatitude(mLatitude);
            p.setLongitude(mLongitude);
            p.setPhotoPath(mCurrentPhotoPath);
            photoViewModel.insert(p);
        }
    }

}
