package com.trial.edupay.Utils;

import android.text.Html;

import com.trial.edupay.Controller.SharedPref;
import com.trial.edupay.Model.BaseEntity;
import com.trial.edupay.Model.Notifications;
import com.trial.edupay.Model.User;

import org.joda.time.DateTime;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import static com.trial.edupay.Utils.FirebaseService.MessageType.MESSAGE;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class Utils {

    public static String getDisplayDateTime(DateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.toString("dd MMM, hh:mm aa");
    }

    public static String getDisplayDate(DateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.toString("dd MMM, YYYY");
    }

    public static DateTime convertToCurrentTimeZone(DateTime dateTime) {
        if (dateTime == null)
            return null;
        else
            return dateTime.plusHours(5).plusMinutes(30);
    }

    public static Boolean dataNotFound(ArrayList list){
        return list == null || list.isEmpty();
    }

    public static void saveUser(User user) {
        SharedPref.getInstance().setString(SharedPref.PFUser, user.toJson());
    }

    public static User getUser() {
        String userJson = SharedPref.getInstance().getString(SharedPref.PFUser);
        if (userJson != null)
            return BaseEntity.fromJson(userJson, User.class);
        else
            return null;
    }

    public static String getMobile() {
        return SharedPref.getInstance().getString(SharedPref.PFMobile);
    }

    public static String getEmail() {
        return SharedPref.getInstance().getString(SharedPref.PFEmail);
    }

    public static int createNotificationId(String studentId, String objectType) {

        if (studentId == null || objectType == null) return 0;
        else if (!ObjectId.isValid(studentId)) return 0;

        ObjectId objectId = new ObjectId(studentId);
        int counter = objectId.getCounter();

        if (FirebaseService.MessageType.valueOf(objectType).equals(MESSAGE)) return counter + 1;
        else return counter;
    }

    public static Notifications getNotifications() {
        String jsonNotifications = SharedPref.getInstance().getString(SharedPref.PFGroupNotifications);

        if (jsonNotifications != null)
            return BaseEntity.fromJson(jsonNotifications, Notifications.class);
        else
            return null;
    }

    public static void saveNotifications(Notifications notifications) {
        SharedPref.getInstance().setString(SharedPref.PFGroupNotifications, notifications.toJson());
    }

    public static String trimText(String text) {
        if (text.contains(" minutes ago"))
            return text.replace(" minutes ago", "m");
        else if (text.contains(" hour ago"))
            return text.replace(" hour ago", "h");
        else if (text.contains(" hours ago"))
            return text.replace(" hours ago", "h");
        else if (text.contains(" day ago"))
            return text.replace(" day ago", "d");
        else if (text.contains(" days ago"))
            return text.replace(" days ago", "d");
        else if (text.contains(" week ago"))
            return text.replace(" week ago", "w");
        else if (text.contains(" weeks ago"))
            return text.replace(" weeks ago", "w");
        else if (text.contains(" month ago"))
            return text.replace(" month ago", "M");
        else if (text.contains(" months ago"))
            return text.replace(" months ago", "M");
        else
            return text;
    }

    public static String convertHtmlToString(String text) {
        if (text != null) {
            String s = Html.fromHtml(text.replace("<li>", "<br /> . ")).toString();
            return s.trim();
        } else
            return "";
    }

    public static String getInitials(String text) {
        String[] strings = text.split(" ");
        String result = "";
        for (String string : strings)
            if (!string.equals(""))
                result = result + string.subSequence(0, 1);

        return result;
    }
}


