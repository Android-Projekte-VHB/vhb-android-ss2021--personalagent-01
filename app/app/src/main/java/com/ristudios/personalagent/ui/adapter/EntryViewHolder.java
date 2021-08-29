package com.ristudios.personalagent.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Entry;
import com.ristudios.personalagent.utils.Utils;

import org.jetbrains.annotations.NotNull;

public class EntryViewHolder extends RecyclerView.ViewHolder {

    private TextView textView, txtTime;
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
        txtTime = itemView.findViewById(R.id.txt_viewholder_appointment_time);

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
                imageView.setWillNotDraw(false);
                imageView.setColorFilter(itemView.getResources().getColor(R.color.easy, null), PorterDuff.Mode.SRC_IN);
                txtTime.setText("");
                break;
            case MEDIUM:
                imageView.setWillNotDraw(false);
                imageView.setColorFilter(itemView.getResources().getColor(R.color.medium, null), PorterDuff.Mode.SRC_IN);
                txtTime.setText("");
                break;
            case HARD:
                imageView.setWillNotDraw(false);
                imageView.setColorFilter(itemView.getResources().getColor(R.color.hard, null), PorterDuff.Mode.SRC_IN);
                txtTime.setText("");
                break;
            case NONE:
                txtTime.setTextColor(Color.BLACK);
                txtTime.setText(Utils.getFormattedTime(Utils.getDateFromMillis(entry.getDate())));
                imageView.setWillNotDraw(true);
            default:
                break;
        }
    }
    public interface OnEntryClickedListener {
        void onEntryClicked(int position);
    }
}
