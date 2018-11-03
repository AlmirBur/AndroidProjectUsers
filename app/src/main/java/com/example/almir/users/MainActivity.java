package com.example.almir.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    UsersDatabaseHelper usersDatabaseHelper;
    RecyclerView recyclerView;
    CardAdapter adapter;
    Toolbar toolbar;
    static List<User> users = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update: {
                updateUsers();
                break;
            }
            default: {
                Toast.makeText(getApplicationContext(), "Неизвестная кнопка",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        initList();

        usersDatabaseHelper = new UsersDatabaseHelper(getApplicationContext());
        if (usersDatabaseHelper.databaseIsEmpty()) {
            UpdateDatabaseTask updateDatabaseTask = new UpdateDatabaseTask();
            updateDatabaseTask.execute();
            try {
                updateDatabaseTask.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            users = usersDatabaseHelper.getUsers();
            adapter.setUsers(users);
            adapter.notifyDataSetChanged();
        }
    }

    void initList() {
        recyclerView = findViewById(R.id.list_recycler);
        adapter = new CardAdapter();
        adapter.setUsers(users);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setListener(new CardAdapter.Listener() {
            @Override
            public void onClick(int id) {
                showDetails(id);
            }
        });
    }

    public void showDetails(int id) {
        User user = usersDatabaseHelper.getUser(id);
        if (user == null) {
            Toast.makeText(getApplicationContext(), "Такого профиля нет",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.isActive()) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), user.getName() + " неактивный",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void updateUsers() {
        recyclerView.setVisibility(View.INVISIBLE);
        ActionMenuItemView item = findViewById(R.id.update);
        item.setEnabled(false);
        UpdateDatabaseTask task = new UpdateDatabaseTask();
        task.execute();
    }

    void updated() {
        users = usersDatabaseHelper.getUsers();
        adapter.notifyDataSetChanged();
        ActionMenuItemView item = findViewById(R.id.update);
        item.setEnabled(true);
        recyclerView.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), "Обновлено",
                Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("StaticFieldLeak")//подсказка IDE
    class UpdateDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... v) {
            usersDatabaseHelper.updateDatabase();
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            updated();
        }
    }
}