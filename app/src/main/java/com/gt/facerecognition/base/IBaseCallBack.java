package com.gt.facerecognition.base;

public interface IBaseCallBack {
    /**
     * 加载时错误
     */
    void onError();

    /**
     * 正在加载
     */
    void onLoading();

    /**
     * 加载结果数据为空
     */
    void onEmpty();
}
