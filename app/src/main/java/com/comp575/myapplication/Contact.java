package com.comp575.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name,email,mobile;
    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public Contact (String name,String email,String mobile){
        this.name=name;
        this.email=email;
        this.mobile=mobile;
    }

    public Contact (Parcel in){
        name=in.readString();
        email=in.readString();
        mobile=in.readString();
    }

    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o) {

        /* Check if o is an instance of Contact */
        if (!(o instanceof Contact)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Contact c = (Contact) o;

        // Compare the data members and return accordingly
        return (this.name.equals(c.name));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(mobile);
    }
}
