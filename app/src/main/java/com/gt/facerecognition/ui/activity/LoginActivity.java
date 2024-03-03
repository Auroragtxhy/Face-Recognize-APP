package com.gt.facerecognition.ui.activity;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gt.facerecognition.R;
import com.gt.facerecognition.model.domain.User;
import com.gt.facerecognition.presenter.impl.LoginPresenterImpl;
import com.gt.facerecognition.utils.Constants;
import com.gt.facerecognition.utils.LogUtils;
import com.gt.facerecognition.view.ILoginCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ILoginCallback {

    private LoginPresenterImpl mLoginPresenter;

    private String mUserAccount;
    private String mUserPassword;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.user_account_et)
    public EditText mUser_account_et;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.user_password_et)
    public EditText mUser_password_et;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.user_login_btn)
    public Button mUser_login_btn;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(LoginActivity.this);  //在设置layout后才能用，即在setContentView之后才可以使用
        checkLoginState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MCheckPermission();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MCheckPermissionForBluetooth();
        }
        initPresenter();
        initView();

    }

    /**
     * 运行时权限获取
     */
    @SuppressLint("InlinedApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void MCheckPermission() {
        int write_external_storagePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_external_storagePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int location_fine_code = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int location_coarse_code = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        int bluetooth = checkSelfPermission(Manifest.permission.BLUETOOTH);
        int bluetooth_admin = checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN);
        int bluetooth_connect = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
        int bluetooth_advertise = checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE);
        int bluetooth_scan = checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN);

        if (write_external_storagePermission == PackageManager.PERMISSION_GRANTED
                && read_external_storagePermission == PackageManager.PERMISSION_GRANTED
                && bluetooth_advertise == PackageManager.PERMISSION_GRANTED
                && bluetooth_scan == PackageManager.PERMISSION_GRANTED
                && bluetooth_connect == PackageManager.PERMISSION_GRANTED
                && bluetooth_admin == PackageManager.PERMISSION_GRANTED
                && bluetooth == PackageManager.PERMISSION_GRANTED
                && location_coarse_code == PackageManager.PERMISSION_GRANTED
                && location_fine_code == PackageManager.PERMISSION_GRANTED) {
            LogUtils.d(this, "已经有相应的权限");
        } else {
            LogUtils.d(this, "requestPermission");

            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, Constants.REQUEST_PERMISSION_CODE);

        }
    }

    /**
     * 高版本获取蓝牙权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InlinedApi")
    private void MCheckPermissionForBluetooth() {
        int bluetooth_connect = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
        int bluetooth_scan = checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN);
        if ( bluetooth_scan == PackageManager.PERMISSION_GRANTED && bluetooth_connect == PackageManager.PERMISSION_GRANTED ){
            LogUtils.d(this, "已经有相应的高版本蓝牙相应的权限");
        } else {
            LogUtils.d(this, "requestPermission");
            //获取权限
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
            }, Constants.REQUEST_PERMISSION_CODE);
        }
    }

    /**
     * 创建Presenter
     */
    private void initPresenter() {
        mLoginPresenter = new LoginPresenterImpl();
        mLoginPresenter.registerViewCallback(this);//导入Callback，注册接口，即当前类实现IHomeCallBack的接口
    }

    /**
     * 控件初始化
     */
    private void initView() {
        mUser_login_btn.setOnClickListener(this);
    }

    /**
     * 监测登录状态
     */
    private void checkLoginState() {
        mSettings = LoginActivity.this.getSharedPreferences(Constants.LOG_LOGIN, MODE_PRIVATE);
        boolean hasLogged = mSettings.getBoolean(Constants.HAS_LOGGED, false);
        if (hasLogged) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LogUtils.d(this, "非第一次登录，直接进入主界面");
            /* 防止用户登录后返回键返回该界面 测试结束后记得取消注释 */
            LoginActivity.this.finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_login_btn:
                mUserAccount = mUser_account_et.getText().toString().trim();
                mUserPassword = mUser_password_et.getText().toString().trim();
                LogUtils.d(this, "用户登录信息：" + mUserAccount + "/r/n" + mUserPassword);
                if (TextUtils.isEmpty(mUserAccount)) {
                    Toast.makeText(this, "账户不可以为空，请输入账户", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mUserPassword)) {
                    Toast.makeText(this, "密码不可以为空，请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                mLoginPresenter.loginToAccount(mUserAccount, mUserPassword);
                break;
        }
    }

    @Override
    public void onError() {
        Toast.makeText(this, "加载错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {
        Toast.makeText(this, "加载数据为空", Toast.LENGTH_SHORT).show();
    }

    /**
     * 登录的数据从这里返回
     * @param user 返回的数据
     */
    @Override
    public void onLoginLoaded(User user) {
        int result = user.getData().getRet();
        /* 账户存在 */
        if (result == 1) {
            /* 输入的密码和返回账户的密码一致 */
            if (user.getData().getPassword().equals(mUserPassword)) {
                String Server_returns_account = user.getData().getUser_account_phone();
                String Server_returns_name = user.getData().getUser_name();
                String Server_return_gender = user.getData().getGender();
                /* 保存用户信息 */
                saveUserInfoBySharedPreferences(Server_returns_account, Server_returns_name, Server_return_gender);
                /* 标志已经登录 */
                logLogin();
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(Constants.USER_ACCOUNT, mUserAccount);
                intent.putExtra(Constants.USER_PASSWORD, mUserPassword);
                startActivity(intent);
                LogUtils.d(this, "登录界面即将销毁");
                /* 防止用户登录后返回键返回该界面 测试结束后记得取消注释 */
                LoginActivity.this.finish();
            } else {
                Toast.makeText(LoginActivity.this, "输入密码不正确", Toast.LENGTH_SHORT).show();
                //LogUtils.d(this, "输入密码不正确，请重试");
            }
        } else {
            Toast.makeText(LoginActivity.this, "该账户不存在", Toast.LENGTH_SHORT).show();
            //LogUtils.d(this, "该账户不存在，请重试");
        }
    }

    /**
     * 标记已经登录
     */
    private void logLogin() {
        mSettings = getSharedPreferences(Constants.LOG_LOGIN, 0);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(Constants.HAS_LOGGED, true);
        editor.apply();
    }

    /**
     * 通过SharedPreferences保存用户账号
     */
    @SuppressLint("CommitPrefEdits")
    private void saveUserInfoBySharedPreferences(String server_returns_account, String server_returns_name, String server_return_gender) {
        mSharedPreferences = this.getSharedPreferences(Constants.SERVER_RETURNS_USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(Constants.SERVER_RETURN_ACCOUNT, server_returns_account);
        edit.putString(Constants.SERVER_RETURN_NAME, server_returns_name);
        edit.putString(Constants.SERVER_RETURN_GENDER, server_return_gender);
        edit.apply();
    }
}