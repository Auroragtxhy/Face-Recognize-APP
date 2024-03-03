package com.gt.facerecognition.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gt.facerecognition.R;
import com.gt.facerecognition.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    /* 定义一个枚举状态指定当前加载状态 */
    private State currentState = State.NONE;

    private FrameLayout mBaseContainer;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    private Unbinder mBind;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater, container);
        mBaseContainer = rootView.findViewById(R.id.base_fragment_container);
        loadStateView(inflater, container);
        mBind = ButterKnife.bind(this, rootView);
        /* 初始化presenter，有的Fragment不需要创建Presenter，所以这里用protected，方便其他的子类fragment复写该方法 */
        initPresenter();
        /* 初始化View。将rootView作为参数送入，不管是否用到，在子类中复写该方法 */
        initView(rootView);
        /* 在initView（初始化控件）过后，即可对控件设置事件的监听 */
        initListener();
        /* 加载数据。是一个具体的方法，非抽象。有的Fragment不需要加载数据，所以这里用protected，方便其他的子类fragment复写该方法 */
        loadData();
        return rootView;
    }

    protected void loadData() {
        //加載數據
    }

    protected void initPresenter() {
        //創建presenter
    }

    protected void initView(View rootView){
        //初始化控件
    }

    /**
     * 加載View的狀態
     */
    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        /* 填充加載成功的fragment */
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);
        /* 填充正在加載的fragment */
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);
        /* 填充加載錯誤的fragment */
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);
        /* 填充加載錯誤的fragment */
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);
        /* 设置初始状态为无 */
        setUpState(State.NONE);
    }

    public void setUpState(State state) {
        this.currentState = state;
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int resID = getSuccessViewResID();
        return inflater.inflate(resID, container, false);
    }

    /**
     * 獲取成功 時候的layout的id
     * @return resID
     */
    protected abstract int getSuccessViewResID();

    /**
     * root View
     */
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 如果子类需要设置相关的监听事件，覆盖该方法即可
     */
    protected void initListener() {

    }

    /**
     * 设置加载错误的重点击事件，使用butterKnife
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.network_error_tips)
    public void retry() {
        //点击了重新加载内容
        LogUtils.d(this, "on retry...");
        onRetryClick();
    }

    /**
     * 如果子类需要网络错误后的点击，覆写该方法即可
     */
    protected void onRetryClick() {

    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();  // 在销毁的时候解除绑定
        }
        release();  //在销毁该Fragment的时候释放资源
    }

    protected void release() {
        //释放资源
    }
}
