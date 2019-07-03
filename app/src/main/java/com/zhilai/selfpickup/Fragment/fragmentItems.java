package com.zhilai.selfpickup.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.zhilai.selfpickup.Adapter.itemsAdapter;
import com.zhilai.selfpickup.MainActivity;
import com.zhilai.selfpickup.Object.ZtgInfoBean;
import com.zhilai.selfpickup.Object.cabinetInfo;
import com.zhilai.selfpickup.R;
import com.zhilai.selfpickup.Util.Constant;
import com.zhilai.selfpickup.Util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class fragmentItems extends Fragment {

    private RecyclerView mRecyclerView;
    private GridLayoutManager layoutManager;
    private com.zhilai.selfpickup.Adapter.itemsAdapter itemsAdapter;
    private List<ZtgInfoBean> cabinetList;
    private String TAG = "fragmentItems";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        mRecyclerView =  view.findViewById(R.id.valuables_recyclerView);
        layoutManager=new GridLayoutManager(getContext(), Constant.cabinetTotalCol);
        mRecyclerView.setLayoutManager(layoutManager);
        if(!Constant.superF)
            getBoxStatusInfo();
        else{
            cabinetList = new ArrayList();
            for(int i = 0; i < Constant.cabinetTotalNum; i++)
                cabinetList.add(new ZtgInfoBean(i+1));
            UpdateItemsLayoutView();
        }
        return view;
    }
    /**加载柜子排列视图*/
    private void UpdateItemsLayoutView(){
        itemsAdapter = new itemsAdapter(cabinetList);
        mRecyclerView.setAdapter(itemsAdapter);
    }

    /**
     * 从服务器获取自提柜的数组
     */
    void getBoxStatusInfo() {
        final SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("AdminInfo", MODE_PRIVATE);
        final String id = sharedPreferences.getString("admin", "");
        Bundle mBundle = new Bundle();
        mBundle.putString("ztgID",Constant.ztgID);
        HttpUtil.sendShowStatusOkHttpRequest(mBundle, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                warningMessageShow("请检查网络连接");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,response.code()+"");
                switch (response.code()){
                    case 200:
                        cabinetList = new Gson().fromJson(response.body().string(), cabinetInfo.class).getZtgInfo();
                        getActivity().runOnUiThread(() -> UpdateItemsLayoutView());
                        break;
                    default:
                        break;
                }
            }
        });

    }

    void warningMessageShow(String wrongInfo){
         getActivity().runOnUiThread(() -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(wrongInfo).setIcon(R.drawable.scanner_info)
                    .setPositiveButton("确定", (dialog, which) -> {
                    }).show();
        });
    }
}
