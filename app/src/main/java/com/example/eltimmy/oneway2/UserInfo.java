package com.example.eltimmy.oneway2;

/**
 * Created by eltimmy on 2/21/2018.
 */

public class UserInfo {
    public String name ;
    public  String phone;
    public String password;
    private UserInfo(){
    }

    public UserInfo(String name,String phone,String password) {
        this.name = name;
        this.phone = phone;
        this.password=password;

    }
    public String  getName()
    {
        return name;
    }
    public String  getPhone()
    {
        return phone;
    }
    public String  getPassword()
    {
        return password;
    }




    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
