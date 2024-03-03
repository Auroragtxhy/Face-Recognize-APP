package com.gt.facerecognition.ui.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gt.facerecognition.R;
import com.gt.facerecognition.base.BaseFragment;
import com.gt.facerecognition.base.BaseRecyclerViewAdapter;
import com.gt.facerecognition.model.domain.FaceRecognition;
import com.gt.facerecognition.presenter.IUserPresenter;
import com.gt.facerecognition.presenter.impl.UserPresenterImpl;
import com.gt.facerecognition.ui.activity.CheckDetailActivity;
import com.gt.facerecognition.ui.activity.LoginActivity;
import com.gt.facerecognition.utils.Constants;
import com.gt.facerecognition.utils.LogUtils;
import com.gt.facerecognition.view.IUserCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UserFragment extends BaseFragment implements IUserCallback {


    /* 保存回调接口加载进来的数据 */
    private List<FaceRecognition.DataDTO> mDataDTOS = new ArrayList<>();

    IUserPresenter mIUserPresenter = null;

    private String mUserName;
    private String mUserAccount;
    private String mUserGender;

    /* 初始化控件 */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.profile_image)
    public ImageView mUser_head_portrait;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.user_name_tv)
    public TextView mUser_name_tv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.user_id_tv)
    public TextView mUser_id_tv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.user_sign_out_btn)
    public Button mUser_sign_out_btn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.user_check_info_btn)
    public Button mUser_check_info_btn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.user_info_list_rcv)
    public RecyclerView mUser_info_list_rcv;
    private BaseRecyclerViewAdapter mBaseInfoAdapter;


    @Override
    protected int getSuccessViewResID() {
        return R.layout.fragment_user2;
    }

    @Override
    protected void initPresenter() {
        mIUserPresenter = new UserPresenterImpl();
        mIUserPresenter.registerViewCallback(this);
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        getUserInfoBySharedPreferences();
        /* 设置控件信息 */
        mUser_name_tv.setText(mUserName);
        mUser_id_tv.setText(mUserAccount);
        if (mUserGender.equals("男")) {
            mUser_head_portrait.setImageResource(R.mipmap.user_man);
        } else {
            mUser_head_portrait.setImageResource(R.mipmap.user_woman);
        }
        /* 设置适配器 */
        mUser_info_list_rcv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mBaseInfoAdapter = new BaseRecyclerViewAdapter();
        mUser_info_list_rcv.setAdapter(mBaseInfoAdapter);
        /* 条目里按钮的点击事件的监听,进入新的Activity查看条目详情 */
        mBaseInfoAdapter.setItemButtonOnClickListener((name, time, result, imageUrl) -> {
            Intent intent = new Intent(requireActivity(), CheckDetailActivity.class);
            intent.putExtra(Constants.ALL_USER_NAME, name);
            intent.putExtra(Constants.ALL_USER_TIME, time);
            intent.putExtra(Constants.ALL_USER_RESULT, result);
            intent.putExtra(Constants.ALL_USER_IMAGE_URL, imageUrl);
            startActivity(intent);
        });
    }

    /**
     * 获取登录时保存的用户信息
     */
    private void getUserInfoBySharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.SERVER_RETURNS_USER_INFO, MODE_PRIVATE);
        mUserName = sharedPreferences.getString(Constants.SERVER_RETURN_NAME, "小蟹");
        LogUtils.d(this, "mUserName == " + mUserName);
        mUserAccount = sharedPreferences.getString(Constants.SERVER_RETURN_ACCOUNT, "17393128103");
        LogUtils.d(this, "mUserAccount == " + mUserAccount);
        mUserGender = sharedPreferences.getString(Constants.SERVER_RETURN_GENDER, "女");
        LogUtils.d(this, "mUserGender == " + mUserGender);
    }

    @Override
    protected void initListener() {
        /* 获取信息按钮的点击事件 */
        mUser_check_info_btn.setOnClickListener(view -> {
            mIUserPresenter.getUserInfo(mUserName);
        });

        /* 退出当前账号按钮的点击事件 */
        mUser_sign_out_btn.setOnClickListener(view -> {
            clearLoginTokenStatus();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            LogUtils.d(this, "退出当前账号...");
            requireActivity().finish();
        });
    }

    /**
     * 退出登录时,清除标记的登录状态
     */
    private void clearLoginTokenStatus() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOG_LOGIN, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.apply();
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
//        setUpState(State.EMPTY);
        Toast.makeText(getContext(), "当前没有可执行任务", Toast.LENGTH_SHORT).show();
    }

    /* 加载的数据 */
    @Override
    public void userInfoLoaded(FaceRecognition faceRecognition) {
        /* 数据过来后先将状态设置为成功 */
        LogUtils.d(this, "数据加载成功...");
        setUpState(State.SUCCESS);
        /* 数据判空 */
        if (faceRecognition != null) {
            mDataDTOS.clear();
            mDataDTOS.addAll(faceRecognition.getData());
            /* 给Adapter设置数据 */
            mBaseInfoAdapter.setData(mDataDTOS);
        }
    }

    @Override
    protected void onRetryClick() {
        /* 网络错误点击重试 */
        if (mIUserPresenter != null) {
            mIUserPresenter.getUserInfo(mUserName);
        }
    }
}
