package com.alex.vr_party_app.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.ImageCardStaggeredGridBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageStaggeredGridAdapter extends RecyclerView.Adapter<ImageStaggeredGridAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<Upload> uploads;

    //private ArrayList<Bitmap> bitmaps;
    //Target target;
    //Bitmap tempBitmap = null;

    public ImageStaggeredGridAdapter(Context context, ArrayList<Upload> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(ImageCardStaggeredGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        Upload uploadCurrent = uploads.get(position);


//        mTarget=new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                if (bitmap == null) {
//                } else {
//                    mBitmaps.add(bitmap);
//                    holder.image_view.setImageBitmap(bitmap);
//                }
//            }
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                Log.d("AlexDebug", "failed");
//            }
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//            }
//        };
//       Picasso.with(mContext).load(uploadCurrent.getUrl()).resize(1000, 800).centerInside().placeholder(R.drawable.description).into(holder.image_view);
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .noPlaceholder()
                .resize(600, 400)
                .centerInside()
                .into(holder.binding.imageView);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageCardStaggeredGridBinding binding;

        public ImageViewHolder(ImageCardStaggeredGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                createDialogInfo(position);
            }
        }
    }

    private void createDialogInfo(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialog = inflater.inflate(R.layout.dialog_picture, null);
        final ImageView image = dialog.findViewById(R.id.image);
        builder.setView(dialog);
        Picasso.get()
                .load(uploads.get(position).getImageUrl())
                .noPlaceholder()
                .resize(800, 0)
                .centerCrop()
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        builder.show();
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
    }
}
