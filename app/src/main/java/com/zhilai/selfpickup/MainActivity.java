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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.zhilai.commondriver.ILockerInterface;
import com.zhilai.commondriver.IScannerCallback;
import com.zhilai.selfpickup.Object.AdminInfo;
import com.zhilai.selfpickup.Object.DistributionResponse;
import com.zhilai.selfpickup.Object.FinishResponse;
import com.zhilai.selfpickup.Object.HardInfo;
import com.zhilai.selfpickup.Object.LoginResponse;
import com.zhilai.selfpickup.Object.OpenLockerRet;
import com.zhilai.selfpickup.Object.ScannerRet;
import com.zhilai.selfpickup.Object.TakeFinshResponse;
import com.zhilai.selfpickup.Object.TakeResponse;
import com.zhilai.selfpickup.Object.VerifyResponse;
import com.zhilai.selfpickup.Util.Constant;
import com.zhilai.selfpickup.Util.HttpUtil;
import com.zhilai.selfpickup.Util.SaxXml;


import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.cupboard)
    TextView cupboardText;
    @InjectView(R.id.card1)
    CardView add;
    @InjectView(R.id.card2)
    CardView take;
    @InjectView(R.id.temp1)
    ImageView administrator;
    @InjectView(R.id.callText)
    TextView callText;
    @InjectView(R.id.superf)
    TextView superf;
    @InjectView(R.id.temp2)
    ImageView im;
    /**A01-16 shupai*/
    private String TAG = "MainActivity";
    private String[] items = {"扫码取件","取件码取件"};
    private Timer mTimer; // 计时器，每1秒执行一次任务
    private MainActivity.MyTimerTask mTimerTask; // 计时任务，判断是否未操作时间到达ns
    public static long mLastActionTime; // 上一次操作时间
    public  ILockerInterface lockerInterface;
    private String resultNo;
    private boolean  isUser;
    private ScannerRet scannerRet;
    private String ztgID;
    private ServiceConnection mainConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            lockerInterface = ILockerInterface.Stub.asInterface(service);
            Log.i("MainActivity", "onServiceConnected: ");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("MainActivity", "onServiceDisconnected: ");
        }
    };
    private SharedPreferences s;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        s = getSharedPreferences("AdminInfo", MODE_PRIVATE);
        edit = s.edit();

        GetCabinetInfo();
        if(!Constant.superF)
            if(s.getBoolean("remember",false)){
                initUI(s.getString("adminName",""),
                        s.getString("adminPhone",""),
                        s.getString("location",""));
            } else
                GetAdministorInfo();
        startTimer();

        /**调试用，可以删除*/
        im.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);

        });
    }

    @OnClick({R.id.card1, R.id.card2, R.id.temp1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card1:
                /**送货*/
                isUser = false;
                AlertDialog dialogs = new AlertDialog.Builder(this).setTitle("选择送货方式").setIcon(R.drawable.ic_sentiment_satisfied_black_24dp)
                        .setSingleChoiceItems(items, -1, (dialog1, which) -> {
                            switch (which){
                                case 0:
                                    if(Constant.superF)
                                        startQrCode();
                                    else
                                        startQRCode();
                                    break;
                                case 1:
                                    EnterCode();
                                    break;
                                default:
                                    break;
                            }
                            dialog1.dismiss();
                        }).create();
                dialogs.show();
                break;
            case R.id.card2:
                 /**取货*/
                isUser = true;
                AlertDialog dialogt = new AlertDialog.Builder(this).setTitle("选择取件方式").setIcon(R.drawable.ic_sentiment_satisfied_black_24dp)
                        .setSingleChoiceItems(items, -1, (dialog1, which) -> {
                            switch (which){
                                case 0:
                                    if(Constant.superF)
                                         startQrCode();
                                    else
                                         startQRCode();
                                    break;
                                case 1:
                                    EnterCode();
                                    break;
                                default:
                                    break;
                            }
                            dialog1.dismiss();
                        }).create();
                dialogt.show();
                break;
            case R.id.temp1:
                /**管理员*/
                if(Constant.superF)
                    startActivity(new Intent(MainActivity.this,Administrator.class));
                else
                    loginDialog();
                break;
        }
    }

    /**登陆请求*/
    void login(final String name, final String pwd){
        final Bundle mBundle = new Bundle();
        HashMap<String,Object> h = new HashMap<>();
        h.put("adminID",name);
        h.put("adminPassword",pwd);
        mBundle.putString("json",new Gson().toJson(h));
        HttpUtil.sendLoginOkHttpRequest(mBundle, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException{
                Log.d(TAG,"response is"+response.code()+"");
                switch (response.code()) {
                    case 200:
                        edit.putString("adminID",name);
                        edit.commit();
                        Intent intent = new Intent(MainActivity.this, Administrator.class);
                        startActivity(intent);
                        break;
                    default:
                        final String errorMes= response.body().string();
                        runOnUiThread(() -> {
                            if (errorMes != null) {
                                String m = new Gson().fromJson(errorMes, LoginResponse.class).getRetMsg();
                               warningMessageShow(m);
                            }
                        });
                        break;
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                warningMessageShow("请检查网络连接");
            }
        });
    }
    /**自定义登录对话框*/
    public void loginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this, R.layout.login_layout, null);
        //设置对话框布局
        dialog.setView(dialogView);
        dialog.show();
        final EditText etName =  dialogView.findViewById(R.id.et_name);
        final EditText etPwd =   dialogView.findViewById(R.id.et_pwd);
        Button btnLogin =  dialogView.findViewById(R.id.btn_login);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnLogin.setOnClickListener(v -> {
            dialog.dismiss();
            /**此处向服务器发送管理员密码、用户名*/
            login(etName.getText().toString(),etPwd.getText().toString());
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }




    /**获取相关柜子的管理员电话和信息*/
    private void GetAdministorInfo(){
        /**呼叫管理员*/
        HttpUtil.sendQueryAdminOkHttpRequest(new Bundle(), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                warningMessageShow("请检查网络连接");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                switch (response.code()){
                    case 200:
                        AdminInfo adminInfo = new Gson().fromJson(response.body().string(),AdminInfo.class);
                        List<AdminInfo.AdminBean> adminBeansList = adminInfo.getAdmin();
                        edit.putString("adminName",adminBeansList.get(0).getAdminName());
                        edit.putString("adminPhone",adminBeansList.get(0).getAdminTel());
                        edit.putString("location",adminInfo.getZtgLocation());
                        edit.putBoolean("remember",true);
                        edit.commit();
                        initUI(adminBeansList.get(0).getAdminName(),adminBeansList.get(0).getAdminTel(),adminInfo.getZtgLocation());
                        break;
                    default:
                        Log.d(TAG, "Unknown ERROR!");
                        break;
                }
            }
        });
    }
    /**获取柜子的行总数 列总数  总数目 柜子ID*/
    private void GetCabinetInfo(){
        try {
            HardInfo hardInfo = SaxXml.sax2xml(getResources().getAssets().open("config_info.xml"));
            Constant.setData(hardInfo.getZtgID(),hardInfo.getCabinetTotalRow(),hardInfo.getCabinetTotalCol(),hardInfo.getCabinetTotalNum());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUI(String name,String tel,String location){
        runOnUiThread(() -> {
            callText.setText("如果有问题请拨打以下电话\n "
                + name + " ( " + tel +" ) ");
            cupboardText.setText(location);
        });
    }



    /***脱机版*/
    /** 开始扫码*/
    private void startQrCode() {
/****/
        try {
               if (lockerInterface==null)
                   runOnUiThread(() -> Toast.makeText(MainActivity.this, "开始扫描二维码"+ " lockerInterface is  null" , Toast.LENGTH_SHORT).show());
               else
                   runOnUiThread(() -> Toast.makeText(MainActivity.this, "开始扫描二维码"+ " lockerInterface is  not null" , Toast.LENGTH_SHORT).show());
            final String message0 = lockerInterface.turnOnScanner(0, 1000 * 20, new IScannerCallback.Stub() {
                    @Override
                    public void onComplete(final String result)  {
                         if(result!=null )
                            try {
                                Log.d(TAG,"扫描结果"+result);
                                lockerInterface.openLocker(Constant.getBoxId(Constant.cabinetTotalRow,Constant.cabinetTotalCol,"A")[Integer.parseInt(result)%Constant.cabinetTotalNum]);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }


                    }
                });
            //runOnUiThread( ()->Toast.makeText(MainActivity.this,"ret"+message0,Toast.LENGTH_SHORT).show());

        } catch (final RemoteException e) {
            e.printStackTrace();
        }
    }

    /**取件码处理*/
    private void dealWithCodeC(String carrier_code) {
        Observable.create((e)->{
            int no = Integer.parseInt(carrier_code);
            e.onNext(lockerInterface.openLocker(Constant.getBoxId(Constant.cabinetTotalRow,Constant.cabinetTotalCol,"A")[no%Constant.cabinetTotalNum]));
            e.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<Object>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Object value) {
                                Toast.makeText(MainActivity.this,(String)value,Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }


                );
    }






    /***联机版*/
    /** 取货扫码内容获取*/
    private void startQRCode(){
        Observable.create((e)->{
            final String message0 = lockerInterface.turnOnScanner(0, 1000 * 20, new IScannerCallback.Stub() {
                @Override
                public void onComplete(final String result) throws RemoteException {
                    if(result==null){
                        resultNo = "!";
                    } else
                        resultNo = result;
                }
            });
            scannerRet = new Gson().fromJson(message0,ScannerRet.class);
            e.onComplete();
          }
        ).subscribeOn(Schedulers.newThread())
         .observeOn(Schedulers.io())
         .subscribe(new Observer<Object>() {
             @Override
             public void onSubscribe(Disposable d) {}

             @Override
             public void onNext(Object value) {}

             @Override
             public void onError(Throwable e) { }

             @Override
             public void onComplete() {
                 switch (scannerRet.getResult_code()){
                     case 0:
                         dealWithMa(resultNo);
                         try {
                             TimeUnit.SECONDS.sleep(20);
                             if(resultNo==null || resultNo.equals("!"))
                                 warningMessageShow("扫码超时");
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         break;
                     default:
                         warningMessageShow(scannerRet.getError_msg());
                         break;
                 }
             }
         });
    }
    /**取货码或送货码输入*/
    private void EnterCode(){
        startActivityForResult(new Intent(MainActivity.this, PickupCode.class), 1);
    }

    //处理码
    private void dealWithMa(String m){
        if(m==null || m.equals("!")){
            return;
        }
        if(isUser){
            dealWithTakeCode(m);
        }else{
            dealWithSendCode(m);
        }

    }

    /**处理取货，向闪客风发验证请求*/
    private void dealWithTakeCode(String ma){
        final Bundle mBundle = new Bundle();
        HashMap<String,Object> h = new HashMap<>();
        h.put("pickupCode",ma);
        mBundle.putString("json",new Gson().toJson(h));
        HttpUtil.sendTakeVerifyOkHttpRequest(mBundle, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                warningMessageShow("请检查网络连接");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                  switch (response.code()){
                      case 200:
                          final TakeResponse takeResponse = new Gson().fromJson(response.body().string(),TakeResponse.class);
                          int x = takeResponse.getCabinetId();
                          Observable.create((e)->{
                              e.onNext(lockerInterface.openLocker
                                      (Constant.getBoxId(Constant.cabinetTotalRow,Constant.cabinetTotalCol,"A")[x]));
                              e.onComplete();
                          } ).subscribeOn(Schedulers.newThread())
                                  .observeOn(Schedulers.io())
                                  .subscribe(new Observer<Object>() {
                                      @Override
                                      public void onSubscribe(Disposable d) {

                                      }

                                      @Override
                                      public void onNext(Object value) {
                                          OpenLockerRet openLockerRet = new Gson().fromJson((String) value,OpenLockerRet.class);
                                          int pickupStatus;
                                          if(openLockerRet.getResult_code()==0){
                                                  pickupStatus = 1;
                                          }else{
                                              pickupStatus = 0;
                                              warningMessageShow(openLockerRet.getError_msg());
                                          }
                                          Map<String,Object> h = new HashMap();
                                          Bundle mBundle = new Bundle();
                                          h.put("orderID",takeResponse.getOrderID());
                                          h.put("pickupStatus",pickupStatus);
                                          mBundle.putString("json",new Gson().toJson(h));
                                          HttpUtil.sendTakeFinishOkHttpRequest(mBundle, new Callback() {
                                              @Override
                                              public void onFailure(Call call, IOException e) {
                                                  warningMessageShow("请检查网络连接");
                                              }

                                              @Override
                                              public void onResponse(Call call, Response response) throws IOException {
                                                      switch (response.code()){
                                                          case 200:
                                                              TakeFinshResponse takeFinshResponse = new Gson().fromJson(response.body().string(),
                                                                      TakeFinshResponse.class);
                                                              String m = "第"+takeFinshResponse.getCabinetRow()+"行第"+takeFinshResponse.getCabinetCol()+"列柜子已打开";
                                                              warningMessageShow(m);
                                                              break;
                                                          default:
                                                              warningMessageShow(new Gson().fromJson(response.body().string(), FinishResponse.class).getRetMsg());
                                                              break;
                                                      }
                                              }
                                          });
                                      }

                                      @Override
                                      public void onError(Throwable e) {

                                      }

                                      @Override
                                      public void onComplete() {

                                      }
                                  });

                          break;
                      default:
                          VerifyResponse verifyResponse = new Gson().fromJson(response.body().string(),VerifyResponse.class);
                          warningMessageShow(verifyResponse.getRetMsg());
                          break;
                  }
            }
        });

    }

    /**处理送货，向闪客风发验证请求*/
    private void dealWithSendCode(String ma){
        final Bundle mBundle = new Bundle();
        HashMap<String,Object> h = new HashMap<>();
        h.put("deliveryCode",ma);
        mBundle.putString("json",new Gson().toJson(h));
        HttpUtil.sendSendVerifyOkHttpRequest(mBundle, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                warningMessageShow("请检查网络连接");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                switch (response.code()){
                    case 200:
                        getAvailableCabinet();
                        break;
                    default:
                        VerifyResponse verifyResponse = new Gson().fromJson(response.body().string(),VerifyResponse.class);
                        warningMessageShow(verifyResponse.getRetMsg());
                        break;
                }
            }
        });
    }





    /**
     * 向后台发送柜子请求编号
     * 先向后台请求柜子分配，之后打开对应柜子，成功，返回成功信息
     * 失败，go on
     * */
    boolean canOpen = false;
    void getAvailableCabinet(){
        for(int i = 0; i < Constant.cabinetTotalNum; i++){
             canOpen = false;
            Bundle mBundle = new Bundle();
            mBundle.putString("ztgID",ztgID);
            HttpUtil.sendDistributeCabinetOkHttpRequest(mBundle, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    warningMessageShow("请检查网络连接");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    switch (response.code()){
                        case 200:
                            DistributionResponse distributionResponse = new Gson().fromJson(response.body().string(),DistributionResponse.class);
                            int x = (distributionResponse.getCabinetRow()-1)* + distributionResponse.getCabinetCol();
                            Observable.create((e)->{
                                e.onNext(lockerInterface.openLocker
                                        (Constant.getBoxId(Constant.cabinetTotalRow,Constant.cabinetTotalCol,"A")[x]));
                                e.onComplete();
                            } ).subscribeOn(Schedulers.newThread())
                                .observeOn(Schedulers.io())
                                .subscribe(new Observer<Object>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Object value) {
                                        OpenLockerRet openLockerRet = new Gson().fromJson((String) value,OpenLockerRet.class);
                                        if(openLockerRet.getResult_code()==0){
                                              canOpen = true;
                                        }else{
                                             canOpen = false;
                                        }
                                        Bundle mBundle = new Bundle();
                                        HashMap<String,Object> h = new HashMap<>();
                                        h.put("ztgID",Constant.ztgID);
                                        h.put("cabinetRow",distributionResponse.getCabinetRow()+"");
                                        h.put("cabinetCol",distributionResponse.getCabinetCol()+"");
                                        h.put("openStatus",openLockerRet.getResult_code()==0?"1":"0");
                                        mBundle.putString("json",new Gson().toJson(h));
                                        HttpUtil.sendOpenCabinetResultOkHttpRequest(mBundle, new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                warningMessageShow("请检查网络连接");
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                   switch (response.code()){
                                                       case 200:

                                                           break;
                                                       default:
                                                           warningMessageShow(new Gson().fromJson(response.body().string(),VerifyResponse.class).getRetMsg());
                                                           break;
                                                   }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                            break;
                        default:
                            break;
                    }


                }
            });
            if(canOpen)
                break;
        }

    }



    /**获取返回取件码和柜子号函数*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String carrier = data.getExtras().getString("carrier_code");
        if (!carrier.equals("")){
            if(Constant.superF)
                dealWithCodeC(carrier);
            else
                dealWithMa(carrier);
        }
    }

    /**
     * *绑定函数实现
     **@param  //service 服务名称
     *@parm connection 连接类
     */
    public void bindService(){
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
        bindService();

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        if (mainConnection != null )
            unbindService(mainConnection);
    }


    /**定时器，一段时间管理员不点击屏幕会自动退出，超时时长，视情况而定 */
    private void startTimer() {
        mTimer = new Timer();
        mTimerTask = new MainActivity.MyTimerTask();
        // 初始化上次操作时间为登录成功的时间
        mLastActionTime = System.currentTimeMillis();
        // 每过1s检查一次
        mTimer.schedule(mTimerTask, 0, 1000);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mLastActionTime = System.currentTimeMillis();
        Log.e(TAG, "正在点击屏幕");
        return super.dispatchTouchEvent(ev);
    }
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(() -> {
                if(Constant.superF )
                      superf.setText("脱机");
                else
                    superf.setText("联机");
            });
            if (System.currentTimeMillis() - mLastActionTime > 1000 * 60 * 1) {
                stopTimer();// 停止计时任务
                resetSprfMain();//退出登录状态
            }
        }
    }
    public void resetSprfMain() {
        startActivity(new Intent(MainActivity.this,Advertisement.class));

    }
    public void stopTimer() {
        mTimer.cancel();
    }

    public void superA(View view) {
        Constant.superF = !Constant.superF;
    }
    void warningMessageShow(String wrongInfo){
        runOnUiThread(() -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(wrongInfo).setIcon(R.drawable.scanner_info)
                    .setPositiveButton("确定", (dialog, which) -> {
                    }).show();
        });
    }
}
