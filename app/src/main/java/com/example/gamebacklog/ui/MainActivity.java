package com.example.gamebacklog.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gamebacklog.Game;
import com.example.gamebacklog.R;
import com.example.gamebacklog.database.GameRoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private List<Game> mGames;
    private GameAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private GestureDetector mGestureDetector;
    private GameRoomDatabase db;
    private MainViewModel mainViewModel;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static final int REQUESTCODEADD = 1234;
    public static final int REQUESTCODEEDIT = 4321;
    public static final String EDIT = "editoradd";
    public static final String EXTRA_GAME = "extragame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = GameRoomDatabase.getDatabase(this);
        mGames = new ArrayList<>();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                mGames = games;
                updateUI();
            }
        });

        initToolbar();
        initFloatingButton();
        initRecyclerView();

        if(mGames.size() == 0) {
            mainViewModel.insert(new Game("GTA", "PS4", "Stalled", "14-03-2019"));
            mainViewModel.insert(new Game("CoD", "PC", "Dropped", "01-02-2019"));
            mainViewModel.insert(new Game("Rocket League", "Xbox", "Playing", "05-12-2018"));
        }
        updateUI();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    private void initFloatingButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(EDIT, false);
                startActivityForResult(intent, REQUESTCODEADD);
            }
        });
    }
    private void initRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = (viewHolder.getAdapterPosition());
                final Game game = mGames.get(position);
                mainViewModel.delete(game);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.mainContent), "Deleted from list", Snackbar.LENGTH_LONG);

                snackbar.setAction("Undo",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainViewModel.insert(game);
                    }
                }).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(this);
    }

    public void updateUI() {
        if( mAdapter == null ) {
            mAdapter = new GameAdapter(mGames);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mGames);
        }
    }

    private void deleteAllGames(final List<Game> games) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mainViewModel.deleteAll(games);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == R.id.action_delete_all) {
            deleteAllGames(mGames);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (child != null) {
            int mAdapterPosition = recyclerView.getChildAdapterPosition(child);
            if(mGestureDetector.onTouchEvent(motionEvent)){
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(EDIT, true);
                intent.putExtra(EXTRA_GAME, mGames.get(mAdapterPosition));
                startActivityForResult(intent, REQUESTCODEEDIT);
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Game game = data.getParcelableExtra(EXTRA_GAME);
            if (requestCode == REQUESTCODEADD) {
                mainViewModel.insert(game);
            } else if (requestCode == REQUESTCODEEDIT) {
                mainViewModel.update(game);
            }
        }
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}