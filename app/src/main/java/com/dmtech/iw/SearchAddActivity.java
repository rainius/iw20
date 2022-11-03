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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dmtech.iw.databinding.ActivitySearchAddBinding;
import com.dmtech.iw.entity.Basic;
import com.dmtech.iw.entity.SearchInfos;
import com.dmtech.iw.entity.SearchResult;
import com.dmtech.iw.http.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchAddActivity extends AppCompatActivity implements View.OnClickListener {

    //视图绑定属性
    private ActivitySearchAddBinding mBindings;
    //列表视图适配器
    private ArrayAdapter<String> mAdapter;
    //搜索得到的位置名称列表
    private List<String> mLocationLabels;

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
                //清空名字列表
                mLocationLabels.clear();
                //当搜索成功时，用搜索到的结果重新填充名字列表
                if ("ok".equals(searchInfos.getStatus())) {
                    Log.d("iWeather", "查询结果数目：" + searchInfos.getBasic().size());

                    for (Basic basic : searchInfos.getBasic()) {
                        //搜索的地点，国家，上级行政区
                        String label = basic.getLocation() + "，"
                                + basic.getCnty()
                                + basic.getAdmin_area();
                        //加入到名字列表
                        mLocationLabels.add(label);
                    }
                }
                //在UI线程通知列表视图数据更新了
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
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
        //初始化位置名称列表
        mLocationLabels = new ArrayList<>();
        //临时测试数据：
        mLocationLabels.add("珠穆朗玛");
        mLocationLabels.add("马里亚纳");
        //测试数据~
        //建立适配器对象
        mAdapter = new ArrayAdapter<>(this, //上下文对象
                android.R.layout.simple_list_item_1,    //列表项布局
                mLocationLabels     //要显示的名字字串列表
        );
        //组装列表视图
        mBindings.listview.setAdapter(mAdapter);
        mBindings.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //在此处理列表项的点击
                TextView textView = (TextView) view;
                Log.d("iWeather", "你选中了：" + textView.getText());
            }
        });
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