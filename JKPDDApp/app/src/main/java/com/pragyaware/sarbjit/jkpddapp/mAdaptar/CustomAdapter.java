package com.pragyaware.sarbjit.jkpddapp.mAdaptar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.pragyaware.sarbjit.jkpddapp.R;
import com.pragyaware.sarbjit.jkpddapp.common.PreferenceUtil;
import com.pragyaware.sarbjit.jkpddapp.mActivity.ViewComplaints;
import com.pragyaware.sarbjit.jkpddapp.mModel.OfficerComplModel;
import com.pragyaware.sarbjit.jkpddapp.mUtil.Constants;
import com.pragyaware.sarbjit.jkpddapp.mUtil.DialogUtil;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private String cID;
    //    private String cAddress;
    private OfficerComplModel complaint;
    private View view;
    private List<OfficerComplModel> dataSet;
    private final static int FADE_DURATION = 1000;

    public CustomAdapter(Context context, List<OfficerComplModel> data) {
        this.context = context;
        this.dataSet = data;
    }

//    public void refreshDataset(List<Complaint> data) {
//        this.dataSet = data;
//        this.notifyDataSetChanged();
//    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

//        view.setOnClickListener(ViewComplaints.myOnClickListener);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        complaint = dataSet.get(listPosition);

        cID = "Complaint ID : " + String.valueOf(complaint.getID());
        String cDate = "Date : " + complaint.getComplaintStamp();
        holder.complaintId.setText(cID);
        holder.complaintDate.setText(cDate);
        if (complaint.getComplaintStat().equalsIgnoreCase("Pending")) {
            holder.complaintStatus.setText("Status : " + complaint.getComplaintStat());
            holder.complaintStatus.setTextColor(Color.parseColor("#e84855"));
        } else if (complaint.getComplaintStat().equalsIgnoreCase("In progress")) {
            holder.complaintStatus.setText("Status : Theft Not Detected");
            holder.complaintStatus.setTextColor(Color.parseColor("#33658a"));
        } else {
            holder.complaintStatus.setText("Status : Theft Detected");
            holder.complaintStatus.setTextColor(Color.parseColor("#33658a"));
        }
        holder.complaintAddress.setText("Address : " + complaint.getComplaintAddress());
//        holder.complaintStatus.setText(cStatus);
//        if (complaint.getComplaintStatus() != null) {
//            if (complaint.getComplaintStat().equalsIgnoreCase(com.pragyaware.sarbjit.jkpddapp.common.Constants.PENDING_STATUS)){
//                holder.complaintStatus.setTextColor(Color.parseColor("#e84855"));
//            } else {
//                holder.complaintStatus.setTextColor(Color.parseColor("#33658a"));
//            }
//        } else {
//            holder.complaintStatus.setTextColor(Color.parseColor("#33658a"));
//        }

        String path = dataSet.get(listPosition).getComplaintMedia().split(",")[0];
        String Url = Constants.IMG_URL + path + "|80|80|40";
        Picasso.with(context).load(Url).into(holder.imageViewIcon);
        setScaleAnimation(holder.itemView);

    }

//    public void removeItem(Complaint complaint) {
//        dataSet.remove(complaint);
//        this.notify();
//    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView complaintId, complaintDate, complaintAddress, /*complaintMarkTo,*/
                complaintStatus;
        ImageView imageViewIcon;


        MyViewHolder(View itemView) {
            super(itemView);
            complaintId = (TextView) itemView.findViewById(R.id.tv_compId);
            complaintDate = (TextView) itemView.findViewById(R.id.tv_compDate);
            this.complaintAddress = (TextView) itemView.findViewById(R.id.tv_compAddress);
//            this.complaintMarkTo = (TextView) itemView.findViewById(R.id.tv_compMarkTo);
            complaintStatus = (TextView) itemView.findViewById(R.id.tv_compStatus);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.iv_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PreferenceUtil.getInstance(context).isOfficer()) {
                        OfficerComplModel complaint1 = dataSet.get(getAdapterPosition());
                        if (!complaint1.getID().equalsIgnoreCase("0")) {
//                        if ("Completed".equals(complaint1.getComplaintStatus()) && "In progress".equals(complaint1.getComplaintStatus())) {
//                            UpdateStatusActivity.submit_btn.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            UpdateStatusActivity.submit_btn.setVisibility(View.VISIBLE);
//                        }
                            if (context instanceof ViewComplaints) {
                                ((ViewComplaints) context).updateStatus(complaint1);
                            }
//                        } else {
//                            DialogUtil.showToast("Status is already update server.", context, false);
//                        }
                        } else {
                            DialogUtil.showToast("This complaint not registered yet on server.", context, false);
                        }
                    }
                }
            });


        }
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

}
