package com.jorny.aircraftdemo.model.EnemyAirCraft;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jorny.aircraftdemo.Factory.AirCraftFactory;
import com.jorny.aircraftdemo.Server.LevelServer;
import com.jorny.aircraftdemo.model.Bullet.Bullet;
import com.jorny.aircraftdemo.model.ExplosionPieces;
import com.jorny.aircraftdemo.model.LinearRunAirCraft;
import com.jorny.aircraftdemo.view.AirCraftDisPlayView;

import java.util.List;


public class EnemyAirCraft extends LinearRunAirCraft {
    public final static int ENEMY_TYPE_SPYPLANE = 0;
    public final static int ENEMY_TYPE_FIGHTERPLANE = 1;
    public final static int ENEMY_TYPE_CRUISERPLANE = 2;
    protected int mExtraSpeed;
    protected int mSeek = 1;
    protected int mMaxLife = 1;
    protected int mLife = 1;//生命
    protected int mScore = 0;//分数

    protected void initEnemyAirCraftInfo() {
        float runWeight = LevelServer.getInstance().getRunWeightByLevel();
        mExtraSpeed = (int) (runWeight * mSeek);
        mSpeed = mOriginalSpeed + mExtraSpeed;
        setLife(mMaxLife);//小敌机抗抵抗能力为1，即一颗子弹就可以销毁小敌机
        setSpeed(mSpeed);
        setScore(mScore);
    }

    public EnemyAirCraft(Bitmap bitmap) {
        super(bitmap);
    }

    public int getLife() {
        return mLife;
    }

    public void setLife(int life) {
        this.mLife = life;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int mScore) {
        this.mScore = mScore;
    }

    @Override
    public void onAfterDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view) {
        super.onAfterDraw(canvas, paint, view);
        //绘制完成后要检查自身是否被子弹打中
        if (!isDestroyed()) {
            //敌机在绘制完成后要判断是否被子弹打中

            List<Bullet> bullets = view.getAliveBullets();
            for (Bullet bullet : bullets) {
                //判断敌机是否与子弹相交
                boolean hsCollide = hasCollideWithOther(bullet);
                if (hsCollide) {
                    bullet.destroy();
                    mLife = mLife - bullet.getPower();
                    if (mLife <= 0) {
                        explode(view);
                        return;
                    }
                }
            }
        }
    }

    //创建爆炸效果
    public void explode(AirCraftDisPlayView disPlayView) {
        float centerX = getX() + getWidth() / 2;
        float centerY = getY() + getHeight() / 2;
        ExplosionPieces explosion = (ExplosionPieces) AirCraftFactory.getInstance().getAirCraft(ExplosionPieces.TAG);
        explosion.centerTo(centerX, centerY);
        disPlayView.addAirCraftToBuffer(explosion);
        destroy();
        disPlayView.addScore(mScore);
    }


    @Override
    public void reset() {
        super.reset();
        setLife(mMaxLife);
        initEnemyAirCraftInfo();
    }
}
