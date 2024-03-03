package com.gt.facerecognition.presenter;

import com.gt.facerecognition.base.IBasePresenter;
import com.gt.facerecognition.view.ILoginCallback;

public interface ILoginPresenter extends IBasePresenter<ILoginCallback> {

    void loginToAccount(String userAccount, String userPassword);
}
