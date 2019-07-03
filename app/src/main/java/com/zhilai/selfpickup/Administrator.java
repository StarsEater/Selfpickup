package com.zhilai.selfpickup;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhilai.commondriver.ILockerInterface;
import com.zhilai.selfpickup.Fragment.fragmentDoors;
import com.zhilai.selfpickup.Fragment.fragmentItems;
import com.zhilai.selfpickup.Util.Constant;
import com.zhilai.selfpickup.Util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Response;



public class Administrator extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    @InjectView(R.id.tab_layout)
    TabLayout tabLayout;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;
    @InjectView(R.id.setting_layout)
    LinearLayout setting_layout;
    @InjectView(R.id.logout)
    TextView loginout;
    @InjectView(R.id.temp1)
    ImageView back;
    @InjectView(R.id.openall)
    ImageView openall;

    private String[] titles=new String[]{"柜门管理","物品查看"};
    private static final String TAG = "Adminstrator";
    private fragmentDoors f_doors;
    private fragmentItems f_items;
    private ArrayList<Fragment> fragments;
    private MyViewPagerAdapter viewPagerAdapter;
    public  ILockerInterface lockerInterface;
    public  ServiceConnection mainConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            lockerInterface = ILockerInterface.Stub.asInterface(service);
            Log.i("Administrator", "onServiceConnected: ");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("Administrator", "onServiceDisconnected: ");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        openall.setOnClickListener(v -> {
                openAll();
        });
        fragments = new ArrayList<>();
        loginout.setOnClickListener(view -> logout());
        back.setOnClickListener(view -> finish());

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (String tab:titles){
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        RefreshLayout();
        tabLayout.setOnTabSelectedListener(this);


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        //viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    public void onTabReselected(TabLayout.Tab tab) { }


    private void logout(){
        final SharedPreferences sharedPreferences =  getSharedPreferences("AdminInfo", MODE_PRIVATE);
        final String id = sharedPreferences.getString("admin", "");
        Bundle mBundle = new Bundle();
        mBundle.putString("admin_id",id);
        HttpUtil.sendLogoutOkHttpRequest(mBundle, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,response.code()+"");
                switch (response.code()){
                    case 200:
                        break;
                    default:
                        Log.d(TAG, "Unknown ERROR!");
                        break;
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
        finish();
    }
    /**
     * 内容页的适配器
     */
    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private String[] titles;

        public MyViewPagerAdapter(FragmentManager fm, String[] titles, List<Fragment> fragments) {
            super(fm);
            this.titles = titles;
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragments.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return fragments.size();
        }



    }
    public  void RefreshLayout(){
        f_doors = new fragmentDoors();
        f_items = new fragmentItems();
        fragments.clear();
        fragments.add(f_doors);
        fragments.add(f_items);
        /**tab标签和内容viewpager*/
        viewPagerAdapter=new MyViewPagerAdapter(getSupportFragmentManager(),titles,fragments);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

    }

    // 每当用户接触了屏幕，都会执行此方法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MainActivity.mLastActionTime = System.currentTimeMillis();
        Log.e(TAG, "正在点击屏幕");
        return super.dispatchTouchEvent(ev);
    }
    /**
     * *绑定函数实现
     **@param  //s e r vice 服务名称
     *@parm connection 连接类
     */
    public void bindService()throws ClassNotFoundException {
        try {
            Intent intent = new Intent("android.intent.action.ZLDRIVER");
            intent.setPackage("com.zhilai.commondriver");
            bindService(intent, mainConnection, BIND_AUTO_CREATE);
            Toast.makeText(this, "bind Success", Toast.LENGTH_SHORT).show();
        } catch (Error e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            bindService();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "onStop: ");

        if (mainConnection != null)
            unbindService(mainConnection);
    }


    public void openAll() {
        Observable.create((ObservableOnSubscribe<String>) e -> {


            for (int i = 0; i < Constant.cabinetTotalNum; i++) {

                String response =   lockerInterface.openLocker(Constant.getBoxId(Constant.cabinetTotalRow, Constant.cabinetTotalCol, "A")[i]);

                e.onNext(response);

                TimeUnit.SECONDS.sleep(1);
            }
            e.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
