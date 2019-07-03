package com.zhilai.selfpickup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhilai.selfpickup.widget.Keyboard;
import com.zhilai.selfpickup.widget.PayEditText;

import butterknife.ButterKnife;

public class PickupCode extends AppCompatActivity {


    private PayEditText payEditText;
    private Keyboard keyboard;
    private ImageView back;


    private static final String[] KEY = new String[] {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "<<", "0", ""
    };
    private String TAG = "PickupCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_code);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        payEditText = (PayEditText) findViewById(R.id.PayEditText_pay);
        keyboard = (Keyboard) findViewById(R.id.KeyboardView_pay);
        back = findViewById(R.id.temp1);
        keyboard.setKeyboardKeys(KEY);

        keyboard.setOnClickKeyboardListener((position, value) -> {
            if (position < 11 && position != 9) {
                payEditText.add(value);
            } else if (position == 9) {
                payEditText.remove();
            }else if (position == 11) {
                //finish();
            }
        });

        /**
         * 当密码输入完成时的回调
         */
        payEditText.setOnInputFinishedListener(password -> {
            //当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
            //final String carrier_code = payEditText.getText();
            final String carrier_code = password;
            if (!carrier_code.isEmpty()) {
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("carrier_code", carrier_code);
                //设置返回数据
                PickupCode.this.setResult(RESULT_OK, intent);
                //关闭Activity
                PickupCode.this.finish();
            }
            else{
                runOnUiThread(() -> Toast.makeText(PickupCode.this,"请重新输入完整取件码",Toast.LENGTH_SHORT).show());

            }



           // Toast.makeText(getApplication(), "您的密码是：" + password, Toast.LENGTH_SHORT).show();
        });

        back.setOnClickListener(view -> {
            //把返回数据存入Intent
            Intent intent = new Intent();
            intent.putExtra("boxNoC","");
            intent.putExtra("carrier_code", "");
            //设置返回数据
            PickupCode.this.setResult(RESULT_OK, intent);
            //关闭Activity
            PickupCode.this.finish();
        });
    }
    // 每当用户接触了屏幕，都会执行此方法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MainActivity.mLastActionTime = System.currentTimeMillis();
        Log.e(TAG, "正在点击屏幕");
        return super.dispatchTouchEvent(ev);
    }
}

