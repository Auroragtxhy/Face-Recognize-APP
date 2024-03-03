package com.gt.facerecognition.base;

public interface IBasePresenter<T> {
    /**
     * 注册UI更新通知接口
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消UI更新通知接口
     * @param callback
     */
    void unRegisterViewCallback(T callback);
}
