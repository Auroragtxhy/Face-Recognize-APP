package com.gt.facerecognition.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gt.facerecognition.R;
import com.gt.facerecognition.presenter.ICheckInfoPresenter;
import com.gt.facerecognition.utils.Constants;
import com.gt.facerecognition.utils.LogUtils;
import com.gt.facerecognition.view.ICheckInfoCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CheckDetailActivity extends AppCompatActivity implements ICheckInfoPresenter {

    private Unbinder mBind;

    private String mUser_name;
    private String mIdentify_time;
    private String mIdentify_result;
    private String mImage_url;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.Name_tv)
    public TextView mName_tv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.time_tv)
    public TextView mTime_tv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.result_tv)
    public TextView mResult_tv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.image_iv)
    public ImageView mImage_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail_acticity);
        mBind = ButterKnife.bind(CheckDetailActivity.this);
        getIntentInfo();
        initView();
    }

    /**
     * 设置控件信息
     */
    private void initView() {
        mName_tv.setText(mUser_name);
        mTime_tv.setText(mIdentify_time);
        mResult_tv.setText(mIdentify_result);
        Glide.with(this)
                .load(mImage_url)
                .into(mImage_iv);



//todo:图片的点击事件
        /* 图片的点击事件 */
//        mImage_iv.setOnClickListener(view -> {
//
//        });
    }

    /**
     * 获取intent的信息
     */
    private void getIntentInfo() {
        Intent intent = getIntent();
        mUser_name = intent.getStringExtra(Constants.ALL_USER_NAME);
        mIdentify_time = intent.getStringExtra(Constants.ALL_USER_TIME);
        mIdentify_result = intent.getStringExtra(Constants.ALL_USER_RESULT);
        mImage_url = intent.getStringExtra(Constants.ALL_USER_IMAGE_URL);
        LogUtils.d(this, "getImageUrl == " + mImage_url);
    }



    @Override
    public void getImage(String imageUrl) {

    }

    @Override
    public void registerViewCallback(ICheckInfoCallback callback) {

    }

    @Override
    public void unRegisterViewCallback(ICheckInfoCallback callback) {

    }
}