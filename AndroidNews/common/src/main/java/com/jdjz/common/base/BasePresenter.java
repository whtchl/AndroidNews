package com.jdjz.common.base;

import android.content.Context;

import com.jdjz.common.baserx.RxManager;

/**
 * des:基类 presenter
 * Created by tchl on 2016-10-27.
 */

public abstract class BasePresenter<T,E> {
    public Context mContext;
    public E mModel;
    public T mView;
    public RxManager mRxManager;

    public void setVM(T v,E m){
        mRxManager = new RxManager();
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart(){};

    public void onDestroy(){
        ;  //tchl
    }
}
