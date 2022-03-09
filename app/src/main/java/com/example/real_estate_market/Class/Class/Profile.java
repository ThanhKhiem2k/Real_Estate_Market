package com.example.real_estate_market.Class.Class;

public class Profile {

    private String Address;
    private String Email;
    private String Id;
    private String Name;
    private String NPhone;
    private String Sex;

    public Profile(String address, String email, String id, String name, String NPhone, String sex) {
        Address = address;
        Email = email;
        Id = id;
        Name = name;
        this.NPhone = NPhone;
        Sex = sex;
    }

    public Profile() {
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNPhone() {
        return NPhone;
    }

    public void setNPhone(String NPhone) {
        this.NPhone = NPhone;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }


    @Override
    public String toString() {
        return "Profile{" +
                "Address='" + Address + '\'' +
                ", Email='" + Email + '\'' +
                ", Id='" + Id + '\'' +
                ", Name='" + Name + '\'' +
                ", NPhone='" + NPhone + '\'' +
                ", Sex='" + Sex + '\'' +
                '}';
    }
}

