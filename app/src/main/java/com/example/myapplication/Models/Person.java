package com.example.myapplication.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Person implements Parcelable {

    private int id;
    private String firstName;
    private String lastName;
    private String photoPath;
    private String address;
    private Set<String> statuses;

    public Person(int id, String firstName, String lastName, String photoPath, String address, Set<String> statuses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoPath = photoPath;
        this.address = address;
        this.statuses = statuses;
    }

    protected Person(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        photoPath = in.readString();
        address = in.readString();

        // Read statuses as a List and convert it to a Set
        List<String> statusList = in.createStringArrayList();
        statuses = new HashSet<>(statusList != null ? statusList : new ArrayList<>());

    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }

    // Accessor for photoPath
    public String getPhotoPath() {
        return photoPath;
    }

    public String getFullInfo() {
        return "Name: " + firstName + " " + lastName + "\n" +
                "Address: " + address + "\n";
    }

    public String getName() {
        return "Name: " + firstName + " " + lastName;
    }

    public Set<String> getStatuses() {
        return statuses;
    }

    public int getId() {
        return id;
    }

    public void addStatus(String status) {
        statuses.add(status);
    }

    public void removeStatus(String status) {
        statuses.remove(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(photoPath);
        dest.writeString(address);

        // Convert statuses to a List and write it to the Parcel
        dest.writeStringList(new ArrayList<>(statuses));
    }
}
