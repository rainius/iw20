package com.dmtech.iw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dmtech.iw.databinding.DrawerItemLayoutBinding;
import com.dmtech.iw.databinding.MainDrawerLayoutBinding;
import com.dmtech.iw.entity.Basic;
import com.dmtech.iw.entity.DaoSession;
import com.dmtech.iw.entity.HeWeather6;
import com.dmtech.iw.event.MessageEvent;
import com.dmtech.iw.http.HttpHelper;
import com.dmtech.iw.ui.WeatherFragment;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements WeatherFragment.OnWeatherLoadedCallback {

    //视图绑定对象
    MainDrawerLayoutBinding mBinding;
    //抽屉位置列表适配器
    private DrawerLocationsAdapter mDrawerLocatonsAdapter;
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
                    //获取天气对象并确保有效
                    HeWeather6 weather = fragment.getWeather();
                    if (weather == null) {
                        return;
                    }
                    //取得基本信息对象
                    Basic basic = weather.getBasic();
                    //已当前具体位置名称为主标题
                    String title = basic.getLocation();
                    //已当前位置所述国家、行政区划为主标题
                    String subTitile =
                            basic.getAdmin_area() + "，" + basic.getCnty();
                    //更新工具栏标题
                    mBinding.mainView.mainToolbar.setTitle(title);
                    mBinding.mainView.mainToolbar.setSubtitle(subTitile);

                    //更新抽屉当前位置高亮
                    mDrawerLocatonsAdapter.notifyDataSetChanged();
                }
            };

    // 暂时用来产生天气位置的测试方法
    private void fillTestFragments() {
        //根据位置ID列表依次创建Fragment
        for (String locationId : HttpHelper.LOCATION_IDS) {
            //mFragments.add(WeatherFragment.newInstance(locationId));
            WeatherFragment wf = WeatherFragment.newInstance(locationId);
            wf.setOnWeatherLoadedCallback(this);
            mFragments.add(wf);
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
        mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawer,    //面向此抽屉
                mBinding.mainView.mainToolbar,  //属于此工具栏
                R.string.open,
                R.string.close);
        // 使按钮监听抽屉状态（可以不经过抽屉按钮操作抽屉）
        mBinding.drawer.addDrawerListener(mDrawerToggle);
        //为Fragment列表填充测试数据
        //fillTestFragments();
        //获取ViewPager对象
        mViewPager = mBinding.mainView.viewpager;
        //创建适配器对象
        mAdapter = new WeatherFragmentAdapter(this);
        //设定适配器
        mViewPager.setAdapter(mAdapter);
        //注册监听回调
        mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback);
        //配置抽屉中的位置列表
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mBinding.drawerView.rvLocations.setLayoutManager(lm);
        mDrawerLocatonsAdapter = new DrawerLocationsAdapter();
        mBinding.drawerView.rvLocations.setAdapter(mDrawerLocatonsAdapter);

        //加载全部位置
        loadLocationsToUI();

        //验证拼装获取某城市天气数据的API
        testHttpHelperGetUrl();
        //注册成为EventBus订阅者
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销订阅
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.d("iWeather", "数据库更新事件");
        loadLocationsToUI();
    }

    private void loadLocationsToUI() {
        //读取数据库中的位置集合
        //获取数据库会话对象
        DaoSession session =
                ((IWeatherApp) getApplication()).getDaoSession();
        //加载全部已添加的数据库记录
        List<Basic> locations = session.getBasicDao().loadAll();
        //刷新抽屉中的位置列表内容
        mDrawerLocatonsAdapter.setLocations(locations);
        //清空现有Fragment列表
        mFragments.clear();
        //重建WeatherFragment列表
        for (Basic location : locations) {
            //依次创建WeatherFragment实例
            WeatherFragment wf =
                    WeatherFragment.newInstance(location.getCid());
            wf.setOnWeatherLoadedCallback(this);    //设置回调
            //加入列表
            mFragments.add(wf);
        }
        //通知ViewPager刷新
        mAdapter.notifyDataSetChanged();
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
            Snackbar.make(mBinding.mainView.getRoot(),
                    "增加新位置",
                    Snackbar.LENGTH_SHORT)
                    .show();
            Log.d("iWeather", "增加新位置");
            //启动搜索添加页面
            Intent intent =
                    new Intent(this, SearchAddActivity.class);
            startActivity(intent);
            //防止被其它代码处理
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWeatherLoaded() {
        //TODO:触发标题更新
        //取当前页面序号
        int position = mViewPager.getCurrentItem();
        //直接触发页面切换事件，促使其更新标题
        mOnPageChangeCallback.onPageSelected(position);
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

    //抽屉RecyclerView适配器
    private class DrawerLocationsAdapter extends RecyclerView.Adapter<DrawerItemHolder> {
        //需要展示的位置列表
        private List<Basic> locations = new ArrayList<>();
        //设定列表中要显示的位置
        public void setLocations(List<Basic> locations) {
            this.locations = locations;
            Log.d("iWeather", "DrawerLocationsAdapter 获得新位置");
            //更新位置后刷新列表
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DrawerItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //在此为条目创建视图容器对象
            View itemView = getLayoutInflater().inflate(
                    R.layout.drawer_item_layout, parent,false);
            return new DrawerItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull DrawerItemHolder holder, int position) {
            // 在此绑定条目数据内容
            //获取条目对应的位置基本信息对象
            Basic itemLocation = locations.get(position);
            //设定当前位置名字
            holder.itemBinding.tvLocation.setText(itemLocation.getLocation());
            //高亮显示当前位置
            int currItem = mViewPager.getCurrentItem();
            if (position == currItem) {
                holder.itemBinding.tvLocation.setTextColor(
                        getResources().getColor(R.color.yellow_600));
            } else {
                holder.itemBinding.tvLocation.setTextColor(
                        getResources().getColor(R.color.white));
            }
            //设定当前位置所述国家（地区）及行政区
            String admin = itemLocation.getCnty() + "，" + itemLocation.getAdmin_area();
            holder.itemBinding.tvAdmin.setText(admin);
            //选择位置并使ViewPager跳转
            holder.itemBinding.drawerItemContainer.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击位置条目：切换主视图位置，然后关闭抽屉
                    //ViewPager平滑滚动至所选位置
                    mViewPager.setCurrentItem(
                            holder.getAdapterPosition(),
                            true);
                    //抽屉关闭，并向左滑出
                    mBinding.drawer.closeDrawer(Gravity.LEFT);
                }
            });
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }
    }


    //抽屉位置列表RecyclerView单项容器
    private class DrawerItemHolder extends RecyclerView.ViewHolder {

        DrawerItemLayoutBinding itemBinding;

        public DrawerItemHolder(@NonNull View itemView) {
            super(itemView);
            //绑定到列表条目视图
            itemBinding = DrawerItemLayoutBinding.bind(itemView);
        }
    }

}












