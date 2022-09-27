package com.dmtech.iw.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.dmtech.iw.R;
import com.dmtech.iw.databinding.FragmentWeatherBinding;
import com.dmtech.iw.entity.HeWeather6;
import com.dmtech.iw.entity.HeWeatherBean;
import com.dmtech.iw.http.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    //外部传入名字参数的参数名
    private static final String ARG_NAME = "name";

    //名字属性
    private String mName;
    //天气对象
    private HeWeather6 mWeather;

    public String getName() {
        return mName;
    }

    public HeWeather6 getWeather() {
        return mWeather;
    }

    // 缺省构造方法，务必保持为空
    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * 用来创建本Fragment的工厂方法，由此传入所需的参数，返回本Fragment的一个实例
     * @return A new instance of fragment WeatherFragment.
     */
    public static WeatherFragment newInstance(String name) {
        //生成一个空的实例
        WeatherFragment fragment = new WeatherFragment();
        //用Bundle对象打包传入的参数
        Bundle args = new Bundle();
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
        //获取asset管理器（AssetManager）对象
        AssetManager assetManager = getActivity().getAssets();
        // 从asset中的字体文件创建字体对象
        Typeface typeface = Typeface.createFromAsset(
                assetManager,
                "HelveticaNeue Bold.ttf");
        //设定字体
        binding.tvCurTemp.setTypeface(typeface);
        //空出虚拟按键栏位置
        int padding = getVirtualBarHeight(getActivity());
        binding.weatherInfoContainer.setPadding(0, 0, 0, padding);
        //发起网络访问获取天气数据
        requestWeather();
        //返回根视图对象
        return binding.getRoot();
    }

    // 获取虚拟按键栏高度
    private int getVirtualBarHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int height = dm.heightPixels;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        // 计算虚拟按键栏高度
        int virtualbarHeight = realHeight - height;
        if (virtualbarHeight < 0) {
            virtualbarHeight = 0;
        }
        return virtualbarHeight;
    }

    //请求本页面的天气数据
    private void requestWeather() {
        //TODO：在此访问网络获取本页面天气数据
        String locationId = mName;  // 需要取得和风天气地点ID
        Log.d("iWeather", "request weather for: " + locationId);
        // 根据地点ID生成对应的URL
        String url = HttpHelper.getUrl(locationId);
        Log.d("iWeather", "request weather from: " + url);
        // 创建OkHttp客户端对象
        OkHttpClient client = new OkHttpClient();
        // 构建访问url的请求对象
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        //客户端发起请求，并将任务放入异步队列
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //TODO: 处理失败情况
                //请求失败，记录日志
                Log.d("iWeather", "Request failed: " + e.getMessage());
                //显示失败的详细信息
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //TODO: 处理响应
                //请求成功，提取响应回来的内容
                String result = response.body().string();
                Log.d("iWeather", "Request OK: " + result);
                //TODO: 将result转换成 HeWeatherBean 实体对象
                //创建Gson对象
                Gson gson = new Gson();
                //创建HeWeatherBean类型的对象并以实际的天气数据填充
                HeWeatherBean weatherBean =
                        gson.fromJson(result, HeWeatherBean.class);
                //提取天气对象
                mWeather = weatherBean.getHeWeather6().get(0);
                //显示当前位置名称
                Log.d("iWeather", "Weather: " + mWeather.getBasic().getLocation());
                //显示当前位置的上级行政区
                Log.d("iWeather", "Weather: " + mWeather.getBasic().getAdmin_area());
            }
        });
    }

            /*

        // 创建OkHttp客户端对象
        OkHttpClient client = new OkHttpClient();
        // 构建访问url的请求对象
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        //客户端发起请求，并将任务放入异步队列
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //请求失败，记录日志
                Log.d("iWeather", "Request failed: " + e.getMessage());
                //显示失败的详细信息
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //请求成功，提取响应回来的内容
                String result = response.body().string();
                Log.d("iWeather", "Request OK: " + result);
                //用Gson将JSON串转化为实体对象
                Gson gson = new Gson();
                HeWeatherBean weatherBean = gson.fromJson(result, HeWeatherBean.class);
                //提取天气对象
                mWeather = weatherBean.getHeWeather6().get(0);
                Log.d("iWeather", "Weather: " + mWeather.getBasic().getLocation());
                Log.d("iWeather", "Weather: " + mWeather.getBasic().getAdmin_area());

            }
        });
        */
}