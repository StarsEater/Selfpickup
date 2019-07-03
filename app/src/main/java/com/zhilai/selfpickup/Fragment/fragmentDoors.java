package com.zhilai.selfpickup.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zhilai.selfpickup.Adapter.doorsAdapter;
import com.zhilai.selfpickup.Administrator;
import com.zhilai.selfpickup.Object.QueryMess;
import com.zhilai.selfpickup.Object.ZtgInfoBean;
import com.zhilai.selfpickup.R;
import com.zhilai.selfpickup.Util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class fragmentDoors extends Fragment {


    private SwipeRefreshLayout doorsRefreshlayout;
    private RecyclerView mRecyclerView;
    private GridLayoutManager layoutManager;
    private com.zhilai.selfpickup.Adapter.doorsAdapter doorsAdapter;
    private List<ZtgInfoBean> mboxStatusList;
    private String TAG = "fragmentDoors";
    private Administrator a;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_door, container, false);
        doorsRefreshlayout = view.findViewById(R.id.doors_refreshlayout);
        mRecyclerView = view.findViewById(R.id.doors_recyclerView);

        a = (Administrator) getActivity();
        mboxStatusList = new ArrayList<>();
        layoutManager = new GridLayoutManager(getContext(), Constant.cabinetTotalCol);
        mRecyclerView.setLayoutManager(layoutManager);
        getBoxStatusInfo();

        doorsRefreshlayout.setOnRefreshListener(() -> {
            getDoorsData();
            doorsAdapter.notifyDataSetChanged();
            doorsRefreshlayout.setRefreshing(false);
        });
        return view;
    }

    void getDoorsData() {
        Observable.create((e) -> {
            mboxStatusList.clear();
            for (int i = 0; i < Constant.cabinetTotalNum; i++) {
                String s = a.lockerInterface.getLocker(Constant.getBoxId(Constant.cabinetTotalRow, Constant.cabinetTotalCol, "A")[i]);
                String t = s + "#" + i;
                e.onNext(t);
            }
            e.onComplete();
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object value) {
                        String s = (String) value;
                        int i = Integer.parseInt(s.split("#")[1]);
                        mboxStatusList.add(
                                new ZtgInfoBean(i + 1,
                                        new Gson().
                                                fromJson(s.split("#")[0], QueryMess.class))
                        );
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        doorsAdapter.notifyDataSetChanged();
                    }
                });
              /*****/
//           mboxStatusList.add(new ZtgInfoBean(i+1));

    }
    /**
     * 从硬件上获取自提柜的数组
     */
    /***/
    void getBoxStatusInfo() {

        Observable.create((e) -> {
                    getDoorsData();

            e.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object value) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        doorsAdapter = new doorsAdapter(mboxStatusList, a);
                        mRecyclerView.setAdapter(doorsAdapter);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
