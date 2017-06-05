package com.jorny.aircraftdemo.Factory;
/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.ArrayMap;

import com.jorny.aircraftdemo.ApplycationContext;
import com.jorny.aircraftdemo.R;
import com.jorny.aircraftdemo.model.BaseAirCraft;
import com.jorny.aircraftdemo.model.Bullet.Bullet;
import com.jorny.aircraftdemo.model.EnemyAirCraft.CruiserPlane;
import com.jorny.aircraftdemo.model.ExplosionPieces;
import com.jorny.aircraftdemo.model.EnemyAirCraft.FighterPlane;
import com.jorny.aircraftdemo.model.Bullet.NormalBullet;
import com.jorny.aircraftdemo.model.Reward.NormalButtleReward;
import com.jorny.aircraftdemo.model.PlayerAirCraft;
import com.jorny.aircraftdemo.model.EnemyAirCraft.SpyPlane;
import com.jorny.aircraftdemo.model.Bullet.SuperBullet;

/***
 * 缓冲池 避免大量CG 导致卡顿
 */
public class AirCraftFactory {
    public static final int MAX_BULLET_SIZE = 30;
    public static final int MAX_FIGHTER_PLANE_SIZE = 10;
    public static final int MAX_CRUISER_PLANE_SIZE = 8;
    public static final int MAX_SPY_PLANE_SIZE = 20;
    public static final int MAX_EXPLO_PIECES_SIZE = 20;
    private static AirCraftFactory sAirCraftFactory;
    private ArrayMap<String, Bitmap> mAirCraftBitMaps = new ArrayMap<>();
    private CacheAirCraftPool mBulletPool = new CacheAirCraftPool(MAX_BULLET_SIZE);
    private CacheAirCraftPool mCuiserPlanePool = new CacheAirCraftPool(MAX_CRUISER_PLANE_SIZE);
    private CacheAirCraftPool mFighterPlanePool = new CacheAirCraftPool(MAX_FIGHTER_PLANE_SIZE);
    private CacheAirCraftPool mSpyPlanePool = new CacheAirCraftPool(MAX_SPY_PLANE_SIZE);
    private CacheAirCraftPool mExploPiecePool = new CacheAirCraftPool(MAX_EXPLO_PIECES_SIZE);

    public void addAirCraftToPool(BaseAirCraft airCraft) {
        if (airCraft != null) {
            if (airCraft instanceof Bullet) {
                mBulletPool.putCacheItem(airCraft);
            } else if (airCraft instanceof SpyPlane) {
                mSpyPlanePool.putCacheItem(airCraft);
            } else if (airCraft instanceof FighterPlane) {
                mFighterPlanePool.putCacheItem(airCraft);
            } else if (airCraft instanceof CruiserPlane) {
                mCuiserPlanePool.putCacheItem(airCraft);
            } else if (airCraft instanceof ExplosionPieces) {
                mExploPiecePool.putCacheItem(airCraft);
            }

        }
    }

    private AirCraftFactory() {
        init();
    }

    private void init() {
        Context context = ApplycationContext.sContext;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.play_air_scraft);
        mAirCraftBitMaps.put(PlayerAirCraft.TAG, bitmap);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.normal_bullet);
        mAirCraftBitMaps.put(NormalBullet.TAG, bitmap);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_bullet);
        mAirCraftBitMaps.put(SuperBullet.TAG, bitmap);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cruiserplane);
        mAirCraftBitMaps.put(CruiserPlane.TAG, bitmap);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fighter_plane);
        mAirCraftBitMaps.put(FighterPlane.TAG, bitmap);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spyplane);
        mAirCraftBitMaps.put(SpyPlane.TAG, bitmap);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        mAirCraftBitMaps.put(ExplosionPieces.TAG, bitmap);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_reward);
        mAirCraftBitMaps.put(NormalButtleReward.TAG, bitmap);

    }

    public synchronized static AirCraftFactory getInstance() {
        if (sAirCraftFactory == null) {
            sAirCraftFactory = new AirCraftFactory();
        }
        return sAirCraftFactory;
    }
    public BaseAirCraft getAirCraft(String tag) {
        BaseAirCraft airCraft = null;
        Bitmap bitmap;
        if (SpyPlane.TAG.equals(tag)) {
            airCraft = mSpyPlanePool.getCacheItem();
            if (airCraft == null) {
                bitmap = getBitmapByTAG(tag);
                airCraft = new SpyPlane(bitmap);
            }
        } else if (FighterPlane.TAG.equals(tag)) {
            airCraft = mFighterPlanePool.getCacheItem();
            if (airCraft == null) {
                bitmap = getBitmapByTAG(tag);
                airCraft = new FighterPlane(bitmap);
            }
        } else if (CruiserPlane.TAG.equals(tag)) {
            airCraft = mCuiserPlanePool.getCacheItem();
            if (airCraft == null) {
                bitmap = getBitmapByTAG(tag);
                airCraft = new CruiserPlane(bitmap);
            }
        } else if (NormalBullet.TAG.equals(tag)) {
            airCraft = mBulletPool.getCacheItem();
            if (airCraft == null || !(airCraft instanceof NormalBullet)) {
                bitmap = getBitmapByTAG(tag);
                airCraft = new NormalBullet(bitmap);
            }
        } else if (SuperBullet.TAG.equals(tag)) {
            airCraft = mBulletPool.getCacheItem();
            if (airCraft == null || !(airCraft instanceof NormalBullet)) {
                bitmap = getBitmapByTAG(tag);
                airCraft = new SuperBullet(bitmap);
            }
        } else if (PlayerAirCraft.TAG.equals(tag)) {
            bitmap = getBitmapByTAG(tag);
            airCraft = new PlayerAirCraft(bitmap);
        } else if (ExplosionPieces.TAG.equals(tag)) {
            airCraft = mExploPiecePool.getCacheItem();
            if (airCraft == null) {
                bitmap = getBitmapByTAG(tag);
                airCraft = new ExplosionPieces(bitmap);
            }
        } else if (NormalButtleReward.TAG.equals(tag)) {
            bitmap = getBitmapByTAG(tag);
            airCraft = new NormalButtleReward(bitmap);
        }
        return airCraft;
    }

    public Bitmap getBitmapByTAG(String tag) {
        return mAirCraftBitMaps.get(tag);
    }

}
