package com.sixd.ecomail.Utility;

/**
 * Created by Praveen on 26-Mar-18.
 */

public class Constant {

   /* public static String BASEURL="https://ec2-52-13-46-94.us-west-2.compute.amazonaws.com:8443/webservice/emxService";*/
   public static String BASEURL="http://ec2-52-13-46-94.us-west-2.compute.amazonaws.com:8001/webservice/emxService";

   public static String SUCCESS_MSG="success";

    public static class Key{

        public static String THEME_NAME="theme_name";
        public static String CONSUMER_ID="consumerId";
    }


    public static class API{

        public static String LOGIN="userAuthentication";
    }

    public static class Message{
        public static String LOGIN_ERROR="Please Enter Valide UserID";
        public static String PASSWORD_ERROR="Please Enter Valide Password";
        public static String LOGIN_SUCCESS="LogedIn Successfully.";
        public static String INTERNET_ERROR="Please Check Your Internet Connection.";
        public static String EMAIL_ERROR="Please Enter Valide Email Address.";
        public static String STATE_ERROR="Please select a state.";
        public static String FOLDERNAME_ERROR="Please Enter Folder Name.";
        public static String FOLDER_ADD_SUCCESS="Folder Added Successfully.";
        public static String FOLDER_REMOVE_SUCCESS="Folder Remove Successfully.";
        public static String FOLDER_EDIT_SUCCESS="name edited Successfully.";
        public static String FOLDER_MOVE_SUCCESS="Folder moved Successfully.";

        public static String FILTER_MSG="Please Select a filter...";
    }
}
