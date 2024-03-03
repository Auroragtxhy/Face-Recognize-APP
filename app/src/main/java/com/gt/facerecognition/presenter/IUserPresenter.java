package com.gt.facerecognition.presenter;

import com.gt.facerecognition.base.IBasePresenter;
import com.gt.facerecognition.view.IUserCallback;

public interface IUserPresenter extends IBasePresenter<IUserCallback> {
    void getUserInfo(String person);
}
