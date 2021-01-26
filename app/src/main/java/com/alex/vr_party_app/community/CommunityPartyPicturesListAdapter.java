package com.alex.vr_party_app.community;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.vr_party_app.R;
import com.alex.vr_party_app.databinding.CommunityItemBinding;
import com.alex.vr_party_app.databinding.CommunityListPicturesItemBinding;
import com.alex.vr_party_app.databinding.HeaderCommunityBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommunityPartyPicturesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private CommunityPartyClass communityPartyClass;

    public CommunityPartyPicturesListAdapter(Context context, CommunityPartyClass communityPartyClass) {
        this.context = context;
        this.communityPartyClass = communityPartyClass;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new CommunityPartyPicturesListAdapter.HeaderViewHolder(HeaderCommunityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new CommunityPartyPicturesListAdapter.ItemViewHolder(CommunityListPicturesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Integer community = listPictures.get(position);
//        Picasso.get().load(community)
//                .resize(600, 0)
//                .centerCrop()
//                .into(holder.binding.image);
//        holder.binding.image.setOnClickListener(v->{
//            createDialogInfo(community);
//        });

        if (holder instanceof CommunityPartyPicturesListAdapter.HeaderViewHolder) {
            Log.d("AlexDebug", "HeaderViewHolder");
            Picasso.get()
                    .load(communityPartyClass.getIcon())
                    .resize(200, 200)
                    .centerCrop()
                    .transform(new CommunityPartyAdapter.CircularTransformation())
                    .into(((CommunityPartyPicturesListAdapter.HeaderViewHolder) holder).binding.iconCommunity);
            ((CommunityPartyPicturesListAdapter.HeaderViewHolder) holder).binding.communityName.setText(communityPartyClass.getName());
            ((CommunityPartyPicturesListAdapter.HeaderViewHolder) holder).binding.discordLink.setOnClickListener(v -> {
                if (!communityPartyClass.getLink().isEmpty()) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(communityPartyClass.getLink())));
                }
            });
        } else {
            ((ItemViewHolder) holder).binding.image.setOnClickListener(v->{
                createDialogInfo(communityPartyClass.getListOfPictures().get(position-1));
            });
            Picasso.get()
                    .load(communityPartyClass.getListOfPictures().get(position-1))
                    .resize(400, 0)
                    .centerCrop()
                    .transform(new CommunityPartyAdapter.CircularTransformation())
                    .into(((ItemViewHolder) holder).binding.image);
        }
    }

    private void createDialogInfo(Integer community) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialog = inflater.inflate(R.layout.dialog_picture, null);
        final ImageView image = dialog.findViewById(R.id.image);
        builder.setView(dialog);
        Picasso.get()
                .load(community)
                .noPlaceholder()
                .resize(1000, 0)
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


    @Override
    public int getItemCount() {
        return communityPartyClass.getListOfPictures().size() + 1;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderCommunityBinding binding;

        public HeaderViewHolder(HeaderCommunityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        public CommunityListPicturesItemBinding binding;

        public ItemViewHolder(CommunityListPicturesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}
