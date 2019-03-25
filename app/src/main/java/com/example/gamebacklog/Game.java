package com.example.gamebacklog;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "game")
public class Game implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "gameName")
    private String gameName;

    @ColumnInfo(name = "gamePlatform")
    private String gamePlatform;

    @ColumnInfo(name = "gameStatus")
    private String gameStatus;

    @ColumnInfo(name = "date")
    private String gameEdited;

    @Override
    public String toString() {
        return "Game: " + this.getGameName();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGamePlatform() {
        return gamePlatform;
    }

    public void setGamePlatform(String gamePlatform) {
        this.gamePlatform = gamePlatform;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameEdited() {
        return gameEdited;
    }

    public void setGameEdited(String gameEdited) {
        this.gameEdited = gameEdited;
    }

    public void setAll(String name, String platform, String status, String editDate){
        this.gameName = name;
        this.gamePlatform = platform;
        this.gameStatus = status;
        this.gameEdited = editDate;
    }

    public enum STATUS {
        W("Want to play"),
        P("Playing"),
        S("Stalled"),
        D("Dropped");

        private final String status;

        STATUS(String text){
            this.status = text;
        }

        @TypeConverter
        public String statusToString() {
            return "";
        }
    }

    public Game(String gameName, String gamePlatform, String gameStatus, String gameEdited) {
        this.gameName = gameName;
        this.gamePlatform = gamePlatform;
        this.gameStatus = gameStatus;
        this.gameEdited = gameEdited;
    }

    protected Game(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        gameName = in.readString();
        gamePlatform = in.readString();
        gameStatus = in.readString();
        gameEdited = in.readString();
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(gameName);
        dest.writeString(gamePlatform);
        dest.writeString(gameStatus);
        dest.writeString(gameEdited);
    }
}
