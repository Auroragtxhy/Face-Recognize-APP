package com.gt.facerecognition.ui.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gt.facerecognition.R;
import com.gt.facerecognition.base.BaseFragment;
import com.gt.facerecognition.ui.adapter.BluetoothRecyclerviewAdapter;
import com.gt.facerecognition.utils.Constants;
import com.gt.facerecognition.utils.LogUtils;
import com.gt.facerecognition.utils.SendAndReceiveBluetoothMessages;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;

public class BluetoothFragment extends BaseFragment implements View.OnClickListener {



    /* layout在父类中绑定，这里直接使用ButterKnife绑定控件 */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.turn_on_bluetooth_btn)
    public ImageButton mOpenBluetooth;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.search_bluetooth_btn)
    public ImageButton mSearchBluetooth;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.search_bluetooth_state_tv)
    public TextView mSearchBluetoothState;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bluetooth_disconnected_btn)
    public ImageButton mDisconnectedBluetooth;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.turn_off_bluetooth_btn)
    public ImageButton mOffBluetooth;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.connected_bluetooth_device_name_tv)
    public TextView mConnectedDeviceName;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.connected_bluetooth_device_address_tv)
    public TextView mConnectedDeviceAddress;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.controller_of_btn_CardView)
    public CardView mControllerOfButton;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bluetooth_open_door_comment_btn)
    public Button mOpenDoorComment;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bluetooth_close_door_comment_btn)
    public Button mCloseDoorComment;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bluetooth_list_rcv)
    public RecyclerView mBluetoothRCV;

    private final Handler mHandler = new Handler();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothGatt mBluetoothGatt = null;
    private BluetoothGattDescriptor mDescriptor = null;
    private BluetoothRecyclerviewAdapter mBluetoothRecyclerviewAdapter;
    private String mConnectedDeviceName1;
    private String mConnectedDevicesAddress1;
    private BluetoothGattCharacteristic mTargetCharacter;

    ActivityResultLauncher<Intent> requestDataLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                /* 获取蓝牙管理器 */
                mBluetoothManager = (BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE);
                /* 获取蓝牙适配器 */
                mBluetoothAdapter = mBluetoothManager.getAdapter();
                /* 获取低功耗蓝牙扫描器 */
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();});

    /**
     * 實現父類中的獲取加載成功後的layout的id的方法
     */
    @Override
    protected int getSuccessViewResID() {
        return R.layout.fragment_bluetooth;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);

        /* 获取蓝牙管理器 */
        mBluetoothManager = (BluetoothManager) requireActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        /* 获取蓝牙适配器 */
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        /* 获取低功耗蓝牙扫描器 */
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        /* Recyclerview适配器 */
        mBluetoothRecyclerviewAdapter = new BluetoothRecyclerviewAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBluetoothRCV.setLayoutManager(linearLayoutManager);
        mBluetoothRCV.setAdapter(mBluetoothRecyclerviewAdapter);

        /* 条目里点击事件的监听 */
        mBluetoothRecyclerviewAdapter.setOnItemButtonClickListener(position -> {
            BluetoothDevice device = mBluetoothRecyclerviewAdapter.getDevice(position);
            mConnectedDeviceName1 = device.getName();
            mConnectedDevicesAddress1 = device.getAddress();
            /* 关闭所有的连接 */
            disconnectBluetooth1();
            /* 连接蓝牙 */
            connectBluetooth(mConnectedDevicesAddress1);
        });
    }

    /**
     * 连接蓝牙
     * @param connectedDeviceAddress 蓝牙设备地址
     */
    @SuppressLint("MissingPermission")
    private boolean connectBluetooth(String connectedDeviceAddress) {
        if (mBluetoothAdapter == null || connectedDeviceAddress.isEmpty()) {
            Toast.makeText(requireActivity(), "发生了不可名状之错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        /* 通过传入的地址，利用适配器获取远程设备 */
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(connectedDeviceAddress);
        if (device == null) {
            Toast.makeText(requireActivity(), "未找到该设备", Toast.LENGTH_SHORT).show();
            return false;
        }
        mBluetoothGatt = device.connectGatt(requireActivity(), false, mGattCallback);
        if (mBluetoothGatt == null) {
            Toast.makeText(requireActivity(), "连接设备失败", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /***
     * 蓝牙连接的回调
     */
    public BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            LogUtils.d(this, "onConnectionStateChange : status = " + status + "     newState = " +  newState);
            BluetoothDevice device = gatt.getDevice();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    LogUtils.d(this, "onConnectionStateChange: 连接成功，device name =" + device.getName());
                    mHandler.sendEmptyMessage(Constants.BLUETOOTH_CONNECTED);
                    updateConnectStatus(true);
                    boolean ret = mBluetoothGatt.discoverServices();
                    LogUtils.d(this, "onConnectionStateChange: 是否返现服务：" + ret);
                } else if(newState == BluetoothGatt.STATE_DISCONNECTED){
                    LogUtils.d(this, "onConnectionStateChange: 连接失败");
                    disconnectBluetooth();
                    mHandler.sendEmptyMessage(Constants.BLUETOOTH_DISCONNECTED);
                    updateConnectStatus(false);
                }
            } else if (status == 8) {
                mHandler.sendEmptyMessage(Constants.BLUETOOTH_DISCONNECTED);
                updateConnectStatus(false);
                disconnectBluetooth();
                Looper.prepare();
                Toast.makeText(requireActivity(), "蓝牙连接断开了", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                disconnectBluetooth();
                Looper.prepare();
                Toast.makeText(requireActivity(), "蓝牙连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> services = gatt.getServices();
                for (BluetoothGattService service : services) {
                    if (service.getUuid().toString().equals(Constants.SERVICE_UUID)) {
                        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                        for (BluetoothGattCharacteristic characteristic : characteristics) {
                            if (characteristic.getUuid().toString().equals(Constants.CHARACTERISTIC_UUID)) {
                                mTargetCharacter = characteristic;
                                /* 设置已经找到的Gatt和Characteristic */
                                SendAndReceiveBluetoothMessages.mTargetCharacter = mTargetCharacter;
                                SendAndReceiveBluetoothMessages.mBluetoothGatt = mBluetoothGatt;
                                mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                                mDescriptor = characteristic.getDescriptor(UUID.fromString(Constants.UUID_DESCRIPTOR));
                                mDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                mBluetoothGatt.writeDescriptor(mDescriptor);
                            }

                        }
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                LogUtils.d(this, "onCharacteristicRead: 读特征值被调用");
                UUID uuid = characteristic.getUuid();
                String valueStr = new String(characteristic.getValue());
                LogUtils.d(this, "onCharacteristicRead: 数据是---->" + valueStr +
                        "\r\n" + "   UUID --> " + uuid);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                LogUtils.d(this, "onCharacteristicWrite: 写特征值被调用");
                UUID uuid = characteristic.getUuid();
                String valueStr = new String(characteristic.getValue());
                LogUtils.d(this, "onCharacteristicWrite: ---> 写特征UUID ---> " + uuid +
                        "\r\n" + "  value --> " + valueStr);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            LogUtils.d(this, "onCharacteristicChanged: 特征值改变");
            UUID uuid = characteristic.getUuid();
            String valueStr = null;
            try {
                valueStr = new String(characteristic.getValue(), 0, characteristic.getValue().length, "GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] value = characteristic.getValue();
            LogUtils.d(this, "onCharacteristicChanged:  数据valueStr---->" + valueStr +
                    "\r\n" + "   UUID --> " + uuid);
            LogUtils.d(this, "onCharacteristicChanged:  数据value---->" + Arrays.toString(value) +
                    "\r\n" + "   UUID --> " + uuid);
            //todo:接收到数据的处理

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                LogUtils.d(this, "onDescriptorRead: 读描述值");
                UUID uuid = descriptor.getUuid();
                String valueStr = Arrays.toString(descriptor.getValue());
                LogUtils.d(this, "onDescriptorRead: UUID --> " + uuid +
                        "\r\n" + "  读描述值是---> " + valueStr);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                LogUtils.d(this, "onDescriptorWrite: 写描述值");
                UUID uuid = descriptor.getUuid();
                String valueStr = Arrays.toString(descriptor.getValue());
                LogUtils.d(this, "onDescriptorWrite: UUID --> " + uuid +
                        "\r\n" + "  读描述值是---> " + valueStr);
            }
        }
    };

    /**
     * 更新蓝牙的连接状态
     * 回调里不能更新UI
     * runOnUiThread
     * @param status 状态
     */
    private void updateConnectStatus(boolean status) {
        if (status) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mConnectedDeviceName.setText(mConnectedDeviceName1);
                    mConnectedDeviceAddress.setText(mConnectedDevicesAddress1);
                    mControllerOfButton.setVisibility(View.VISIBLE);
                    mBluetoothRCV.setVisibility(View.GONE);
                    LogUtils.d(this, "更新蓝牙连接状态为连接成功");
                }
            });
        } else {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mConnectedDeviceName.setText("未连接");
                    mConnectedDeviceAddress.setText("未连接");
                    mControllerOfButton.setVisibility(View.GONE);
                    mBluetoothRCV.setVisibility(View.VISIBLE);
                    LogUtils.d(this, "更新蓝牙连接状态为未连接");
                }
            });
        }
    }


    @Override
    protected void initListener() {
        /* 设置按键 */
        /* 4个蓝牙控制按钮，2个命令按钮的设置监听 */
        mOpenBluetooth.setOnClickListener(this);
        mSearchBluetooth.setOnClickListener(this);
        mDisconnectedBluetooth.setOnClickListener(this);
        mOffBluetooth.setOnClickListener(this);
        mOpenDoorComment.setOnClickListener(this);
        mCloseDoorComment.setOnClickListener(this);
    }


    /**
     * 按钮的点击事件
     * @param view 控件
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.turn_on_bluetooth_btn:
                /* 开启蓝牙 */
                turnOnBluetooth();
                break;
            case R.id.turn_off_bluetooth_btn:
                /* 延时关闭蓝牙 */
                turnOffBluetoothDelayed();
                break;
            case R.id.bluetooth_disconnected_btn:
                /* 断开连接 */
                disconnectBluetooth();
                break;
            case R.id.search_bluetooth_btn:
                if (mSearchBluetoothState.getText().equals("搜索设备")) {
                    /* 搜索设备 */
                    onSearchDevices();
                } else if (mSearchBluetoothState.getText().equals("搜索设备")) {
                    /* 停止搜索蓝牙 */
                    offSearchDevices();
                }
                break;
            /* 按钮点击发送数据 */
            case R.id.bluetooth_open_door_comment_btn:
                /* 发送开门指令 */
                String openComment = "ON";
                new sendDataThread(openComment);
                break;
            case R.id.bluetooth_close_door_comment_btn:
                /* 发送关门指令 */
                String closeComment = "OFF";
                new sendDataThread(closeComment);
                break;
        }
    }

    /**
     * 停止搜索蓝牙
     */
    @SuppressLint("MissingPermission")
    private void offSearchDevices() {
        if (mBluetoothAdapter != null) {
            /* 手动点击了停止，移除延时停止搜索蓝牙的回调 */
            mHandler.removeCallbacks(offSearchBluetoothDelayed);
            mBluetoothLeScanner.startScan(scanCallBack);
            mSearchBluetoothState.setText("搜索设备");
        }
    }

    /**
     * 搜索蓝牙结果的回调
     */
    public ScanCallback scanCallBack = new ScanCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            int rssi ;
            if (!(mBluetoothRecyclerviewAdapter.devices.contains(device)) && (device.getName() != null)
                    && !(device.getName().contains(":"))) {
                rssi = result.getRssi();
                mBluetoothRecyclerviewAdapter.addVice(device, rssi);
                LogUtils.d(this, "scanCallback device = " + device);
                LogUtils.d(this, "scanCallback rssi = " + rssi);
            }
        }
    };

    /**
     * 搜索蓝牙
     */
    @SuppressLint("MissingPermission")
    private void onSearchDevices() {
        /* 8S后停止搜索 */
        if (mBluetoothAdapter.isEnabled()) {
            mHandler.postDelayed(offSearchBluetoothDelayed, 15 * 1000);
            mSearchBluetoothState.setText("停止搜索");
            if ((mBluetoothAdapter != null) && (mBluetoothLeScanner != null)) {
                /* 开始搜索蓝牙 */
                mBluetoothLeScanner.startScan(scanCallBack);
            }else {
                Toast.makeText(getContext(), "请稍等，正在启用蓝牙服务！", Toast.LENGTH_SHORT).show();
                mSearchBluetoothState.setText("搜索设备");
            }
        } else {
            Toast.makeText(getContext(), "请打开蓝牙", Toast.LENGTH_SHORT).show();
        }
    }

    Runnable offSearchBluetoothDelayed = new Runnable() {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            if (mBluetoothLeScanner != null) {
                mBluetoothLeScanner.stopScan(scanCallBack);
                mSearchBluetoothState.setText("搜索设备");
            } else {
                Toast.makeText(getContext(), "蓝牙未连接", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 蓝牙断开连接
     */
    @SuppressLint("MissingPermission")
    private void disconnectBluetooth() {
        if (mBluetoothGatt == null) {
            Toast.makeText(getContext(), "未连接！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (mDescriptor == null) {
                Toast.makeText(getContext(), "没有找到描述符！", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mDescriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(mDescriptor);
                mBluetoothGatt.close();
                mBluetoothGatt = null;
                updateConnectStatus(false);
                Toast.makeText(getContext(), "已经断开连接！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 蓝牙断开连接
     */
    @SuppressLint("MissingPermission")
    private void disconnectBluetooth1() {
        if (mBluetoothGatt == null) {
            return;
        } else {
            if (mDescriptor == null) {
                return;
            } else {
                mDescriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(mDescriptor);
                mBluetoothGatt.close();
                mBluetoothGatt = null;
                updateConnectStatus(false);
                Toast.makeText(getContext(), "已经断开连接！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 延时关闭蓝牙
     */
    @SuppressLint("MissingPermission")
    private void turnOffBluetoothDelayed() {
        if (mBluetoothAdapter.isEnabled()) {
            /* 延时2S关闭蓝牙 */
            mHandler.postDelayed(() -> mBluetoothAdapter.disable(), 2*1000);
            mBluetoothRCV.setVisibility(View.GONE);
        } else {
            Toast.makeText(getContext(), "蓝牙已经关闭！", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 打开蓝牙
     */
    private void turnOnBluetooth() {
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //startActivity(intent);
                /* 在APP中打开蓝牙后，需要重新加载蓝牙管理器，设备，适配器 */
                requestDataLauncher.launch(intent);
                mBluetoothRCV.setVisibility(View.VISIBLE);
            }else {
                Toast.makeText(getContext(),"蓝牙已经打开了", Toast.LENGTH_SHORT).show();
                mBluetoothRCV.setVisibility(View.VISIBLE);
            }
        }else {
            Toast.makeText(getContext(),"该设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 数据分包处理
     * @param len 长度
     * @return 数组 eg。 len = 30 ，lens[0] = 1, lens[1] = 10
     * lens = [1,10]
     */
    private int[] dataDivision(int len) {
        int[] lens = new int[2];
        lens[0] = len / 20;
        lens[1] = len % 20;
        return lens;
    }

    /**
     * 发送数据线程
     */
    public class sendDataThread implements Runnable{
        private String sendData = null;
        /* 有参构造 */
        public sendDataThread(String sendData) {
            this.sendData = sendData;
            new Thread(this).start();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            if (sendData != null) {
                byte[] buff;
                /* 将sendData的字符转化为字符以UTF-8编码的字节的数组 */
                buff = sendData.getBytes(StandardCharsets.UTF_8);
                LogUtils.d(this, "run: buff.length = " + buff.length);
                /* 对数据进行分包，20个字节为一组 */
                int[] sendDataLens = dataDivision(buff.length);
                for (int i = 0; i < sendDataLens[0]; i++) {
                    byte[] data20 = new byte[20];
                    for (int j = 0; j < 20; j++) {
                        data20[j] = buff[i * 20 + j];
                    }
                    mTargetCharacter.setValue(data20);
                    mBluetoothGatt.writeCharacteristic(mTargetCharacter);
                }
                /* 休眠20ms */
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /* 处理以20字节为单位分包后多余的字节 */
                if (sendDataLens[1] != 0) {
                    byte[] lastData = new byte[sendDataLens[1]];
                    for (int i = 0; i < sendDataLens[1]; i++) {
                        lastData[i] = buff[sendDataLens[0] * 20 + i];
                    }
                    mTargetCharacter.setValue(lastData);
                    mBluetoothGatt.writeCharacteristic(mTargetCharacter);
                } else {
                    LogUtils.d(this, "run: lastData is null!");
                }
            }
        }
    }
}
