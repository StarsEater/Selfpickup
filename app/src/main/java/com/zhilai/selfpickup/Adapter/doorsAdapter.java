package com.zhilai.selfpickup.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhilai.selfpickup.Administrator;
import com.zhilai.selfpickup.Object.ZtgInfoBean;
import com.zhilai.selfpickup.R;
import com.zhilai.selfpickup.Util.Constant;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
public class doorsAdapter extends RecyclerView.Adapter<doorsAdapter.ViewHolder>{
    private Context mContext;
    private List<ZtgInfoBean> mboxList;
    private Administrator p;
    private final String openedColor="#1E90FF";
    private final String closedColor="#ffffff";
    public String  TAG = "doorsAdapter";
    public doorsAdapter(List<ZtgInfoBean> statusList, Administrator p){
        this.mboxList = statusList;
        this.p = p;
    }

    @Override
    public doorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_door_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final doorsAdapter.ViewHolder holder, final int position) {
        final ZtgInfoBean boxStatusInfo = mboxList.get(position);
        holder.boxNoOfDoor.setText(position+1+"");
        holder.boxNoOfDoor.setBackgroundColor(
                boxStatusInfo.getOpen()?
                        Color.parseColor(openedColor) :
                        Color.parseColor(closedColor));
        holder.door_image.setBackgroundResource(
                boxStatusInfo.getOpen()?
                        R.drawable.open:
                        R.drawable.closed
        );
        holder.door_image.setOnClickListener(view -> Observable.create((e)->{
             e.onNext(p.lockerInterface.openLocker(Constant.getBoxId(Constant.cabinetTotalRow,Constant.cabinetTotalCol,"A")[position]));
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
                        holder.boxNoOfDoor.setBackgroundColor(
                                Color.parseColor(openedColor) );
                        holder.door_image.setBackgroundResource(
                                R.drawable.open);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }

    @Override
    public int getItemCount() {
        return mboxList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView door_image;
        TextView boxNoOfDoor;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            door_image = itemView.findViewById(R.id.doors_image);
            boxNoOfDoor = itemView.findViewById(R.id.doors_boxNo);

        }
    }
}
