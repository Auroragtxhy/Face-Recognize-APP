package com.gt.facerecognition.presenter.impl;

import com.gt.facerecognition.base.BaseFragment;
import com.gt.facerecognition.presenter.ICheckInfoPresenter;
import com.gt.facerecognition.utils.RetrofitManager;
import com.gt.facerecognition.view.ICheckInfoCallback;

import retrofit2.Retrofit;

public class CheckInfoPresenterImpl implements ICheckInfoPresenter {

    private ICheckInfoCallback mCallback = null;

    public String imageUrl;


    @Override
    public void registerViewCallback(ICheckInfoCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(ICheckInfoCallback callback) {
        this.mCallback = null;
    }

    @Override
    public void getImage(String imageUrl) {


    }
}
