package com.comp575.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Job implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String client,descr,billTo,notes,date;
    public int orderNum;
    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel source) {
            return new Job(source);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public Job (String client,String descr,String billTo, String date){
        this.client=client;
        this.descr=descr;
        this.billTo=billTo;
        this.date=date;
    }

    public Job (Parcel in){
        client=in.readString();
        descr=in.readString();
        billTo=in.readString();
        date=in.readString();
        orderNum=in.readInt();
        notes=in.readString();
    }

    public String toString(){
        return client + " "
            + orderNum + " "
            + descr + " "
            + date;
    }

    @Override
    public boolean equals(Object o) {

        /* Check if o is an instance of Job */
        if (!(o instanceof Job)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Job j = (Job) o;

        // Compare the data members and return accordingly
        return (this.orderNum.equals(c.orderNum));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(client);
        dest.writeString(descr);
        dest.writeString(billTo);
        dest.writeString(date);
        dest.writeInt(orderNum);
        dest.writeString(notes);
    }
}
