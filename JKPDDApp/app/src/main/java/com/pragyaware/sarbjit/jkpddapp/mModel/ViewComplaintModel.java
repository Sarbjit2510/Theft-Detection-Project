package com.pragyaware.sarbjit.jkpddapp.mModel;

/**
 * Created by sarbjit on 05/13/2017.
 */
public class ViewComplaintModel {

    String DistrictName, TotalReceived, TotalResolved, TotalPending, totalDetected, totalNotDetected;

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getTotalReceived() {
        return TotalReceived;
    }

    public void setTotalReceived(String totalReceived) {
        TotalReceived = totalReceived;
    }

//    public String getTotalResolved() {
//        return TotalResolved;
//    }
//
//    public void setTotalResolved(String totalResolved) {
//        TotalResolved = totalResolved;
//    }

    public String getTotalTheftDetected() {
        return totalDetected;
    }

    public void setTotalTheftDetected(String totalDetected) {
        this.totalDetected = totalDetected;
    }

    public String getTotalTheftNotDetected() {
        return totalNotDetected;
    }

    public void setTotalTheftNotDetected(String totalNotDetected) {
        this.totalNotDetected = totalNotDetected;
    }

    public String getTotalPending() {
        return TotalPending;
    }

    public void setTotalPending(String totalPending) {
        TotalPending = totalPending;
    }
}
