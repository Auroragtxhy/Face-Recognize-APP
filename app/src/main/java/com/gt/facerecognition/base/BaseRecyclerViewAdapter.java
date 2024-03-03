package com.gt.facerecognition.base;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gt.facerecognition.R;
import com.gt.facerecognition.model.domain.FaceRecognition;
import com.gt.facerecognition.utils.Constants;
import com.gt.facerecognition.utils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 作为这几个一起使用LinerLayoutManager的RecyclerView的Adapter，注意使用的Holder是RecyclerView.ViewHolder
 */
public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<FaceRecognition.DataDTO> mDataDTOS = new ArrayList<>();

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.allUser_image_imv)
    public ImageView mAll_User_image_imv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.allUser_name_tv)
    public TextView mAll_User_name_tv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.allUser_time_point_content_tv)
    public TextView mAll_User_time_point_tv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.allUSer_open_or_not_content_tv)
    public TextView mAll_User_open_or_not_tv;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.allUSer_to_check_info_btn)
    public Button mAll_User_to_check_info_btn;
    private onItemButtonOnClickListener mItemButtonOnClickListener;
    private Date mDate;

    /**
     * 1.数据传入
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<FaceRecognition.DataDTO> dataDTOS) {
        mDataDTOS.clear();
        mDataDTOS.addAll(dataDTOS);
        notifyDataSetChanged();
    }



    /**
     * 3.绑定界面
     * onCreateViewHolder()这个方法用于创建条目<Item>的View，返回innerHolder()的实例
     * 这里的innerHolder()需要传进去的是View， 这个View就是条目的View
     * 所以创建一个关于这个条目的布局item_list_view
     *
     * 不过这里是差异化的地方，不同类的条目的View是不一样的，通过子类来实现其传送View的方法，
     * 所以这里需要将getSubView()变为抽象方法，由子类去实现
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        /* 获取子类的ItemView，该抽象函数由子类实现...大家都使用一个item_layout，就不用子类实现了 */
//        View itemView = getSubView(parent, viewType);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_info, parent, false);
        /* ButterKnife绑定获取的ItemView */
        ButterKnife.bind(this, itemView);
        /* 返回innerHolder()的实例 */
        return new innerHolder(itemView);
    }

//    /* 获取子类的ItemView，该抽象函数由子类实现 */
//    protected abstract View getSubView(ViewGroup parent, int viewType);


    /**
     * 5.设置控件
     * 因为共用一个item_layout，所以这个父类可以统一设置数据，如果不是，需要由子类完成
     * 这里仅设置控件
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /* 获取当前实例 */
        FaceRecognition.DataDTO dataDTO = mDataDTOS.get(position);
        String allUser_name = dataDTO.getPerson();
        String allUser_time = dataDTO.getTime();
        LogUtils.d(this, "allUser_time : " + allUser_time);
        /* 格式化字符串 */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            mDate = dateFormat.parse(allUser_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert mDate != null;
        String time = dateFormat2.format(mDate);
        LogUtils.d(this, "time : " + time);

        String allUSer_result = dataDTO.getResult();
        String allUSer_imageUrl = dataDTO.getPhoto();
        /* 设置控件的内容 */
        mAll_User_name_tv.setText(allUser_name);
        mAll_User_time_point_tv.setText(time);
        mAll_User_open_or_not_tv.setText(allUSer_result);
        /* eg.  http://127.0.0.1:8911/face/getPictureInfo?person=小火龙&photo=iTab-k7g7yq.bmp
        * .load(Constants.BASE_URL + "getPictureInfo?person=" + allUser_name + "&photo=" + allUSer_imageUrl)
        */
        /* String imageUrl = getSubImageUrl(allUser_name, allUSer_imageUrl); */
        String imageUrl = Constants.BASE_URL + "getPictureInfo?person=" + allUser_name + "&photo=" + allUSer_imageUrl;
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(mAll_User_image_imv);

        /* 按钮的点击事件 */
        mAll_User_to_check_info_btn.setOnClickListener(view -> {
            /* 外部实现的借接口的调用 */
            mItemButtonOnClickListener.onButtonClick(allUser_name, allUser_time, allUSer_result, imageUrl);
        });
    }

    /* 获取子类的imageURl的抽象方法，由子类实现 */
    //protected abstract String getSubImageUrl(String allUser_name,String allUSer_imageUrl);

    /**
     * 2.getItemCount返回条目的个数

     */
    @Override
    public int getItemCount() {
        if (mDataDTOS != null) {
            return mDataDTOS.size();
        }
        return 0;
    }

    /* 4.初始化控件,初始化控件已经由ButterKnife完成 */
    public class innerHolder extends RecyclerView.ViewHolder {
        public innerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 6.设置点击事件的回调
     * 怎么理解这个回调呢
     * （1）这里定义一个接口onItemButtonOnClickListener，接口里有我们需要的方法onButtonClick，不过方法的具体实现
     * 是由外部实现这个接口载并且来实现这个方法的，我们仅在这里规定方法的返回值，参数，方法名，再说一遍，方法的具体内容由外部实现
     * （2）我们在内部（这里）再定义这样一个接口的类，用来接收外部传入的已经实现的接口
     * private onItemButtonOnClickListener mItemButtonOnClickListener;
     * （3）最后定义一个监听的方法setItemButtonOnClickListener，该方法在外部调用用来将外部实现的接口传入内部，
     * 将外部实现的接口（匿名类）赋给内部提前准备的接口类，
     * this.mItemButtonOnClickListener = listener;
     * 这样内部就可以调用这个mItemButtonOnClickListener接口类就来使用外部实现的方法,例如在按钮的点击事件中
     * mItemButtonOnClickListener.onButtonClick(allUser_name, allUser_time, allUSer_result, imageUrl);
     */
    /* 点击事件的接口和内部方法 */
    public interface onItemButtonOnClickListener {
        void onButtonClick(String name, String time, String result, String imageUrl);
    }

    /* 点击事件的监听 */
    public void setItemButtonOnClickListener(onItemButtonOnClickListener listener) {
        this.mItemButtonOnClickListener = listener;
    }
}
