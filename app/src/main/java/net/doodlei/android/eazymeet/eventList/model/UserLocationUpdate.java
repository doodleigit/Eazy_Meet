package net.doodlei.android.eazymeet.eventList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLocationUpdate {

    @SerializedName("from_user_id")
    @Expose
    private String fromUserId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("invitation_identity_id")
    @Expose
    private String invitationId;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("updated_time")
    @Expose
    private long updateTime;

    public UserLocationUpdate(String fromUserId, String userId, String invitationId, double latitude, double longitude, long updateTime) {
        this.fromUserId = fromUserId;
        this.userId = userId;
        this.invitationId = invitationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.updateTime = updateTime;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
