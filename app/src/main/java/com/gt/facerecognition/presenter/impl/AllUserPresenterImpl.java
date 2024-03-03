package com.gt.facerecognition.presenter.impl;

import static java.net.HttpURLConnection.HTTP_OK;

import androidx.annotation.NonNull;

import com.gt.facerecognition.model.API;
import com.gt.facerecognition.model.domain.FaceRecognition;
import com.gt.facerecognition.presenter.IAllUserPresenter;
import com.gt.facerecognition.utils.LogUtils;
import com.gt.facerecognition.utils.RetrofitManager;
import com.gt.facerecognition.view.IAllUserCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.HTTP;

public class AllUserPresenterImpl implements IAllUserPresenter {
    private IAllUserCallback mCallback = null;

    @Override
    public void registerViewCallback(IAllUserCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(IAllUserCallback callback) {
        mCallback = null;
    }

    /*
    这里加载所有用户的数据
     */
    @Override
    public void getAllUSerInfo() {
        /* 加载之前将界面设置为加载界面 */
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Call<FaceRecognition> task = api.getFaceInfo();
        task.enqueue(new Callback<FaceRecognition>() {
            @Override
            public void onResponse(@NonNull Call<FaceRecognition> call, @NonNull Response<FaceRecognition> response) {
                /* 加载成功 */
                int code = response.code();
                LogUtils.d(this, "code -----> " + code);
                if (code == HTTP_OK) {
                    FaceRecognition faceRecognitions = response.body();
                    assert faceRecognitions != null;
                    LogUtils.d(this, faceRecognitions.toString());
                    if (mCallback != null) {
                        if (faceRecognitions.getData().size() == 0) {
                            mCallback.onEmpty();
                        } else {
                            /* 通过回调接口加载数据 */
                            //todo：逆序添加数据
                            mCallback.onAllUserInfoLoaded(faceRecognitions);
                        }
                    }
                } else {
                    LogUtils.d(this, "请求失败");
                    if (mCallback != null) {
                        mCallback.onError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FaceRecognition> call, @NonNull Throwable t) {
                LogUtils.d(this, "请求错误...");
                LogUtils.d(this, t.toString());
                if (mCallback != null) {
                    mCallback.onError();
                }
            }
        });
    }

    /*
    这里加载未知用户的数据
     */
    @Override
    public void getUnknownUserInfo() {
        /* 加载之前将界面设置为加载界面 */
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Call<FaceRecognition> task = api.getUnknownInfo();
        task.enqueue(new Callback<FaceRecognition>() {
            @Override
            public void onResponse(@NonNull Call<FaceRecognition> call, @NonNull Response<FaceRecognition> response) {
                /* 加载成功 */
                int code = response.code();
                LogUtils.d(this, "code -----> " + code);
                if (code == HTTP_OK) {
                    FaceRecognition faceRecognitions = response.body();
                    assert faceRecognitions != null;
                    LogUtils.d(this, faceRecognitions.toString());
                    if (mCallback != null) {
                        if (faceRecognitions.getData().size() == 0) {
                            mCallback.onEmpty();
                        } else {
                            /* 通过回调接口加载数据 */
                            //todo：逆序添加数据
                            mCallback.onAllUserInfoLoaded(faceRecognitions);
                        }
                    }
                } else {
                    LogUtils.d(this, "请求失败");
                    if (mCallback != null) {
                        mCallback.onError();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FaceRecognition> call, @NonNull Throwable t) {
                LogUtils.d(this, "请求错误...");
                LogUtils.d(this, t.toString());
                if (mCallback != null) {
                    mCallback.onError();
                }
            }
        });
    }
}
