package ca.bcit.c7098.photogalleryapp.ui;

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
import ca.bcit.c7098.photogalleryapp.data.Photo;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ImageViewHolder> {

    private List<Photo> imageDataList;


    public void setImageDataList(List<Photo> data) {
        imageDataList = data;
        notifyDataSetChanged();
    }

    ImageGalleryAdapter(List<Photo> data) {
        imageDataList = data;
    }

    @NonNull
    @Override
    public ImageGalleryAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGalleryAdapter.ImageViewHolder holder, int position) {
        // set what should be shown based on the position of the item
        Photo current = imageDataList.get(position);
        Bitmap bm = BitmapFactory.decodeFile(current.getPhotoPath());
        holder.mImageView.setImageBitmap(bm);
        holder.mCaption.setText(current.getCaption());
        holder.mTimestamp.setText(current.getDate());
        holder.mLocation.setText(current.getLocation());
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
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