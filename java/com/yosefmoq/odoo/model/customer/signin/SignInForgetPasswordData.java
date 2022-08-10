package com.yosefmoq.odoo.model.customer.signin;

/**
 * @author shubham.agarwal
 */

public class SignInForgetPasswordData {

    @SuppressWarnings("unused")
    private static final String TAG = "SignInForgetPasswordDat";

    private String mUsername;
    private String phone;




    public SignInForgetPasswordData(String phone) {
        this.phone = phone;
    }


    public String getPhone(){
        if(phone == null){
            return  "";
        }
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }


    public String getUsername() {
        if (mUsername == null) {
            return "";
        }
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
}
