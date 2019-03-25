package com.example.gamebacklog.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.gamebacklog.Game;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameRepository {

    private GameRoomDatabase mAppDatabase;
    private GameDao mGameDao;
    private LiveData<List<Game>> mGames;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public GameRepository (Context context) {
        mAppDatabase = GameRoomDatabase.getDatabase(context);
        mGameDao = mAppDatabase.gameDao();
        mGames = mGameDao.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return mGames;
    }

    public void insert(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.insertGame(game);
            }
        });
    }
    public void update(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.updateGame(game);
            }
        });
    }
    public void delete(final Game game) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.deleteGame(game);
            }
        });
    }

    public void deleteAll(final List<Game> games){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mGameDao.deleteAllGames(games);
            }
        });
    }
}
