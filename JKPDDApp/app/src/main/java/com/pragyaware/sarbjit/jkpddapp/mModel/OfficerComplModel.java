package com.pragyaware.sarbjit.jkpddapp.mModel;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/13/2017.
 */
public class OfficerComplModel extends RealmObject implements Parcelable {

    public static final Creator<OfficerComplModel> CREATOR = new Creator<OfficerComplModel>() {
        @Override
        public OfficerComplModel createFromParcel(Parcel in) {
            return new OfficerComplModel(in);
        }

        @Override
        public OfficerComplModel[] newArray(int size) {
            return new OfficerComplModel[size];
        }
    };
    private String ID, complaintComment, complaintAddress, complaintUser, complaintStamp, complaintMedia, userId, complaintStat;

    private OfficerComplModel(Parcel in) {
        ID = in.readString();
        complaintComment = in.readString();
        complaintAddress = in.readString();
        complaintUser = in.readString();
        complaintStamp = in.readString();
        complaintMedia = in.readString();
        userId = in.readString();
        complaintStat = in.readString();
    }

    public OfficerComplModel() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getComplaintComment() {
        return complaintComment;
    }

    public void setComplaintComment(String complaintComment) {
        this.complaintComment = complaintComment;
    }

    public String getComplaintAddress() {
        return complaintAddress;
    }

    public void setComplaintAddress(String complaintAddress) {
        this.complaintAddress = complaintAddress;
    }

    public String getComplaintUser() {
        return complaintUser;
    }

    public void setComplaintUser(String complaintUser) {
        this.complaintUser = complaintUser;
    }

    public String getComplaintStamp() {
        return complaintStamp;
    }

    public void setComplaintStamp(String complaintStamp) {
        this.complaintStamp = complaintStamp;
    }

    public String getComplaintMedia() {
        return complaintMedia;
    }

    public void setComplaintMedia(String complaintMedia) {
        this.complaintMedia = complaintMedia;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComplaintStat() {
        return complaintStat;
    }

    public void setComplaintStat(String complaintStat) {
        this.complaintStat = complaintStat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(complaintComment);
        parcel.writeString(complaintAddress);
        parcel.writeString(complaintUser);
        parcel.writeString(complaintStamp);
        parcel.writeString(complaintMedia);
        parcel.writeString(userId);
        parcel.writeString(complaintStat);
    }
}
