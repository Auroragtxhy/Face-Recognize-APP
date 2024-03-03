package com.gt.facerecognition.view;

import com.gt.facerecognition.base.IBaseCallBack;
import com.gt.facerecognition.model.domain.FaceRecognition;

public interface IUserCallback extends IBaseCallBack {
    void userInfoLoaded(FaceRecognition faceRecognition);
}
