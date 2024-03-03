package com.gt.facerecognition.presenter;

import com.gt.facerecognition.base.IBasePresenter;
import com.gt.facerecognition.view.IAllUserCallback;

public interface IAllUserPresenter extends IBasePresenter<IAllUserCallback> {
    void getAllUSerInfo();
    void getUnknownUserInfo();
}
