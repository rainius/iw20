package com.dmtech.iw.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmtech.iw.R;
import com.dmtech.iw.databinding.FragmentWeatherBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    // 外部初始化参数名字
    // TODO: 根据需要增减数量、修改名字
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    //外部传入名字参数的参数名
    private static final String ARG_NAME = "name";

    // Fragment含有的属性，由外部参数来初始化
    // TODO: 根据需要增减数量、修改类型和命名
    //名字属性
    private String mName;

    public String getName() {
        return mName;
    }

    // 缺省构造方法，务必保持为空
    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * 用来创建本Fragment的工厂方法，由此传入所需的参数，返回本Fragment的一个实例
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: 根据需要增减参数数量、改变参数类型
//    public static WeatherFragment newInstance(String param1, String param2) {
    public static WeatherFragment newInstance(String name) {
        //生成一个空的实例
        WeatherFragment fragment = new WeatherFragment();
        //用Bundle对象打包传入的参数
        Bundle args = new Bundle();
        //参数的名-值要对应
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        //填充传入的名字
        args.putString(ARG_NAME, name);
        //将打包好的参数设置给本Fragment的实例
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 读取传入的参数值并赋予对应的属性
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
            //通过参数名读取其值并赋予mName属性
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //创建绑定对象
        FragmentWeatherBinding binding =
                FragmentWeatherBinding.inflate(inflater);
        //TODO: 为文本视图设置内容
        binding.tvName.setText(mName);
        //返回根视图对象
        return binding.getRoot();
    }
}