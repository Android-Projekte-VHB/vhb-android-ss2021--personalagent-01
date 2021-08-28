package com.ristudios.personalagent.data;

public enum Difficulty {
    NONE (0),
    EASY (10),
    MEDIUM (20),
    HARD (30);

    //points to be rewarded for fulfilling tasks
    public final int points;

    Difficulty(int points){
        this.points = points;
    }
}
