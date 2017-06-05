package com.jorny.aircraftdemo.model;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.jorny.aircraftdemo.Factory.AirCraftFactory;
import com.jorny.aircraftdemo.model.Bullet.Bullet;
import com.jorny.aircraftdemo.model.Bullet.NormalBullet;
import com.jorny.aircraftdemo.model.Bullet.SuperBullet;
import com.jorny.aircraftdemo.model.EnemyAirCraft.EnemyAirCraft;
import com.jorny.aircraftdemo.model.Reward.NormalButtleReward;
import com.jorny.aircraftdemo.view.AirCraftDisPlayView;
import java.util.List;
public class PlayerAirCraft extends BaseAirCraft {
    public static final String TAG = "PlayerAirCraft";
    private static final int MAX_EXPLOED_TIME = 90;
    private static final int MAX_BULLET_COUNT = 100;
    private int mBulletCount = 0;
    private boolean isCollide;
    private long mBeginisExplodeFrame;
    private int mTouchOffSet = 20;
    private int mPower = 1;
    private boolean isBulleInfinite;
    public PlayerAirCraft(Bitmap bitmap) {
        super(bitmap);
    }
    private int mBulletType = BULLE_TYPE_NORMAL;
    public final static int BULLE_TYPE_NORMAL = 1;
    public final static int BULLE_TYPE_SUPERggit  = 2;
    @Override
    public void onBeforeDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view) {
        if (!isDestroyed() && isVisable) {
            validatePosition(canvas);
            //每隔20帧发射子弹
            if (getFrameIndex() % 10 == 0) {
                fight(view);
            }
        }
    }
    //发射子弹
    private void fight(AirCraftDisPlayView disPlayView) {
        //如果战斗机被撞击了或销毁了，那么不会发射子弹
        if (isDestroyed()) {
            return;
        }
        float x = getX() + getWidth() / 2;
        float y = getY() - 5;
        Bullet bullet;
        switch (mBulletType) {
            case BULLE_TYPE_NORMAL:
                switch (mPower) {
                    case 1:
                        fightSingleLineBullet(disPlayView, x, y);
                        break;
                    case 2:
                        fightTwoLineBullet(disPlayView, x, y);
                        if (!isBulleInfinite) {
                            mBulletCount--;
                        }
                        break;
                    default:
                        // mPower >=3
                        fightTwoLineBullet(disPlayView, x, y);
                        fightSingleLineBullet(disPlayView, x, y);
                        if (!isBulleInfinite) {
                            mBulletCount--;
                        }
                        break;
                }
                break;
            case BULLE_TYPE_SUPER:
                //其他路径单路就暂时不实现了
                bullet = (Bullet) AirCraftFactory.getInstance().getAirCraft(SuperBullet.TAG);
                bullet.moveTo(x, y);
                disPlayView.addAirCraftToBuffer(bullet);
                break;
        }
        if (mBulletCount <= 0) {
            if (mPower > 1) {
                mPower--;
                mBulletCount = MAX_BULLET_COUNT;
            } else {
                mPower = 1;
                mBulletCount = 0;
            }
        }
    }

    @NonNull
    private void fightSingleLineBullet(AirCraftDisPlayView disPlayView, float x, float y) {
        Bullet bullet;
        bullet = (Bullet) AirCraftFactory.getInstance().getAirCraft(NormalBullet.TAG);
        bullet.moveTo(x, y);
        disPlayView.addAirCraftToBuffer(bullet);
    }

    private void fightTwoLineBullet(AirCraftDisPlayView disPlayView, float x, float y) {
        float offset;
        float leftX;
        float rightX;
        offset = getWidth() / 4;
        leftX = x - offset;
        rightX = x + offset;
        Bullet leftBullet = (Bullet) AirCraftFactory.getInstance().getAirCraft(NormalBullet.TAG);
        leftBullet.moveTo(leftX, y);
        disPlayView.addAirCraftToBuffer(leftBullet);
        Bullet rightBullet = (Bullet) AirCraftFactory.getInstance().getAirCraft(NormalBullet.TAG);
        rightBullet.moveTo(rightX, y);
        disPlayView.addAirCraftToBuffer(rightBullet);
    }

    @Override
    public void onAfterDraw(Canvas canvas, Paint paint, AirCraftDisPlayView view) {
        if (isDestroyed()) {
            return;
        }
        //在飞机当前还没有被击中时，要判断是否将要被敌机击中
        if (!isCollide) {
            List<EnemyAirCraft> enemies = view.getAllEnemyAirCrafts();
            for (EnemyAirCraft enemyScraft : enemies) {
                if (hasCollideWithOther(enemyScraft)) {
                    explode(view);
                    isVisable = false;
//                    isExploding=true;
                    mBeginisExplodeFrame = getFrameIndex();
                    break;
                }
            }

        } else {
            if (mBeginisExplodeFrame > 0) {
                long frame = getFrameIndex();
                if (frame > mBeginisExplodeFrame) {
                    if (frame - mBeginisExplodeFrame >= MAX_EXPLOED_TIME) {
                        destroy();

                    }
                }
            }
        }
        if (!isCollide) {
            //检查是否获得子弹道具
            List<NormalButtleReward> bulletAwards = view.getAliveBulletReward();
            for (NormalButtleReward bulletAward : bulletAwards) {
                boolean hasCollide = hasCollideWithOther(bulletAward);
                if (hasCollide) {
                    bulletAward.destroy();
                    if (mPower < 3) {
                        mPower++;
                    } else {
                        mPower = 3;
                    }
                    mBulletCount = MAX_BULLET_COUNT;
                }
            }
        }
    }

    //确保plane在范围内
    private void validatePosition(Canvas canvas) {
        if (getX() < 0) {
            setX(0);
        }
        if (getY() < 0) {
            setY(0);
        }
        RectF rectF = getRectF();
        int canvasWidth = canvas.getWidth();
        if (rectF.right > canvasWidth) {
            setX(canvasWidth - getWidth());
        }
        int canvasHeight = canvas.getHeight();
        if (rectF.bottom > canvasHeight) {
            setY(canvasHeight - getHeight());
        }
    }

    @Override
    protected void explode(AirCraftDisPlayView disPlayView) {
        if (!isCollide) { //被打中了就不需要再次爆炸。
            isCollide = true;
            float centerX = getX() + getWidth() / 2;
            float centerY = getY() + getHeight() / 2;
            ExplosionPieces explosion = (ExplosionPieces) AirCraftFactory.getInstance().getAirCraft(ExplosionPieces.TAG);
            explosion.centerTo(centerX, centerY);
            disPlayView.addAirCraftToBuffer(explosion);
        }
    }

    public boolean isPointInPlayAirCraft(float x, float y) {
        return getTouchRectF().contains(x, y);
    }

    public RectF getTouchRectF() {
        float left = x - mTouchOffSet;
        float top = y - mTouchOffSet;
        float right = left + getWidth() + mTouchOffSet;
        float bottom = top + getHeight() + mTouchOffSet;
        RectF rectF = new RectF(left, top, right, bottom);
        return rectF;
    }

    @Override
    public void reset() {
        super.reset();
        mPower = 1;
        mBulletCount = 0;
    }


    public void setBulleInfinite(boolean bulleInfinite) {
        this.isBulleInfinite = bulleInfinite;

    }
}
