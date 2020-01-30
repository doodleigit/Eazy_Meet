package net.doodlei.android.eazymeet.shareLocation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmContact implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("country_code_id")
    @Expose
    private String countryCodeId;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("media_id")
    @Expose
    private String mediaId;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("country_phone_code")
    @Expose
    private String countryPhoneCode;

    private int acceptStatus = -1;

    protected ConfirmContact(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        countryCodeId = in.readString();
        mobile = in.readString();
        mediaId = in.readString();
        isVerified = in.readString();
        userImage = in.readString();
        countryName = in.readString();
        countryId = in.readString();
        countryPhoneCode = in.readString();
    }

    public static final Creator<ConfirmContact> CREATOR = new Creator<ConfirmContact>() {
        @Override
        public ConfirmContact createFromParcel(Parcel in) {
            return new ConfirmContact(in);
        }

        @Override
        public ConfirmContact[] newArray(int size) {
            return new ConfirmContact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(countryCodeId);
        parcel.writeString(mobile);
        parcel.writeString(mediaId);
        parcel.writeString(isVerified);
        parcel.writeString(userImage);
        parcel.writeString(countryName);
        parcel.writeString(countryId);
        parcel.writeString(countryPhoneCode);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountryCodeId() {
        return countryCodeId;
    }

    public void setCountryCodeId(String countryCodeId) {
        this.countryCodeId = countryCodeId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public int getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(int acceptStatus) {
        this.acceptStatus = acceptStatus;
    }
}
