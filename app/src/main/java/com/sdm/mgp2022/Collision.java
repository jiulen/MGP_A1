package com.sdm.mgp2022;

// Created by TanSiewLan2021

public class Collision {

    public static boolean SphereToSphere(float x1, float y1, float radius1, float x2, float y2, float radius2)
    {
        float xVec = x2 - x1;
        float yVec = y2 - y1;

        float distSquared = xVec * xVec + yVec * yVec;

        float rSquared = radius1 + radius2;
        rSquared *= rSquared;

        if (distSquared > rSquared)
            return false;

        return true;
    }
}
