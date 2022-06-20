package com.example.navexample;

public class UserReferenceClass {

    public String getUserRef() {
        return UserRef;
    }

    public void setUserRef(String userRef) {
        UserRef = userRef;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UserReferenceClass() {
    }

    public UserReferenceClass(String userRef, String displayName) {
        this.UserRef = userRef;
        this.displayName = displayName;
    }

    String UserRef;
    String displayName;

//    public UserReferenceClass(Parcel in) {
//        displayName = in.readString();
//        UserRef = in.readString();
//    }
//
//    public static final Creator<UserReferenceClass> CREATOR = new Creator<UserReferenceClass>() {
//        public UserReferenceClass createFromParcel(Parcel in) {
//            return new UserReferenceClass(in);
//        }
//
//        public UserReferenceClass[] newArray(int size) {
//            return new UserReferenceClass[size];
//        }
//
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(displayName);
//        dest.writeString(UserRef);
//    }
}
