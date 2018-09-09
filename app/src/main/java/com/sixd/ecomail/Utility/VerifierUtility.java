package com.sixd.ecomail.Utility;

/**
 * Created by ram on 07-04-2018.
 */

public class VerifierUtility {


    public boolean isVlidEmail(String emailAddress){
        String email = emailAddress.toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
