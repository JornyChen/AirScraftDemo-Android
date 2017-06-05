package com.jorny.aircraftdemo.Factory;

import com.jorny.aircraftdemo.model.BaseAirCraft;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;


/* 缓存池
 * @Description:
 * 作者  陈兆龙
 * 时间 2017年06月02
 * 
 */
public class CacheAirCraftPool {
    private static CacheAirCraftPool instance;//缓存池唯一实例
    private Queue<BaseAirCraft> mCacheItems;//缓存Queue
    private int mMaxSize;
    public CacheAirCraftPool(int maxSize) {
        this.mMaxSize=maxSize;
        mCacheItems = new LinkedList<>();
    }


    /**
     * 清除所有Item缓存
     */
    public synchronized void clearAllItems() {
        mCacheItems.clear();
    }

    /**
     * 获取缓存实体
     */
    public synchronized BaseAirCraft getCacheItem() {
        BaseAirCraft cacheItem = null;
        if (!mCacheItems.isEmpty()) {
            cacheItem = mCacheItems.poll();
        }
        if (cacheItem != null) {
            cacheItem.reset();
        }
        return cacheItem;
    }
    /**
     * 存放缓存信息
     */
    public synchronized void putCacheItem(BaseAirCraft airCraft) {
        if (!mCacheItems.contains(airCraft)) {
            if (mCacheItems.size() < mMaxSize) {
                mCacheItems.add(airCraft);
            } else {
                BaseAirCraft removeAirCraft = mCacheItems.poll();
                if (removeAirCraft != null) {
                    removeAirCraft.finalDestory();
                    //最终被销毁
                }
                mCacheItems.add(airCraft);
            }
        }
    }
    /**
     * 获取缓存数据的数量
     *
     * @return
     */
    public int getSize() {
        return mCacheItems.size();
    }
}
