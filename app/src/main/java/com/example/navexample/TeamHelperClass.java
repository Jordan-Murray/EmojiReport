package com.example.navexample;

import java.util.Arrays;
import java.util.List;

public class TeamHelperClass{

    List<UserReferenceClass> _Members;
    String _TeamName;
    String _TeamUID;
    String _Password;

    public TeamHelperClass() {
    }

    public TeamHelperClass(String TeamUID, String TeamName, UserReferenceClass[] Members, String Password) {
        _TeamName = TeamName;
        _Members.addAll(Arrays.asList(Members));
        _TeamUID = TeamUID;
        _Password = Password;
    }

    public String get_Password() {
        return _Password;
    }

    public void set_Password(String Password) {
        this._Password = Password;
    }

    public String getTeamName() {
        return _TeamName;
    }

    public void setTeamName(String TeamName) {
        _TeamName = TeamName;
    }

    public List<UserReferenceClass> getMembers() {
        return _Members;
    }

    public void setMembers(List<UserReferenceClass> Members) {
        _Members = Members;
    }

    public void addMember(UserReferenceClass Member)
    {
        _Members.add(Member);
    }

    public String getTeamUID() {
        return _TeamUID;
    }

    public void setTeamUID(String TeamUID) {
        _TeamUID = TeamUID;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public TeamHelperClass(Parcel in) {
//        _TeamName = in.readString();
//        _TeamUID = in.readString();
//        _Password = in.readString();
//        in.readTypedList(_Members,UserReferenceClass.CREATOR);
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(_TeamName);
//        dest.writeString(_TeamUID);
//        dest.writeString(_Password);
//        dest.writeTypedList(_Members);
//    }
//
//    public static final Parcelable.Creator<UserHelperClass> CREATOR
//            = new Parcelable.Creator<UserHelperClass>() {
//        public UserHelperClass createFromParcel(Parcel in) {
//            return new UserHelperClass(in);
//        }
//
//        public UserHelperClass[] newArray(int size) {
//            return new UserHelperClass[size];
//        }
//    };

}
