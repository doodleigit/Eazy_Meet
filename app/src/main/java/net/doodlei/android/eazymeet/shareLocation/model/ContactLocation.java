package net.doodlei.android.eazymeet.shareLocation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;

public class ContactLocation implements Parcelable {

    private double latitude, longitude;
    private Marker marker;
    private ConfirmContact confirmContact;

    public ContactLocation(double latitude, double longitude, Marker marker, ConfirmContact confirmContact) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.marker = marker;
        this.confirmContact = confirmContact;
    }

    protected ContactLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<ContactLocation> CREATOR = new Creator<ContactLocation>() {
        @Override
        public ContactLocation createFromParcel(Parcel in) {
            return new ContactLocation(in);
        }

        @Override
        public ContactLocation[] newArray(int size) {
            return new ContactLocation[size];
        }
    };

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

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public ConfirmContact getConfirmContact() {
        return confirmContact;
    }

    public void setConfirmContact(ConfirmContact confirmContact) {
        this.confirmContact = confirmContact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
