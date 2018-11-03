package com.example.almir.users;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<User> users = new ArrayList<>();
    private Listener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        CardView cardView = holder.cardView;

        TextView name = cardView.findViewById(R.id.text_card_name);
        TextView email = cardView.findViewById(R.id.text_card_email);
        TextView isActive = cardView.findViewById(R.id.text_card_isActive);

        name.setText(users.get(position).getName());
        email.setText(users.get(position).getEmail());
        if (users.get(position).isActive()) {
            isActive.setText("ACTIVE");
            isActive.setTextColor(Color.RED);
        } else {
            isActive.setText("inactive");
            isActive.setTextColor(Color.GRAY);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClick(users.get(position).getId());
            }
        });
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface Listener {
        void onClick(int id);
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }
}