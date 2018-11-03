package com.example.almir.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    UsersDatabaseHelper usersDatabaseHelper;
    User user;
    List<User> friends;
    Toolbar toolbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        usersDatabaseHelper = new UsersDatabaseHelper(getApplicationContext());
        init();
    }

    private List<User> getFriends() {
        List<User> friends = new ArrayList<>();
        for (int id : user.getFriends()) friends.add(usersDatabaseHelper.getUser(id));
        return friends;
    }

    private User getUser() {
        return usersDatabaseHelper.getUser(getIntent().getIntExtra("id", -1));
    }

    public void showPhone(View v) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.getPhone())));
    }

    public void showEmail(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", user.getEmail(), null));
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    public void showLocation(View v) {
        @SuppressLint("DefaultLocale")
        String uri = String.format("geo:%f,%f", user.getLatitude(), user.getLongitude());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    public void showFriend(int id) {
        User friend = usersDatabaseHelper.getUser(id);
        if (friend == null) {
            Toast.makeText(getApplicationContext(), "Такого профиля нет",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (friend.isActive()) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), friend.getName() + " неактивный",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void showEmptyView() {
        setContentView(new View(getApplicationContext()));
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        user = getUser();
        if (user == null) {
            showEmptyView();
            return;
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.details));
        RecyclerView recyclerView = findViewById(R.id.list_recycler_friends);
        friends = getFriends();
        CardAdapter adapter = new CardAdapter();
        adapter.setUsers(friends);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setListener(new CardAdapter.Listener() {
            @Override
            public void onClick(int id) {
                showFriend(id);
            }
        });

        TextView name = findViewById(R.id.text_name);
        TextView age = findViewById(R.id.text_age);
        TextView company = findViewById(R.id.text_company);
        TextView email = findViewById(R.id.text_email);
        TextView phone = findViewById(R.id.text_phone);
        TextView address = findViewById(R.id.text_address);
        TextView about = findViewById(R.id.text_about);
        ImageView eye = findViewById(R.id.image_eye);
        ImageView fruit = findViewById(R.id.image_fruit);
        TextView registered = findViewById(R.id.text_registered);
        TextView location = findViewById(R.id.text_location);

        name.setText(user.getName());
        age.setText(String.valueOf(user.getAge()) + " y.o.");
        email.setText(user.getEmail());
        company.setText(getString(R.string.company) + " " + user.getCompany());
        phone.setText(user.getPhone());
        address.setText(user.getAddress());
        about.setText(user.getAbout());
        registered.setText(getString(R.string.registered) + " " + user.getRegistered());
        location.setText(getString(R.string.location) + " " + user.getLatitude() + ", " + user.getLongitude());
        switch (user.getEyeColor()) {
            case BLUE: {
                eye.setBackground(getDrawable(R.drawable.blue_circle));
                break;
            }
            case GREEN: {
                eye.setBackground(getDrawable(R.drawable.green_circle));
                break;
            }
            case BROWN: {
                eye.setBackground(getDrawable(R.drawable.brown_circle));
                break;
            }
        }
        switch (user.getFavoriteFruit()) {
            case APPLE: {
                fruit.setImageResource(R.drawable.apple);
                break;
            }
            case BANANA: {
                fruit.setImageResource(R.drawable.banana);
                break;
            }
            case STRAWBERRY: {
                fruit.setImageResource(R.drawable.strawberry);
                break;
            }
        }
    }
}
