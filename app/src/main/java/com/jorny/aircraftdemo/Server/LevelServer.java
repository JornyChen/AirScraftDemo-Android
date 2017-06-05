package com.jorny.aircraftdemo.Server;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月03
 * 
 */

public class LevelServer {
    private static LevelServer sServer;

    private LevelServer() {
    }

    public static synchronized LevelServer getInstance() {
        if (sServer == null) {
            sServer = new LevelServer();
        }
        return sServer;
    }

    public void UpgradeByScore(long score) {
        mLevel = (int) (score / 5000);//每5000分升一级
    }

    public int getLevel() {
        return mLevel;
    }

    private int mLevel = 0;

    public float getRunWeightByLevel() {
        float weight = 1f * mLevel;
        return weight;
    }

    public float getCreateEnemyWeightByLevel() {
        float weight = 1.5f * mLevel;
        return weight;
    }

    public void reset() {
        mLevel = 0;
    }
}
