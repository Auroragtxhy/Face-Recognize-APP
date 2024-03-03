package com.gt.facerecognition.view;

import android.graphics.Bitmap;

import com.gt.facerecognition.base.IBaseCallBack;

public interface ICheckInfoCallback extends IBaseCallBack {
    void imageLoaded(Bitmap bitmap);
}
