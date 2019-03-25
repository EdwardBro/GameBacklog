package com.example.gamebacklog.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.gamebacklog.database.GameRepository;
import com.example.gamebacklog.Game;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private GameRepository mRepository;
    private LiveData<List<Game>> mGames;

    public MainViewModel(@NonNull Application application){
        super(application);
        mRepository = new GameRepository(application.getApplicationContext());
        mGames = mRepository.getAllGames();
    }

    public LiveData<List<Game>> getGames() {
        return mGames;
    }

    public void insert(Game game){
        mRepository.insert(game);
    }
    public void update(Game game) {
        mRepository.update(game);
    }
    public void delete(Game game) {
        mRepository.delete(game);
    }
    public void deleteAll(List<Game> list) {
        mRepository.deleteAll(list);
    }
}
