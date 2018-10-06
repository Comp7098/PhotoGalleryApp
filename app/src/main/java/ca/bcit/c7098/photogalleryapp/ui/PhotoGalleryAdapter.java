package ca.bcit.c7098.photogalleryapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.common.Utilities;
import ca.bcit.c7098.photogalleryapp.data.Converters;
import ca.bcit.c7098.photogalleryapp.data.Photo;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ImageViewHolder> {

    private List<Photo> imageDataList;
    private final LayoutInflater inflater;

    public void setData(List<Photo> data) {
        imageDataList = data;
        notifyDataSetChanged();
    }

    PhotoGalleryAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PhotoGalleryAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_layout, parent, false);
        return new ImageViewHolder(view, new CaptionTextWatcher());
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoGalleryAdapter.ImageViewHolder holder, int position) {
        // set what should be shown based on the position of the item
        Log.d("PhotoGalleryAdapter", "onBindViewHolder called");
        if (imageDataList != null) {
            Photo current = imageDataList.get(position);
            Glide.with(holder.itemView).load(current.getPhotoPath()).into(holder.mImageView);
            holder.mTimestamp.setText(Converters.fromDate(current.getDate()));
            holder.mLocation.setText(current.getLatitude() + ", " + current.getLongitude());
            holder.watcher.setPosition(position);
            holder.mCaption.setText(current.getCaption());
        }
    }



    @Override
    public int getItemCount() {
        return imageDataList != null ? imageDataList.size() : 0;
    }


    /**
     * Holds the data that will be displayed in the image view
     */
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private EditText mCaption;
        private TextView mTimestamp;
        private TextView mLocation;
        private View layout;

        private CaptionTextWatcher watcher;

        ImageViewHolder(View view, CaptionTextWatcher watcher) {
            super(view);
            layout = view;
            mImageView = view.findViewById(R.id.image_view_main);
            mCaption = view.findViewById(R.id.caption);
            mTimestamp = view.findViewById(R.id.timestamp);
            mLocation = view.findViewById(R.id.location);

            this.watcher = watcher;
            mCaption.addTextChangedListener(watcher);
        }
    }

    /**
     * Listener for when a photo's caption is edited
     */
    private class CaptionTextWatcher implements TextWatcher {
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("image gallery", "Text at " + position + " changed to " + s.toString());
            imageDataList.get(position).setCaption(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}