package com.ristudios.personalagent.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Entry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {

    private final Activity context;
    private ArrayList<Entry> currentEntries;

    public EntryAdapter(Activity context) {
        this.context = context;
        this.currentEntries = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item_default, parent, false);
        return new EntryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EntryViewHolder holder, int position) {
        Entry entry = currentEntries.get(position);
        holder.bindView(entry);
    }

    public void setEntries(ArrayList<Entry> entries) {
        currentEntries = entries;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return currentEntries.size();
    }
}
