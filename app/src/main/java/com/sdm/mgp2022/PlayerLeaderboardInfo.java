package com.sdm.mgp2022; //By Jiu Len

public class PlayerLeaderboardInfo {
    private String playerName;
    private int playerScore;

    public PlayerLeaderboardInfo(String name, int score)
    {
        playerName = name;
        playerScore = score;
    }

    public String GetPlayerName()
    {
        return playerName;
    }
    public int GetPlayerScore()
    {
        return playerScore;
    }
}
