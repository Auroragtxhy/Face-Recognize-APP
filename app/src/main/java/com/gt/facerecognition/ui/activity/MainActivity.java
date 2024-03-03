package com.gt.facerecognition.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.gt.facerecognition.R;
import com.gt.facerecognition.ui.fragment.AllUserFragment;
import com.gt.facerecognition.ui.fragment.BluetoothFragment;
import com.gt.facerecognition.ui.fragment.UserFragment;
import com.gt.facerecognition.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;

    private Unbinder mBind;
    private AllUserFragment mAllUserFragment;
    private BluetoothFragment mUnknownUserFragment;
    private UserFragment mUserFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* 使用ButterKnife绑定这个界面 */
        mBind = ButterKnife.bind(this);
//        initFragment();
        initListener();
        selectTab(0);
    }

    /**
     * 點擊事件發生時候的页面切换
     */
    private void initListener() {
        mNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                LogUtils.d(this,"item -->" + item.getTitle() +  "      id --> " +  item.getItemId());
                if (item.getItemId() == R.id.allUser) {
                    LogUtils.d(this, "切换到首页");
//                    switchFragment(mAllUserFragment);
                    selectTab(0);
                }
                if (item.getItemId() == R.id.unknownUser) {
                    LogUtils.d(this, "切换到未知");
//                    switchFragment(mUnknownUserFragment);
                    selectTab(1);
                }
                if (item.getItemId() == R.id.knownUser) {
                    LogUtils.d(this, "切换到用戶");
//                    switchFragment(mUserFragment);
                    selectTab(2);
                }
                return true;
            }
        });
    }
/***************************************************************************************/
    /**
     * 修改fragment的切换方式
     * 旧的切换方法比较简洁，不过每次切换后原来的fragment界面的资源就销毁了，需要重新加载rcv等资源
     * 新的方法繁琐一点，不过只是隐藏了fragment，不需要重新加载。
     */
    /**
     * 初始化Fragment界面
     */
//    private void initFragment() {
//        mAllUserFragment = new AllUserFragment();
//        mUnknownUserFragment = new UnknownUserFragment();
//        mUserFragment = new UserFragment();
//        mSupportFragmentManager = getSupportFragmentManager();
//        switchFragment(mAllUserFragment);
//    }

    /**
     * fragment切換
     * @param targetFragment 目標fragment
     */
//    private void switchFragment(BaseFragment targetFragment) {
//        FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.main_fragment_container, targetFragment);
//        fragmentTransaction.commit();
//    }
/***************************************************************************************/

    /**
     * 进行选中tab的处理
     * @param i 传入的代表tab的整形
     */
    private void selectTab(int i) {
        /* 获取FragmentManager对象 */
        FragmentManager manager = getSupportFragmentManager();
        /* 获取FragmentTransaction对象 */
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        /* 先隐藏所有的Fragment */
        hideFragments(fragmentTransaction);
        switch (i) {
            case 0:
                if (mAllUserFragment == null) {
                    mAllUserFragment = new AllUserFragment();
                    fragmentTransaction.add(R.id.main_fragment_container, mAllUserFragment);
                } else {
                    /* 如果第一页对应的Fragment已经实例化，则直接显示出来 */
                    fragmentTransaction.show(mAllUserFragment);
                }
                break;

            case 1:
                if (mUnknownUserFragment == null) {
                    mUnknownUserFragment = new BluetoothFragment();
                    fragmentTransaction.add(R.id.main_fragment_container, mUnknownUserFragment);
                } else {
                    /* 如果第一页对应的Fragment已经实例化，则直接显示出来 */
                    fragmentTransaction.show(mUnknownUserFragment);
                }
                break;

            case 2:
                if (mUserFragment == null) {
                    mUserFragment = new UserFragment();
                    fragmentTransaction.add(R.id.main_fragment_container, mUserFragment);
                } else {
                    /* 如果第一页对应的Fragment已经实例化，则直接显示出来 */
                    fragmentTransaction.show(mUserFragment);
                }
                break;
        }
        /* 不要忘记提交事务 */
        fragmentTransaction.commit();

    }
    /**
     * 将三个的Fragment隐藏
     *
     * @param transaction 传入的Fragment事务的对象
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mAllUserFragment != null) {
            transaction.hide(mAllUserFragment);
        }
        if (mUnknownUserFragment != null) {
            transaction.hide(mUnknownUserFragment);
        }
        if (mUserFragment != null) {
            transaction.hide(mUserFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* 在销毁的时候解绑 */
        if (mBind != null) {
            mBind.unbind();
        }
    }
}