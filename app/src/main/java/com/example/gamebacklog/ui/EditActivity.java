package com.example.gamebacklog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gamebacklog.Game;
import com.example.gamebacklog.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextView textViewTitle, textViewPlatform;
    private boolean editingMode;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.spinner);
        textViewTitle = findViewById(R.id.editTextTitle);
        textViewPlatform = findViewById(R.id.editTextPlatform);

        String[] items = new String[]{"Want to play", "Playing", "Stalled", "Dropped"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        Intent inIntent = getIntent();
        editingMode = inIntent.getBooleanExtra(MainActivity.EDIT, false);
        if(editingMode){
            game = inIntent.getParcelableExtra(MainActivity.EXTRA_GAME);
            textViewTitle.setText(game.getGameName());
            textViewPlatform.setText(game.getGamePlatform());
            spinner.setSelection(adapter.getPosition(game.getGameStatus()));
            getSupportActionBar().setTitle(R.string.title_edit);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textTitle = textViewTitle.getText().toString();
                String textPlatform = textViewPlatform.getText().toString();
                String textStatus = spinner.getSelectedItem().toString();
                if(TextUtils.isEmpty(textTitle)){
                    Snackbar.make(view, "Please set a name for the game", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if(TextUtils.isEmpty(textPlatform)){
                    Snackbar.make(view, "Please set a platform for the game", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if(editingMode){
                        game.setAll(textTitle, textPlatform, textStatus, new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    } else {
                        game = new Game(textTitle, textPlatform, textStatus, new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                    }
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.EDIT, editingMode);
                    resultIntent.putExtra(MainActivity.EXTRA_GAME, game);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

            }
        });
    }

}
