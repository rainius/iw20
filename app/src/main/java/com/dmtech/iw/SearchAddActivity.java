package com.dmtech.iw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dmtech.iw.databinding.ActivitySearchAddBinding;

public class SearchAddActivity extends AppCompatActivity implements View.OnClickListener {

    //视图绑定属性
    private ActivitySearchAddBinding mBindings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充（inflate）绑定对象
        mBindings = ActivitySearchAddBinding.inflate(getLayoutInflater());
        setContentView(mBindings.getRoot());
        //setContentView(R.layout.activity_search_add);
        // 取消按钮
        mBindings.btnCancel.setOnClickListener(this);
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