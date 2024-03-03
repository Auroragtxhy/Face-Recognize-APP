package com.gt.facerecognition.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gt.facerecognition.R;
import com.gt.facerecognition.base.BaseFragment;
import com.gt.facerecognition.base.BaseRecyclerViewAdapter;
import com.gt.facerecognition.model.domain.FaceRecognition;
import com.gt.facerecognition.presenter.impl.AllUserPresenterImpl;
import com.gt.facerecognition.ui.activity.CheckDetailActivity;
import com.gt.facerecognition.utils.Constants;
import com.gt.facerecognition.utils.LogUtils;
import com.gt.facerecognition.view.IAllUserCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AllUserFragment extends BaseFragment implements IAllUserCallback {

    /* 保存回调接口加载进来的数据 */
    private List<FaceRecognition.DataDTO> mDataDTOS = new ArrayList<>();

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.show_all_user_btn)
    public Button mShow_All_User_Info_Btn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.all_user_info_rcv)
    public RecyclerView mAll_User_Info_Rcv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.show_unknown_user_btn)
    public Button mShow_Unknown_User_Info_Btn;

    private AllUserPresenterImpl mAllUserPresenter;
    private BaseRecyclerViewAdapter mBaseInfoAdapter;

    /**
     * 實現父類中的獲取加載成功後的layout的id的方法
     * @return
     */
    @Override
    protected int getSuccessViewResID() {
        return R.layout.fragment_all_user;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        /* 设置rcv的布局管理器 */
        mAll_User_Info_Rcv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mBaseInfoAdapter = new BaseRecyclerViewAdapter();
        mAll_User_Info_Rcv.setAdapter(mBaseInfoAdapter);
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

    @Override
    protected void initListener() {
        mShow_All_User_Info_Btn.setOnClickListener(view -> {
            /* 点击按键，获取所有用户的数据 */
            mAllUserPresenter.getAllUSerInfo();
        });

        mShow_Unknown_User_Info_Btn.setOnClickListener(view -> {
            /* 点击按键，获取未知用户的数据 */
            mAllUserPresenter.getUnknownUserInfo();
        });
    }

    /*
    注册回调接口
     */
    @Override
    protected void initPresenter() {
        mAllUserPresenter = new AllUserPresenterImpl();
        mAllUserPresenter.registerViewCallback(this);
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

    /*
    加载的数据从这里进来
     */
    @Override
    public void onAllUserInfoLoaded(FaceRecognition faceRecognition) {
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
        if (mAllUserPresenter != null) {
            mAllUserPresenter.getAllUSerInfo();
        }
    }
}
