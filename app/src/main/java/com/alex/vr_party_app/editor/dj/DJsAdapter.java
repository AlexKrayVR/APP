package com.alex.vr_party_app.editor.dj;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.vr_party_app.databinding.DjsLayoutBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DJsAdapter extends RecyclerView.Adapter<DJsAdapter.DJsHolder> implements Filterable {

    private Context context;
    private List<DJ> djs;
    private List<DJ> djsSort;

    private Listener listener;

    Filter filter = new Filter() {
        //run back
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DJ> filtered = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(djs);
            } else {
                for (DJ dj : djs) {
                    if (dj.getNameDJ().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        Log.d("AlexDebug", "Filter - request string: " + dj.getNameDJ().toLowerCase());
                        filtered.add(dj);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;
            return filterResults;
        }

        //run ui
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            djsSort.clear();
            djsSort.addAll((Collection<? extends DJ>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public interface Listener {
        void onClick(DJ dj);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public DJsAdapter(Context context, List<DJ> djs) {
        this.context = context;
        this.djs = djs;
        djsSort = new ArrayList<>(djs);
    }

    @NonNull
    @Override
    public DJsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DJsAdapter.DJsHolder(DjsLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DJsHolder holder, final int position) {
        DJ dj = djsSort.get(position);

        holder.binding.dj.setText(dj.getNameDJ());

        holder.binding.dj.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(dj);
            }
        });
    }

    @Override
    public int getItemCount() {
        return djsSort == null ? 0 : djsSort.size();
    }

    public class DJsHolder extends RecyclerView.ViewHolder {
        DjsLayoutBinding binding;

        public DJsHolder(DjsLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
