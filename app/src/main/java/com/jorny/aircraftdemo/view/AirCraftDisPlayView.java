package com.jorny.aircraftdemo.view;/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jorny.aircraftdemo.Dialog.AirCraftDialogBuilder;
import com.jorny.aircraftdemo.Dialog.AirCraftDialogBuilderInfo;
import com.jorny.aircraftdemo.Dialog.AndroidNativeDialogBuilder;
import com.jorny.aircraftdemo.Dialog.CanvasDialogBuilder;
import com.jorny.aircraftdemo.Factory.AirCraftFactory;
import com.jorny.aircraftdemo.Server.LevelServer;
import com.jorny.aircraftdemo.R;
import com.jorny.aircraftdemo.model.*;
import com.jorny.aircraftdemo.model.Bullet.Bullet;
import com.jorny.aircraftdemo.model.EnemyAirCraft.CruiserPlane;
import com.jorny.aircraftdemo.model.EnemyAirCraft.EnemyAirCraft;
import com.jorny.aircraftdemo.model.EnemyAirCraft.FighterPlane;
import com.jorny.aircraftdemo.model.EnemyAirCraft.SpyPlane;
import com.jorny.aircraftdemo.model.Reward.NormalButtleReward;
import com.jorny.aircraftdemo.utils.DisPlayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class AirCraftDisPlayView extends View {

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mScorePaint;
    private float mTouchX;
    private float mTouchY;
    private PlayerAirCraft mPlayerAircraft = null;
    private int mFrameIndex;
    private List<BaseAirCraft> mAirScraftsBuffer = new ArrayList<>();
    private List<BaseAirCraft> mAirScrafts = new ArrayList<>();
    List<EnemyAirCraft> mEnemyAirScraft = new ArrayList<>();
    private float mDensity = -1F;//屏幕密度
    private int mFps = -1;
    private long mPreDrawTime = 0L;
    private int mScore = 0;
    public static final int STATUS_GAME_STARTED = 1;//开始
    public static final int STATUS_GAME_PAUSED = 2;//暂停
    public static final int STATUS_GAME_OVER = 3;//结束
    private int mGameStatus = STATUS_GAME_STARTED;
    private float mTextfontSize;
    private float mScorefontSize;
    private LevelServer mLevelServer;
    private int mCreateCount;
    public final static int DIALOG_TYPE_NATIVE = 1;
    public final static int DIALOG_TYPE_CANVAS = 2;
    private static final int CLICK_DURATION_TIME = 1000;
    private boolean isBulleInfinite=false;


    public GameStatusChangeListener getGameStatusChangeListener() {
        return mGameStatusChangeListener;
    }

    public void setGameStatusChangeListener(GameStatusChangeListener mGameStatusChangeListener) {
        this.mGameStatusChangeListener = mGameStatusChangeListener;
    }

    private GameStatusChangeListener mGameStatusChangeListener;
    public void setDialogType(int mDialogType) {
        this.mDialogType = mDialogType;
    }

    private int mDialogType = DIALOG_TYPE_CANVAS;
    private long mTouchDownTime;
    private long mTouchUpTime;
    private AirCraftDialogBuilder mDialogBuilder;
    private Bitmap mPauseBitmap;

    public AirCraftDisPlayView(Context context) {
        super(context);
        init();
    }

    public AirCraftDisPlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mGameStatus) {
            case STATUS_GAME_OVER:
                showGameOverOrPauseDialog(canvas);
                resetAllResources();
                break;
            case STATUS_GAME_STARTED:
                drawGame(canvas);
                break;
            case STATUS_GAME_PAUSED:
                showGameOverOrPauseDialog(canvas);
                break;
        }
    }

    private void init() {
        mLevelServer = LevelServer.getInstance();
        mDensity = getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        mTextPaint.setColor(0x88000000);
        mTextfontSize = DisPlayUtils.dip2px(getContext(), 12f);
        mTextPaint.setTextSize(mTextfontSize);
        mScorePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        mScorePaint.setColor(0x88000000);
        mScorefontSize = DisPlayUtils.dip2px(getContext(), 16f);
        mScorePaint.setTextSize(mScorefontSize);
        mPauseBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_pause);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTouchX = event.getX();
        mTouchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //触点按下
                mTouchDownTime = System.currentTimeMillis();

                return true;
            case MotionEvent.ACTION_MOVE:

                if (mGameStatus == STATUS_GAME_STARTED) {
                    if (mPlayerAircraft != null) {
                        if (mPlayerAircraft.isPointInPlayAirCraft(mTouchX, mTouchY)) {
                            mPlayerAircraft.centerTo(mTouchX, mTouchY);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //触点弹起
                mTouchUpTime = System.currentTimeMillis();
                //计算触点按下到触点弹起之间的时间差
                long downUpDurationTime = mTouchUpTime - mTouchDownTime;
                if (downUpDurationTime < CLICK_DURATION_TIME) {
                    switch (mGameStatus) {
                        case STATUS_GAME_OVER:
                        case STATUS_GAME_PAUSED:
                            if (mDialogBuilder != null) {
                                if (mDialogBuilder.isClickPostiveButton(mTouchX, mTouchY)) {
                                    mDialogBuilder.clickPostiveButton();
                                }
                            }
                            break;
                        case STATUS_GAME_STARTED:
                            if (isClickPause(mTouchX, mTouchY)) {
                                gamePause();
                            }
                            break;

                    }
                }
                break;
        }


        return super.onTouchEvent(event);
    }


    public void startByReady() {
        if (mPlayerAircraft == null) {
            mPlayerAircraft = (PlayerAirCraft) AirCraftFactory.getInstance().getAirCraft(PlayerAirCraft.TAG);
            mPlayerAircraft.setBulleInfinite(isBulleInfinite);

        }
        changeGameStatus(STATUS_GAME_STARTED);
        postInvalidate();
    }


    public void gameOver() {

        changeGameStatus(STATUS_GAME_OVER);
    }

    //绘制暂停状态的游戏
    private void gamePause( ) {
        changeGameStatus(STATUS_GAME_PAUSED);
    }

    private void showGameOverOrPauseDialog(@Nullable Canvas canvas) {
        String message = String.format("您闯到了第%d关，获得了%d分", mLevelServer.getLevel()+1,mScore);
        final boolean isGameOver= mGameStatus ==STATUS_GAME_OVER;
        String positiveBtnText;
        String title;
        if(isGameOver){
            positiveBtnText="再来一局";
            title="游戏结束";
        }else {
            positiveBtnText="继续";
            title="游戏暂停";
        }
        AirCraftDialogBuilder.PostiveClickListener positListener = new AirCraftDialogBuilder.PostiveClickListener() {
            @Override
            public void onClick() {
                if (isGameOver) {
                    restart();
                } else {
                    resume();
                }
            }
        };
        AirCraftDialogBuilderInfo dialogDisPlayVo = new AirCraftDialogBuilderInfo();
        dialogDisPlayVo.setTitle(title);
        dialogDisPlayVo.setMessage(message);
        dialogDisPlayVo.setPostiveText(positiveBtnText);
        dialogDisPlayVo.setListener(positListener);
        if (mDialogType == DIALOG_TYPE_NATIVE) {
            mDialogBuilder = new AndroidNativeDialogBuilder();
        } else {
            mDialogBuilder = new CanvasDialogBuilder(getContext());
        }
        mDialogBuilder.ShowAlertDialog(getContext(), canvas, dialogDisPlayVo);
    }

    //绘制运行状态的游戏
    private void drawGame(Canvas canvas) {
        //第一次绘制时，将战斗机移到Canvas最下方，在水平方向的中心
        if (mFrameIndex == 0) {
            float centerX = canvas.getWidth() / 2;
            float centerY = canvas.getHeight() - mPlayerAircraft.getHeight() / 2;
            mPlayerAircraft.centerTo(centerX, centerY);
        }
        //将spritesNeedAdded添加到sprites中
        if (!mAirScraftsBuffer.isEmpty()) {
            mAirScrafts.addAll(mAirScraftsBuffer);
            mAirScraftsBuffer.clear();
        }
        //检查战斗机跑到子弹前面的情况
        destroyBulletsFrontOfPlayerAircraft();
        //在绘制之前先移除掉已经被destroyed的Sprite
        removeDestroyedSprites();
        //每隔15帧随机添加1架敌机
        if (mFrameIndex % 15 == 0) {
            mCreateCount++;
            createEnemyAirCraftByRandom(canvas.getWidth());
            if (mCreateCount % 50 == 0) {
                //每生产50架飞机就来一发子弹升级包
                createNormalButtleReward(canvas.getWidth());
            }
        }
        //遍历sprites，绘制敌机、子弹、奖励、爆炸效果
        Iterator<BaseAirCraft> iterator = mAirScrafts.iterator();
        while (iterator.hasNext()) {
            BaseAirCraft s = iterator.next();
            if (!s.isDestroyed()) {
                //在Sprite的draw方法内有可能会调用destroy方法
                s.draw(canvas, mPaint, this);
            }
            //我们此处要判断Sprite在执行了draw方法后是否被destroy掉了
            if (s.isDestroyed()) {
                //如果Sprite被销毁了，那么从Sprites中将其移除
                iterator.remove();
            }
        }
        if (mPlayerAircraft != null) {
            //最后绘制战斗机
            mPlayerAircraft.draw(canvas, mPaint, this);
            if (mPlayerAircraft.isDestroyed()) {
                gameOver();
            }
            postInvalidate();
        }
        mFrameIndex++;
        drawFPS(canvas);
        drawScoreAndLevel(canvas);
        drawPauseButton(canvas);
    }
    private void drawPauseButton(Canvas canvas) {
        RectF pauseBitmapDstRecF = getPauseBitmapDstRecF();
        float pauseLeft = pauseBitmapDstRecF.left;
        float pauseTop = pauseBitmapDstRecF.top;
        canvas.drawBitmap(mPauseBitmap, pauseLeft, pauseTop, mPaint);
    }

    /***
     * 是否点击了暂停按钮
     * @param x
     * @param y
     * @return
     */
    private boolean isClickPause(float x, float y) {
        RectF pauseRecF = getPauseBitmapDstRecF();
        return pauseRecF.contains(x, y);
    }
    private RectF getPauseBitmapDstRecF() {
        RectF recF = new RectF();
        recF.left = 15 * mDensity;
        recF.top = 15 * mDensity;
        recF.right = recF.left + mPauseBitmap.getWidth();
        recF.bottom = recF.top + mPauseBitmap.getHeight();
        return recF;
    }
    private void drawFPS(Canvas canvas) {
        //60帧取样一次FPS
        if (mPreDrawTime != 0L && mFrameIndex % 60 == 0) {
            long nowTime = System.currentTimeMillis();
            mFps = getFPSByTime(mPreDrawTime, nowTime);
        }
        mPreDrawTime = System.currentTimeMillis();
        if (mFps >= 0) {
            StringBuffer sb = new StringBuffer("fps:").append(mFps);
            canvas.drawText(sb.toString(), 12f * mDensity, 12f * mDensity, mTextPaint);
        }
    }

    private int getFPSByTime(long preTime, long currentTime) {
        int fps = 0;
        long timeDis = currentTime - preTime;
        if (preTime > 0) {
            fps = (int) (1000L / timeDis);
        }
        return fps;
    }

    private void resetAllResources() {
        LevelServer.getInstance() .reset();
        mFrameIndex = 0;
        mScore = 0;
        //销毁战斗机
        if (mPlayerAircraft != null) {
            mPlayerAircraft.finalDestory();
        }
        mPlayerAircraft = null;

        //销毁敌机、子弹、奖励、爆炸
        for (BaseAirCraft s : mAirScrafts) {
            s.destroy();
        }
        mAirScrafts.clear();
    }

    private void restart() {
        resetAllResources();
        startByReady();
    }
    private void resume() {
        //将游戏设置为运行状态

        changeGameStatus(STATUS_GAME_STARTED);
        postInvalidate();
    }

    /***
     * 移除掉已经destroyed的飞机
     */
    private void removeDestroyedSprites() {
        Iterator<BaseAirCraft> iterator = mAirScrafts.iterator();
        while (iterator.hasNext()) {
            BaseAirCraft as = iterator.next();
            if (as.isDestroyed()) {
                iterator.remove();
            }
        }
    }

    private void destroyBulletsFrontOfPlayerAircraft() {
        if (mPlayerAircraft != null) {
            float aircraftY = mPlayerAircraft.getY();
            List<Bullet> aliveBullets = getAliveBullets();
            for (Bullet bullet : aliveBullets) {
                //如果战斗机跑到了子弹前面，那么就销毁子弹
                if (aircraftY <= bullet.getY()) {
                    bullet.destroy();
                }
            }
        }
    }

    //获取子弹
    public List<Bullet> getAliveBullets() {
        List<Bullet> bullets = new ArrayList<>();
        for (BaseAirCraft as : mAirScrafts) {
            if (!as.isDestroyed() && as instanceof Bullet) {
                Bullet bullet = (Bullet) as;
                bullets.add(bullet);
            }
        }
        return bullets;
    }


    public void addAirCraftToBuffer(BaseAirCraft airCraft) {
        mAirScraftsBuffer.add(airCraft);
    }

    public float getDensity() {
        return mDensity;
    }

    //生成随机的Sprite
    private void createEnemyAirCraftByRandom(int canvasWidth) {
        BaseAirCraft airCraft = null;
        //发送敌机
        Random random = new Random();
        int num = random.nextInt(10);
        int type = getPlaneTypeByNumber(num);
        if (type == EnemyAirCraft.ENEMY_TYPE_SPYPLANE) {
            airCraft = AirCraftFactory.getInstance().getAirCraft(SpyPlane.TAG);
        } else if (type == EnemyAirCraft.ENEMY_TYPE_FIGHTERPLANE) {
            airCraft = AirCraftFactory.getInstance().getAirCraft(FighterPlane.TAG);
        } else if (type == EnemyAirCraft.ENEMY_TYPE_CRUISERPLANE) {
            airCraft = AirCraftFactory.getInstance().getAirCraft(CruiserPlane.TAG);
        }
        if (airCraft != null) {
            float spriteWidth = airCraft.getWidth();
            float spriteHeight = airCraft.getHeight();
            float x = (float) ((canvasWidth - spriteWidth) * Math.random());
            float y = -spriteHeight;
            airCraft.setX(x);
            airCraft.setY(y);
            addAirCraftToBuffer(airCraft);
        }
    }

    private void createNormalButtleReward(int canvasWidth) {
        NormalButtleReward reward = (NormalButtleReward) AirCraftFactory.getInstance().getAirCraft(NormalButtleReward.TAG);
        if (reward != null) {
            float spriteWidth = reward.getWidth();
            float spriteHeight = reward.getHeight();
            float x = (float) ((canvasWidth - spriteWidth) * Math.random());
            float y = -spriteHeight;
            reward.setX(x);
            reward.setY(y);
            addAirCraftToBuffer(reward);
        }
    }

    private int getPlaneTypeByNumber(int num) {
        int result = EnemyAirCraft.ENEMY_TYPE_SPYPLANE;
        if (num > 8) {
            result = EnemyAirCraft.ENEMY_TYPE_CRUISERPLANE;
        } else if (num < 8 && num > 4) {
            result = EnemyAirCraft.ENEMY_TYPE_FIGHTERPLANE;
        } else {
            result = EnemyAirCraft.ENEMY_TYPE_SPYPLANE;
        }
        return result;
    }

    public List<EnemyAirCraft> getAllEnemyAirCrafts() {
        mEnemyAirScraft.clear();
        for (BaseAirCraft as : mAirScrafts) {
            if (!as.isDestroyed() && as instanceof EnemyAirCraft) {
                EnemyAirCraft airCraft = (EnemyAirCraft) as;
                mEnemyAirScraft.add(airCraft);
            }
        }
        return mEnemyAirScraft;
    }

    public void addScore(int score) {
        mScore += score;
        LevelServer.getInstance().UpgradeByScore(mScore);
    }

    private void drawScoreAndLevel(Canvas canvas) {
        int scorePadding = DisPlayUtils.dip2px(getContext(), 20f);
        int levelPaddingTop = DisPlayUtils.dip2px(getContext(), 10f);
        String score = String.format("%d 分", mScore);
        float scoreLeft = getWidth() - mScorePaint.measureText(score) - scorePadding;
        float scoreTop = scorePadding;
        canvas.drawText(score, scoreLeft, scoreTop, mScorePaint);
        String level = String.format("第%d关", mLevelServer.getLevel() + 1);
        float levelLeft = getWidth() - mScorePaint.measureText(level) - scorePadding;
        float levelTop = scoreTop + levelPaddingTop + mScorePaint.getTextSize();
        canvas.drawText(level, levelLeft, levelTop, mScorePaint);
    }

    public List<NormalButtleReward> getAliveBulletReward() {
        List<NormalButtleReward> rewards = new ArrayList<>();
        for (BaseAirCraft as : mAirScrafts) {
            if (!as.isDestroyed() && as instanceof NormalButtleReward) {
                NormalButtleReward reward = (NormalButtleReward) as;
                rewards.add(reward);
            }
        }
        return rewards;
    }

    private void changeGameStatus(int gameStatus) {
        mGameStatus = gameStatus;
        if (mGameStatusChangeListener != null) {
            mGameStatusChangeListener.onGameStatusChange(gameStatus);
        }
    }

    public void setBulleInfinite(boolean isBulleInfinite) {
       this.isBulleInfinite=isBulleInfinite;
    }

    public static  interface GameStatusChangeListener{
        void onGameStatusChange(int status);
    }

}
