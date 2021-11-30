package com.example.gamify;

import java.util.ArrayList;

public class Group {

    private ArrayList<String> people;
    private String game;

    public Group(String game) {
        people = new ArrayList<String>();
        this.game = game;
    }

    public Group() {

    }

    public String getGame() {
        return game;
    }

    public ArrayList<String> getPeople() {
        return people;
    }


}
