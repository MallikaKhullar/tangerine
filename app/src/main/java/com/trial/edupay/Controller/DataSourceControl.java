package com.trial.edupay.Controller;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class DataSourceControl {

    public static String USER_STATE = "LOGGED_IN";
    public static String AUTH_KEY = "AUTH_KEY";

    public enum UserState {
        UNSIGNED("unsigned"),
        CHILD_PENDIN("child_pending"),
        LOGGED_IN("loggedin");

        private final String itemType;

        UserState(String s) {
            itemType = s;
        }

        public boolean equals(String otherItemType) {
            return otherItemType != null && itemType.equals(otherItemType);
        }

        public String toString() {
            return this.itemType;
        }

        public static UserState getType(String featureStr){
            for(UserState value : UserState.values()) if(value.itemType.equals(featureStr)) return value;
            return UNSIGNED;
        }
    }

    public static Boolean isLoggedIn(){
        return SharedPref.getInstance().getString(USER_STATE).equals(UserState.LOGGED_IN);
    }

    public static void setUserState(UserState state) {
        SharedPref.getInstance().setString(USER_STATE, state.toString());
    }

    public static UserState getUserState() {
        return UserState.getType(SharedPref.getInstance().getString(USER_STATE, UserState.UNSIGNED.toString()));
    }

}
