package com.dmtech.iw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.dmtech.iw.databinding.ActivitySearchAddBinding;
import com.dmtech.iw.entity.SearchInfos;
import com.dmtech.iw.entity.SearchResult;
import com.dmtech.iw.http.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchAddActivity extends AppCompatActivity implements View.OnClickListener {

    //视图绑定属性
    private ActivitySearchAddBinding mBindings;
    //控制任务延迟执行的Handler对象
    private Handler mHandler;
    // 发起搜索任务
    private Runnable mStartSearch = new Runnable() {
        @Override
        public void run() {
            //TODO:在此发起搜索
            //从编辑框读取搜索关键字
            String keyWord = mBindings.editKeyword.getText().toString();
            Log.d("iWeather", "开始搜索关键字: " + keyWord);
            runSearch(keyWord);
        }
    };
    //根据关键词进行网络查询
    private void runSearch(String keyWord) {
        //组成搜索URL
        String searchUrl = HttpHelper.getSearchLocationUrl(keyWord);
        Log.d("iWeather", "搜索关键词: " + searchUrl);
        HttpHelper.requestByOkHttp3(searchUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //显示失败的详细信息
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //请求成功，提取响应回来的内容
                String result = response.body().string();
                Log.d("iWeather", "Request OK: " + result);
                //解析查询数据
                Gson gson = new Gson();
                SearchResult searchResult = gson.fromJson(result, SearchResult.class);
                SearchInfos searchInfos = searchResult.getInfos().get(0);
                Log.d("iWeather", "查询状态：" + searchInfos.getStatus());
                Log.d("iWeather", "查询结果数目：" + searchInfos.getBasic().size());
            }
        });
    }

    // 文本输入看守
    private TextWatcher mKeywordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //TODO:在此响应文本变化，参数s为当前变化后的文本
            Log.d("iWeather", "onTextChanged: " + s);
            // 控制流量
            // (1)撤除没有执行的任务
            mHandler.removeCallbacks(mStartSearch);
            // (2)部署新的延迟任务
            mHandler.postDelayed(mStartSearch, 800);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充（inflate）绑定对象
        mBindings = ActivitySearchAddBinding.inflate(getLayoutInflater());
        setContentView(mBindings.getRoot());
        //setContentView(R.layout.activity_search_add);
        // 取消按钮
        mBindings.btnCancel.setOnClickListener(this);
        //监听文本输入
        mBindings.editKeyword.addTextChangedListener(mKeywordWatcher);
        //取得Handler
        mHandler = new Handler(getMainLooper());
    }

    @Override
    public void onClick(View v) {
        //TODO:根据被点击对象v的id处理点击事件
        if (v.getId() == R.id.btn_cancel) {
            //关闭当前窗口即可
            finish();
        }
    }
}