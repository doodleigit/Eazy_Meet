package net.doodlei.android.eazymeet.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class InviteLocation implements Parcelable {

    private String locationName;
    private double latitude, longitude;

    public InviteLocation(String locationName, double latitude, double longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected InviteLocation(Parcel in) {
        locationName = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<InviteLocation> CREATOR = new Creator<InviteLocation>() {
        @Override
        public InviteLocation createFromParcel(Parcel in) {
            return new InviteLocation(in);
        }

        @Override
        public InviteLocation[] newArray(int size) {
            return new InviteLocation[size];
        }
    };

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(locationName);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
