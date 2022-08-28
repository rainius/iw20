package com.dmtech.iw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dmtech.iw.databinding.ActivityMainBinding;
import com.dmtech.iw.databinding.MainDrawerLayoutBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    //视图绑定对象
    MainDrawerLayoutBinding mBinding;
    //抽屉按钮对象
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //导入视图绑定对象
        mBinding = MainDrawerLayoutBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        //将布局中的Toolbar设定为ActionBar
        setSupportActionBar(mBinding.mainView.mainToolbar);
        // 创建抽屉按钮
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mBinding.drawer,    //面向此抽屉
                mBinding.mainView.mainToolbar,  //属于此工具栏
                R.string.open,
                R.string.close);
        // 使按钮监听抽屉状态（可以不经过抽屉按钮操作抽屉）
        mBinding.drawer.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //同步抽屉按钮状态
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 绑定预先定义好的菜单资源
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add) { //判定是否点击添加图标
            //TODO: 输出Snackbar消息
            Snackbar.make(mBinding.mainView.getRoot(),
                    "增加新位置",
                    Snackbar.LENGTH_SHORT)
                    .show();
            //TODO: 输出日志
            Log.d("iWeather", "增加新位置");
            //防止被其它代码处理
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}












