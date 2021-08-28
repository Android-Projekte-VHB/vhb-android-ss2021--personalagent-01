package com.ristudios.personalagent.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Entry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> implements EntryViewHolder.OnEntryClickedListener {

    private final Activity context;
    private ArrayList<Entry> currentEntries;
    private OnEntryEditedListener listener;

    public EntryAdapter(Activity context, OnEntryEditedListener listener) {
        this.context = context;
        this.listener = listener;
        this.currentEntries = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item_default, parent, false);
        return new EntryViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EntryViewHolder holder, int position) {
        Entry entry = currentEntries.get(position);
        holder.bindView(entry);
    }

    public void updateEntries(ArrayList<Entry> entries) {
        currentEntries.clear();
        currentEntries.addAll(entries);
    }

    @Override
    public int getItemCount() {
        return currentEntries.size();
    }

    @Override
    public void onEntryClicked(int position) {
        listener.onEntryEdited(currentEntries.get(position), position);
    }

    public interface OnEntryEditedListener {
        void onEntryEdited(Entry entry, int position);
    }
}
