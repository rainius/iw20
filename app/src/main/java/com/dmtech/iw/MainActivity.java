package com.dmtech.iw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dmtech.iw.databinding.MainDrawerLayoutBinding;
import com.dmtech.iw.entity.Basic;
import com.dmtech.iw.entity.HeWeather6;
import com.dmtech.iw.http.HttpHelper;
import com.dmtech.iw.ui.WeatherFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //视图绑定对象
    MainDrawerLayoutBinding mBinding;
    //抽屉按钮对象
    private ActionBarDrawerToggle mDrawerToggle;
    // 存储各天气位置对应的fragment
    private List<WeatherFragment> mFragments = new ArrayList<>();
    //主视图ViewPager2容器
    private ViewPager2 mViewPager;
    //ViewPager适配器
    private WeatherFragmentAdapter mAdapter;
    //ViewPager翻页监听器
    private ViewPager2.OnPageChangeCallback mOnPageChangeCallback =
            new ViewPager2.OnPageChangeCallback() {
                //重写onPageSelected
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    //将Toolbar标题修改为当前Fragment的名字
                    //获取当前页面
                    WeatherFragment fragment = mFragments.get(position);
                    //获取当前页面名字
                    String title = fragment.getName();
                    //更新工具栏标题
                    mBinding.mainView.mainToolbar.setTitle(title);
                    /*HeWeather6 weather = fragment.getWeather();
                    if (weather == null) {
                        return;
                    }

                    Basic basic = weather.getBasic();
                    String title = basic.getLocation();
                    String subTitile = basic.getAdmin_area() + "，" + basic.getCnty();
                    //更新工具栏标题
                    mBinding.mainView.mainToolbar.setTitle(title);
                    mBinding.mainView.mainToolbar.setSubtitle(subTitile);*/
                }
            };

    // 暂时用来产生天气位置的测试方法
    private void fillTestFragments() {
        //根据位置ID列表依次创建Fragment
        for (String locationId : HttpHelper.LOCATION_IDS) {
            mFragments.add(WeatherFragment.newInstance(locationId));
        }

        for (WeatherFragment f : mFragments) {
            Log.d("iWeather", "添加位置：" +
                    f.getArguments().getString("name"));
        }
    }

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
        //为Fragment列表填充测试数据
        fillTestFragments();
        //获取ViewPager对象
        mViewPager = mBinding.mainView.viewpager;
        //创建适配器对象
        mAdapter = new WeatherFragmentAdapter(this);
        //设定适配器
        mViewPager.setAdapter(mAdapter);
        //注册监听回调
        mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback);

        //验证拼装获取某城市天气数据的API
        testHttpHelperGetUrl();
    }

    private void testHttpHelperGetUrl() {
        String url = HttpHelper.getUrl(HttpHelper.LOCATION_IDS[2]);
        Log.d("iWeather", "testHttpHelperGetUrl: " + url);
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

    //ViewPager适配器类
    private class WeatherFragmentAdapter extends FragmentStateAdapter {

        public WeatherFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            //从列表结构中按对应position取得
            return mFragments.get(position);
        }

        @Override
        public int getItemCount() {
            //取列表结构长度
            return mFragments.size();
        }
    }
}












