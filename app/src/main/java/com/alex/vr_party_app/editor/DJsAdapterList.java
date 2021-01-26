package com.alex.vr_party_app.editor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.vr_party_app.databinding.DjItemBinding;
import com.alex.vr_party_app.databinding.DjsLayoutBinding;
import com.alex.vr_party_app.editor.dj.DJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DJsAdapterList extends RecyclerView.Adapter<DJsAdapterList.DJsHolder> {

    private Context context;
    private List<DJ> djs;

    public DJsAdapterList(Context context) {
        this.context = context;
        this.djs = new ArrayList<>();
    }

    public List<DJ> getDjsList() {
        return djs;
    }

    public void clearDjsList() {
        djs.clear();
        notifyDataSetChanged();
    }

    public void addDj(DJ dj) {
        djs.add(dj);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DJsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DJsAdapterList.DJsHolder(DjItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DJsHolder holder, final int position) {
        DJ djName = djs.get(position);

        holder.binding.djName.setText(djName.getNameDJ());
        holder.binding.djHours.setText(djName.getHours());

        holder.binding.removeDJ.setOnClickListener(v -> {
            djs.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return djs == null ? 0 : djs.size();
    }

    public class DJsHolder extends RecyclerView.ViewHolder {
        DjItemBinding binding;

        public DJsHolder(DjItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
