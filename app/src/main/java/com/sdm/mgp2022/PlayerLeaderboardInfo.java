package com.sdm.mgp2022; //By Jiu Len

public class PlayerLeaderboardInfo {
    private String playerName;
    private int playerScore;

    public void SetPlayerName(String name)
    {
        playerName = name;
    }
    public void SetPlayerScore(int score)
    {
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
