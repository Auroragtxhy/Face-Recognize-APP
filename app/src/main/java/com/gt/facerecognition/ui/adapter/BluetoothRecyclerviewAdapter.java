package com.gt.facerecognition.ui.adapter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gt.facerecognition.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("NonConstantResourceId")
public class BluetoothRecyclerviewAdapter extends  RecyclerView.Adapter<BluetoothRecyclerviewAdapter.innerHolder>{


    private Unbinder mBind;

    @BindView(R.id.bluetooth_device_name_tv)
    public TextView mBluetooth_device_name_tv;

    @BindView(R.id.bluetooth_device_address_tv)
    public TextView mBluetooth_device_address_tv;

    @BindView(R.id.bluetooth_device_strength_tv)
    public TextView mBluetooth_device_strength_tv;

    @BindView(R.id.click_me_2_connect_btn)
    public Button mClick_me_2_connect_btn;

    public List<BluetoothDevice> devices = new ArrayList<>();
    public List<Integer> listRSSI = new ArrayList<>();
    private onItemButtonClickListener mOnItemButtonClickListener;

    @NonNull
    @Override
    public innerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* 填充View */
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
        mBind = ButterKnife.bind(this, itemView);
        return new innerHolder(itemView);
    }

    @SuppressLint({"MissingPermission", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull innerHolder holder, int position) {
        BluetoothDevice device = devices.get(position);
        mBluetooth_device_name_tv.setText(device.getName());
        mBluetooth_device_address_tv.setText(device.getAddress());
        mBluetooth_device_strength_tv.setText(listRSSI.get(position) + "" + "dB");
        mClick_me_2_connect_btn.setOnClickListener(view -> mOnItemButtonClickListener.onItemButtonClick(position));
    }

    @Override
    public int getItemCount() {
        if (devices.size() != 0) {
            return devices.size();
        } else {
            return 0;
        }
    }

    public class innerHolder extends RecyclerView.ViewHolder {
        public innerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 添加设备数据
     * @param device 蓝牙设备
     * @param RSSI 蓝牙设备强度
     */
    @SuppressLint("NotifyDataSetChanged")
    public void addVice(BluetoothDevice device, int RSSI){
        if (!(devices.contains(device))) {
            devices.add(device);
            listRSSI.add(RSSI);
            notifyDataSetChanged();
        }
    }

    public BluetoothDevice getDevice(int position){
        return devices.get(position);
    }


    public interface onItemButtonClickListener{
        void onItemButtonClick(int position);
    }

    public void setOnItemButtonClickListener(onItemButtonClickListener listener) {
        this.mOnItemButtonClickListener = listener;
    }
}
