package com.gt.facerecognition.view;

import com.gt.facerecognition.base.IBaseCallBack;
import com.gt.facerecognition.model.domain.FaceRecognition;
import com.gt.facerecognition.model.domain.User;

import java.util.List;

public interface IAllUserCallback extends IBaseCallBack {
    void onAllUserInfoLoaded(FaceRecognition faceRecognition);
}
