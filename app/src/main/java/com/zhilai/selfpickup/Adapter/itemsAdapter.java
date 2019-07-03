package com.zhilai.selfpickup.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhilai.selfpickup.Object.ZtgInfoBean;
import com.zhilai.selfpickup.R;
import com.zhilai.selfpickup.Util.Constant;

import java.util.List;

public class itemsAdapter extends RecyclerView.Adapter<itemsAdapter.ViewHolder>{
    private Context mContext;
    private List<ZtgInfoBean> mboxList;

    public itemsAdapter(List<ZtgInfoBean> statusList){
        this.mboxList = statusList;
    }

    @Override
    public itemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_valuables_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final itemsAdapter.ViewHolder holder, int position) {
        final ZtgInfoBean boxStatusInfo = mboxList.get(position);
        holder.boxNoOfItem.setText(position+1+"");
        if(Constant.superF){
            holder.item_image.setBackgroundResource(R.drawable.ic_help_outline_black_24dp);
        } else{
            if (boxStatusInfo.getCabinetUsed().equals("1")) {
                holder.item_image.setBackgroundResource(R.drawable.goods);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mboxList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView item_image;
        TextView boxNoOfItem;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            item_image = itemView.findViewById(R.id.valuables_image);
            boxNoOfItem = itemView.findViewById(R.id.valuables_boxNo);
        }
    }
}
