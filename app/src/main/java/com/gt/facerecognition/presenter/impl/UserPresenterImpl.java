package com.gt.facerecognition.presenter.impl;

import static java.net.HttpURLConnection.HTTP_OK;

import androidx.annotation.NonNull;

import com.gt.facerecognition.model.API;
import com.gt.facerecognition.model.domain.FaceRecognition;
import com.gt.facerecognition.presenter.IUserPresenter;
import com.gt.facerecognition.utils.LogUtils;
import com.gt.facerecognition.utils.RetrofitManager;
import com.gt.facerecognition.view.IUserCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserPresenterImpl implements IUserPresenter {

    IUserCallback mCallback = null;
    @Override
    public void registerViewCallback(IUserCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(IUserCallback callback) {
        this.mCallback = null;
    }

    /**
     * 加载用户数据
     * @param person 用户名
     */
    @Override
    public void getUserInfo(String person) {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Call<FaceRecognition> task = api.getUserInfo(person);
        task.enqueue(new Callback<FaceRecognition>() {
            @Override
            public void onResponse(@NonNull Call<FaceRecognition> call, @NonNull Response<FaceRecognition> response) {
                int code = response.code();
                if (code == HTTP_OK) {
                    LogUtils.d(this, "code -----> " + code);
                    FaceRecognition faceRecognitions = response.body();
                    assert faceRecognitions != null;
                    LogUtils.d(this, faceRecognitions.toString());
                    if (mCallback != null) {
                        if (faceRecognitions.getData().size() == 0) {
                            mCallback.onEmpty();
                        } else {
                            /* 通过回调接口加载数据 */
                            mCallback.userInfoLoaded(faceRecognitions);
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
