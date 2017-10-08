package com.pragyaware.sarbjit.jkpddapp.mModel;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sarbjit on 06/13/2017.
 */
public class UpdateComplaintModel extends RealmObject {

    String ID, complaintOfficerComment, complaintDefaulterExists, complaintDefaulterAcc, complaintDefaulterPenality, complaintStat;
    RealmList<MultipleImage> images;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getComplaintOfficerComment() {
        return complaintOfficerComment;
    }

    public void setComplaintOfficerComment(String complaintOfficerComment) {
        this.complaintOfficerComment = complaintOfficerComment;
    }

    public String getComplaintDefaulterExists() {
        return complaintDefaulterExists;
    }

    public void setComplaintDefaulterExists(String complaintDefaulterExists) {
        this.complaintDefaulterExists = complaintDefaulterExists;
    }

    public String getComplaintDefaulterAcc() {
        return complaintDefaulterAcc;
    }

    public void setComplaintDefaulterAcc(String complaintDefaulterAcc) {
        this.complaintDefaulterAcc = complaintDefaulterAcc;
    }

    public String getComplaintDefaulterPenality() {
        return complaintDefaulterPenality;
    }

    public void setComplaintDefaulterPenality(String complaintDefaulterPenality) {
        this.complaintDefaulterPenality = complaintDefaulterPenality;
    }

    public String getComplaintStat() {
        return complaintStat;
    }

    public void setComplaintStat(String complaintStat) {
        this.complaintStat = complaintStat;
    }

    public RealmList<MultipleImage> getImages() {
        return images;
    }

    public void setImages(RealmList<MultipleImage> images) {
        this.images = images;
    }
}