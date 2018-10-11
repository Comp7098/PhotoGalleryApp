package ca.bcit.c7098.photogalleryapp.ui;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.support.v7.widget.LinearSnapHelper;
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
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ca.bcit.c7098.photogalleryapp.BuildConfig;
import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.common.Utilities;
import ca.bcit.c7098.photogalleryapp.data.AppDatabase;
import ca.bcit.c7098.photogalleryapp.data.Photo;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_LOCATION = 1;
    // Request codes
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SEARCH = 2;

    // Image data string
    public static final String KEY_IMAGE_DATA = "data";

    // UI Elements
    private Button buttonSearch;
    private Button buttonTakePicture;

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
        buttonSearch = findViewById(R.id.button_filter);
        buttonTakePicture = findViewById(R.id.button_take_picture);

        recyclerView = findViewById(R.id.image_gallery);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PhotoGalleryAdapter(this);
        recyclerView.setAdapter(mAdapter);

        // TODO: Fix RecyclerView not scaling properly when the screen rotates
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

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        // Get ViewModel and observe the data provider
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        photoViewModel.getAllPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                Log.d("recyclerview", "Item count: " + mAdapter.getItemCount());
                mAdapter.setData(photos);
            }
        });


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchActivity.class);
                startActivityForResult(intent, REQUEST_SEARCH);
            }
        });

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);

        if (locationEnabled) {
            final LocationCallback callback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }
            };
            final LocationRequest request = new LocationRequest();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);

            } else {
                mFusedLocationClient.requestLocationUpdates(request, callback, null);
                final Task<Location> locationResult = mFusedLocationClient.getLastLocation();
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationEnabled = true;
                    // mFusedLocationClient.requestLocationUpdates(request, callback, null);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    final Task<Location> locationResult = mFusedLocationClient.getLastLocation();
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
        if (photoViewModel != null) {
            photoViewModel.updateAll();
        }
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
            }

            Photo p = new Photo();
          //  p.setCaption("");
            p.setDate(new Date());
            p.setLatitude(mLatitude);
            p.setLongitude(mLongitude);
            p.setPhotoPath(mCurrentPhotoPath);
            photoViewModel.insert(p);
        } else if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK && data != null) {
            // Have received search parameters from the Search activity
            Bundle result = data.getExtras();
            if (result != null) {
                // Search by date
                long defaultValue = -1;
                long start = result.getLong(SearchActivity.START_DATE, defaultValue);
                long end = result.getLong(SearchActivity.END_DATE, defaultValue);
                if (start != defaultValue && end != defaultValue) {
                    mAdapter.filterByDate(start, end);
                    return;
                }

                // Search by keyword
                String strDefault = "";
                String keyword = result.getString(SearchActivity.KEYWORD, strDefault);
                if (!keyword.equals(strDefault)) {
                    mAdapter.filterByKeyword(keyword);
                } else {

                    // Search by location
                    double def = 0;
                    double lat = Double.parseDouble(result.getString(SearchActivity.TOP_LEFT, strDefault));
                    double lng = Double.parseDouble(result.getString(SearchActivity.BOTTOM_RIGHT, strDefault));
                    mAdapter.filterByLocation(lat, lng);
                }


            }
        }
    }

}
