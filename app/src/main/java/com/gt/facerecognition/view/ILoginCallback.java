package com.gt.facerecognition.view;

import com.gt.facerecognition.base.IBaseCallBack;
import com.gt.facerecognition.model.domain.User;

public interface ILoginCallback extends IBaseCallBack {
    void onLoginLoaded(User user);
}
