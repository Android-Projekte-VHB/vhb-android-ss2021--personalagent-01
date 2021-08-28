package com.ristudios.personalagent.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Entry;

import org.jetbrains.annotations.NotNull;

public class EntryViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;
    private ImageView imageView;
    private ConstraintLayout constraint;
    private OnEntryClickedListener listener;

    @SuppressLint("UseCompatLoadingForDrawables")
    public EntryViewHolder(@NonNull @NotNull View itemView, OnEntryClickedListener listener) {
        super(itemView);
        this.listener = listener;
        textView = itemView.findViewById(R.id.entry_text);
        imageView = itemView.findViewById(R.id.difficulty_indicator);
        constraint = itemView.findViewById(R.id.entry_constraint);

    }

    public void bindView(Entry entry) {
        textView.setText(entry.getName());
        imageView.setImageResource(R.drawable.android_mascot_30dp);
        setBackgroundForCategory(entry);
        setDifficultyIndicator(entry);
        constraint.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onEntryClicked(getAdapterPosition());
                return false;
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setBackgroundForCategory(Entry entry) {

        switch(entry.getCategory()) {
            case WORK:
                constraint.setBackground(itemView.getResources().getDrawable(R.drawable.background_gradient_work, null));
                break;
            case HOBBY:
                constraint.setBackground(itemView.getResources().getDrawable(R.drawable.background_gradient_hobby, null));
                break;
            case FITNESS:
                constraint.setBackground(itemView.getResources().getDrawable(R.drawable.background_gradient_fitness, null));
                break;
            case APPOINTMENT:
                constraint.setBackground(itemView.getResources().getDrawable(R.drawable.background_gradient_appointment, null));
                break;
            default:
                break;
        }
    }

    private void setDifficultyIndicator(Entry entry) {
        switch (entry.getDifficulty()) {
            case EASY:
                imageView.setColorFilter(itemView.getResources().getColor(R.color.easy, null), PorterDuff.Mode.SRC_IN);
                break;
            case MEDIUM:
                imageView.setColorFilter(itemView.getResources().getColor(R.color.medium, null), PorterDuff.Mode.SRC_IN);
                break;
            case HARD:
                imageView.setColorFilter(itemView.getResources().getColor(R.color.hard, null), PorterDuff.Mode.SRC_IN);
                break;
            default:
                break;
        }
    }
    public interface OnEntryClickedListener {
        void onEntryClicked(int position);
    }
}
