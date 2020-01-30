package net.doodlei.android.eazymeet.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("country_code_id")
    @Expose
    private Integer countryCodeId;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("media_id")
    @Expose
    private Integer mediaId;
    @SerializedName("is_verified")
    @Expose
    private Integer isVerified;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("country_phone_code")
    @Expose
    private Integer countryPhoneCode;
    public final static Parcelable.Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
                "unchecked"
        })
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7647892812106010811L;

    protected User(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.countryCodeId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.mobile = ((String) in.readValue((String.class.getClassLoader())));
        this.mediaId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.isVerified = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.userImage = ((String) in.readValue((String.class.getClassLoader())));
        this.countryName = ((String) in.readValue((String.class.getClassLoader())));
        this.countryId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.countryPhoneCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getCountryCodeId() {
        return countryCodeId;
    }

    public void setCountryCodeId(Integer countryCodeId) {
        this.countryCodeId = countryCodeId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Integer isVerified) {
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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(Integer countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(countryCodeId);
        dest.writeValue(mobile);
        dest.writeValue(mediaId);
        dest.writeValue(isVerified);
        dest.writeValue(userImage);
        dest.writeValue(countryName);
        dest.writeValue(countryId);
        dest.writeValue(countryPhoneCode);
    }

    public int describeContents() {
        return 0;
    }

}