package com.alex.vr_party_app.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.vr_party_app.databinding.CommunityItemBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class CommunityPartyAdapter extends RecyclerView.Adapter<CommunityPartyAdapter.ViewHolder> {

    static private ArrayList<CommunityPartyClass> listCommunity = new ArrayList<>();
    static private Context context;
    //private SliderPictureAdapter adapter;


    public CommunityPartyAdapter(Context context, ArrayList<CommunityPartyClass> listCommunity) {
        this.listCommunity = listCommunity;
        this.context = context;
    }

    @NonNull
    @Override
    public CommunityPartyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CommunityItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull CommunityPartyAdapter.ViewHolder holder, int position) {
        CommunityPartyClass community = listCommunity.get(position);
        holder.binding.recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        CommunityPartyPicturesListAdapter adapter = new CommunityPartyPicturesListAdapter(context, community);
        holder.binding.recycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return listCommunity.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CommunityItemBinding binding;

        public ViewHolder(CommunityItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class CircularTransformation implements Transformation {
        public CircularTransformation() {
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            final Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(output);
            canvas.drawCircle(source.getWidth() / 2, source.getHeight() / 2, source.getWidth() / 2, paint);

            if (source != output)
                source.recycle();

            return output;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


}









