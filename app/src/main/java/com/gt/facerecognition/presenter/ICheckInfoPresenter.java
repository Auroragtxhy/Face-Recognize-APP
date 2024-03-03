package com.gt.facerecognition.presenter;

import com.gt.facerecognition.base.IBasePresenter;
import com.gt.facerecognition.view.ICheckInfoCallback;

public interface ICheckInfoPresenter extends IBasePresenter<ICheckInfoCallback> {
    void getImage(String imageUrl);
}
