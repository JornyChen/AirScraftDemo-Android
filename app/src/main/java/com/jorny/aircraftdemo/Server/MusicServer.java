package com.jorny.aircraftdemo.Server;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月04
 * 
 */

import android.content.Context;
import android.media.MediaPlayer;

import com.jorny.aircraftdemo.ApplycationContext;
import com.jorny.aircraftdemo.R;

import java.io.IOException;

public class MusicServer {

        //最好使用Handler进行操作 暂时不进行修改。
    private MediaPlayer mMediaPlayer;
    Context mContext;
    private boolean isStop = true;

    public MusicServer() {
        mContext = ApplycationContext.sContext;
    }


    public void play() {

        if (mMediaPlayer == null) {
            try {
                mMediaPlayer = MediaPlayer.create(mContext, R.raw.bgmusic);//重新设置要播放的音频
                mMediaPlayer.setVolume(0.5f, 0.5f);
                mMediaPlayer.setLooping(true);
                isStop = false;
                mMediaPlayer.start();//开始播放
            } catch (Exception e) {
                e.printStackTrace();//输出异常信息
            }
        } else {
            if (isStop) {
                try {
                    isStop = false;
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    isStop = true;
                }
            }
            mMediaPlayer.start();//开始播放
        }
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }


    public void stop() {
        isStop = true;
        mMediaPlayer.stop();
    }

    /**
     * 释放播放器资源
     */
    public void releasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
