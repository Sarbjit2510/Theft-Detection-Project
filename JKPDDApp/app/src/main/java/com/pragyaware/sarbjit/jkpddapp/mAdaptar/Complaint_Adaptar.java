package com.pragyaware.sarbjit.jkpddapp.mAdaptar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.mActivity.TheftActivity;
import com.pragyaware.sarbjit.jkpddapp.mModel.ComplaintModel;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sarbjit on 05/13/2017.
 */
public class Complaint_Adaptar extends RecyclerView.Adapter<Complaint_Adaptar.ViewHolder> {

    private Context context;
    private List<ComplaintModel> complaintModels;

    public Complaint_Adaptar(Context context, List<ComplaintModel> complaintModels) {
        this.context = context;
        this.complaintModels = complaintModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_compId.setText("Complaint Id : " + complaintModels.get(position).getID());
        holder.tv_compDate.setText("Date : " + complaintModels.get(position).getComplaintStamp());
        holder.tv_compStatus.setText("Status : " + complaintModels.get(position).getComplaintStat());
        holder.tv_compAddress.setText("Address : " + complaintModels.get(position).getComplaintAddress());
        if (complaintModels.get(position).getComplaintStat().equalsIgnoreCase(com.pragyaware.sarbjit.jkpddapp.common.Constants.PENDING_STATUS)){
            holder.tv_compStatus.setTextColor(Color.parseColor("#e84855"));
        } else {
            holder.tv_compStatus.setTextColor(Color.parseColor("#33658a"));
        }
        String imgId = complaintModels.get(position).getComplaintMedia().split(",")[0];
        String url = Constants.IMG_URL + imgId + "|80|80|40";
        Picasso.with(context).load(url).into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return complaintModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_compId, tv_compDate, tv_compStatus, tv_compAddress;
        Button trackButton;
        ImageView iv_image;

        ViewHolder(View itemView) {
            super(itemView);
            tv_compId = (TextView) itemView.findViewById(R.id.tv_compId);
            tv_compDate = (TextView) itemView.findViewById(R.id.tv_compDate);
            tv_compStatus = (TextView) itemView.findViewById(R.id.tv_compStatus);
            tv_compAddress = (TextView) itemView.findViewById(R.id.tv_compAddress);
            trackButton = (Button) itemView.findViewById(R.id.btnTrack);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComplaintModel model = complaintModels.get(getAdapterPosition());
                    String[] strings = {model.getID(), model.getComplaintAddress(), model.getComplaintComment(), model.getComplaintDefaulterAcc(),
                            model.getComplaintDefaulterExists(), model.getComplaintDefaulterPenality(), model.getComplaintMedia(), model.getComplaintOfficer(),
                            model.getComplaintOfficerComment(), model.getComplaintStamp(), model.getComplaintStat(), model.getComplaintOfficerMedia()};
                    context.startActivity(new Intent(context, TheftActivity.class).putExtra("data", strings));
                }
            });

        }
    }
}
