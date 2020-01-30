package net.doodlei.android.eazymeet.shareLocation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PhoneNumber implements Parcelable {

    private String number, countryCode;

    public PhoneNumber(String countryCode, String number) {
        this.number = number;
        this.countryCode = countryCode;
    }

    protected PhoneNumber(Parcel in) {
        number = in.readString();
        countryCode = in.readString();
    }

    public static final Creator<PhoneNumber> CREATOR = new Creator<PhoneNumber>() {
        @Override
        public PhoneNumber createFromParcel(Parcel in) {
            return new PhoneNumber(in);
        }

        @Override
        public PhoneNumber[] newArray(int size) {
            return new PhoneNumber[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(number);
        parcel.writeString(countryCode);
    }
}
