package ca.bcit.c7098.photogalleryapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.bcit.c7098.photogalleryapp.R;
import ca.bcit.c7098.photogalleryapp.Utilities;
import ca.bcit.c7098.photogalleryapp.data.Photo;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ImageViewHolder> {

    private List<Photo> imageDataList;
    private final LayoutInflater inflater;

    public void setData(List<Photo> data) {
        imageDataList = data;
        notifyDataSetChanged();
    }

    ImageGalleryAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ImageGalleryAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.image_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGalleryAdapter.ImageViewHolder holder, int position) {
        // set what should be shown based on the position of the item
        if (imageDataList != null) {
            int i = position;
            Photo current = imageDataList.get(i);
            // TODO: Find out where else to put this call

            Bitmap bm = Utilities.getThumbnailFromPath(holder.mImageView, current.getPhotoPath());
            holder.mImageView.setImageBitmap(bm);
            holder.mCaption.setText(current.getCaption());
            holder.mTimestamp.setText(current.getDate());
            holder.mLocation.setText(current.getLocation());
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
        ImageViewHolder(View view) {
            super(view);
            layout = view;
            mImageView = view.findViewById(R.id.image_view_main);
            mCaption = view.findViewById(R.id.caption);
            mTimestamp = view.findViewById(R.id.timestamp);
            mLocation = view.findViewById(R.id.location);
        }
    }
}