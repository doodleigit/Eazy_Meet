package net.doodlei.android.eazymeet.shareLocation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Contact implements Parcelable {

    private String name, photo;
    private boolean isCheck = false;
    private int selectNumberPosition = 0;
    private ArrayList<PhoneNumber> phoneNumbers;

    public Contact(String name, String photo, ArrayList<PhoneNumber> phoneNumbers) {
        this.name = name;
        this.photo = photo;
        this.phoneNumbers = phoneNumbers;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        photo = in.readString();
        isCheck = in.readByte() != 0;
        selectNumberPosition = in.readInt();
        phoneNumbers = in.createTypedArrayList(PhoneNumber.CREATOR);
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public int getSelectNumberPosition() {
        return selectNumberPosition;
    }

    public void setSelectNumberPosition(int selectNumberPosition) {
        this.selectNumberPosition = selectNumberPosition;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(photo);
        parcel.writeByte((byte) (isCheck ? 1 : 0));
        parcel.writeInt(selectNumberPosition);
        parcel.writeTypedList(phoneNumbers);
    }
}
