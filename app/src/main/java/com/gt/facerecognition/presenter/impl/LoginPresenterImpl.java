package com.gt.facerecognition.presenter.impl;

import static java.net.HttpURLConnection.HTTP_OK;

import androidx.annotation.NonNull;

import com.gt.facerecognition.model.API;
import com.gt.facerecognition.model.domain.User;
import com.gt.facerecognition.presenter.ILoginPresenter;
import com.gt.facerecognition.utils.LogUtils;
import com.gt.facerecognition.utils.RetrofitManager;
import com.gt.facerecognition.view.ILoginCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginPresenterImpl implements ILoginPresenter {

    private ILoginCallback mCallback = null;

    /**
     * 实现UI通知的接口
     * 多个界面引用时候要使用集合来保存回调引用，即保存这个callback，通知更新的时候用for循环
     * @param callback 回调
     */
    @Override
    public void registerViewCallback(ILoginCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 实现取消UI通知的接口
     *
     * @param callback 回调
     */
    @Override
    public void unRegisterViewCallback(ILoginCallback callback) {
        mCallback = null;
    }

    /**
     * 提交登录信息
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     */
    @Override
    public void loginToAccount(String userAccount, String userPassword) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Call<User> task = api.loginToAccount(userAccount, userPassword);
        task.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                int code = response.code();
                LogUtils.d(this, "code == " + code);
                if (code == HTTP_OK) {
                    User user = response.body();
                    assert user != null;
                    LogUtils.d(this, user.toString());
                    if (mCallback != null) {
                        if (user.getData() == null) {
                            mCallback.onEmpty();
                        } else {
                            mCallback.onLoginLoaded(user);
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
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                LogUtils.d(this, "请求错误...");
                LogUtils.d(this, t.toString());
                if (mCallback != null) {
                    mCallback.onError();
                }
            }
        });
    }
}
