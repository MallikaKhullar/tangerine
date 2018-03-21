package com.trial.edupay.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.trial.edupay.Model.Notifications;
import com.trial.edupay.Modules.DrawerChildren.AboutActivity;
import com.trial.edupay.Modules.Payment.FeeDetailActivity;
import com.trial.edupay.Modules.Profile.AddStudentActivity;
import com.trial.edupay.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by mallikapriyakhullar on 06/01/18.
 */
public class FirebaseService extends FirebaseMessagingService {

    private Uri defaultSoundUri;
    private NotificationManager notificationManager;

    public static String TYPE = "type";
    public static String MESSAGE_ID = "messageId";
    public static String STUDENT_ID = "studentId";
    public static String STUDENT_NAME = "studentName";

    public enum MessageType{
        MESSAGE("message"),
        FEE("fee"),
        UNKNOWN("unknown");

        private final String itemType;

        MessageType(String s) {itemType = s;}

        public boolean equals(String otherItemType) { return otherItemType != null && itemType.equals(otherItemType); }

        public String toString() { return this.itemType; }

        public static MessageType getType(String featureStr){
            for(MessageType value : MessageType.values()) if(value.itemType.equals(featureStr)) return value;
            return UNKNOWN;
        }
    }

    class MessageData {
        String type;
        String studentId;
        String studentName;
        String messageId;
        String title;
        String message;

        public MessageData(String type, String studentId, String studentName, String messageId, String title, String message) {
            this.type = type;
            this.studentId = studentId;
            this.studentName = studentName;
            this.messageId = messageId;
            this.title = title;
            this.message = message;
        }
    }

    @Override public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.w("Firebase", "FCM: received notification");
        defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        parseNotification(remoteMessage);
    }

    void parseNotification(RemoteMessage remoteMessage) {
        if(remoteMessage == null || remoteMessage.getData() == null) return;

        MessageData messageData = new MessageData(
                remoteMessage.getData().get(TYPE),
                remoteMessage.getData().get(STUDENT_ID),
                remoteMessage.getData().get(STUDENT_NAME),
                remoteMessage.getData().get(MESSAGE_ID),
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody()
        );

        switch(MessageType.getType(messageData.type)) {
            case MESSAGE: sendMessage(messageData); break;
            case FEE: sendFee(messageData); break;
            case UNKNOWN: return;
        }
    }

    void sendMessage(MessageData data) {
        //TODO - testing, remove when implemented
        PendingIntent pendingIntent;
        int id =  new Random().nextInt(9999);
        Intent feeIntent = new Intent(this, AddStudentActivity.class);
        pendingIntent = PendingIntent.getActivity(this, id, feeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        buildNotification(data, pendingIntent, id);
    }

    void sendFee(MessageData data) {
        PendingIntent pendingIntent;
        int id =  new Random().nextInt(9999);
        Intent feeIntent = new Intent(this, FeeDetailActivity.class);
        feeIntent.putExtra("studentId", data.studentId);
        pendingIntent = PendingIntent.getActivity(this, id, feeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        buildNotification(data, pendingIntent, id);
    }

    void buildNotification(MessageData data, PendingIntent intent, int id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(data.title)
                .setContentText(data.message)
                .setSound(defaultSoundUri)
                .setTicker(data.message)
                .setAutoCancel(true)
                .setContentIntent(intent);

        notificationManager.notify(id, builder.build());
    }


    private void sendMessageNotification(MessageData data) {

        Notifications localNotifications = Utils.getNotifications();
        if(localNotifications == null) localNotifications = new Notifications();
        if(localNotifications.notificationHashMap == null) localNotifications.notificationHashMap = new HashMap<>();

        PendingIntent pendingIntent = null;
        int id = Utils.createNotificationId(data.studentId, data.type);

        //todo - uncomment this
//        Intent messageIntent = new Intent(this, MessageDisplayActivity.class);
        Intent messageIntent = new Intent(this, AboutActivity.class);
        messageIntent.putExtra("studentId", data.studentId);
        messageIntent.putExtra("messageId", data.messageId);
        pendingIntent = PendingIntent.getActivity(this, id, messageIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if(pendingIntent == null) return;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(data.title)
                .setContentText(data.message)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setGroup(data.studentId);

        notificationManager.notify(id, notificationBuilder.build());

//        if(localNotifications.notificationHashMap.get(id) == null) {
//            //no old messages for that student ID
//
//            Notification notification = new Notification();
//            notification.objectType = data.type;
//            notification.message = data.message;
//
//            messagesInEachNotification.put(id, notification);
//            groupNotifications.notificationHashMap = messagesInEachNotification;
//            AppUtil.saveNotifications(groupNotifications);
//        }
//
//        if(messagesInEachNotification != null && messagesInEachNotification.get(id) != null && messagesInEachNotification.get(id).count > 0){
//
//            // save new message preference and increase the message count
//            messagesInEachNotification.get(id).messages.add(message);
//            messagesInEachNotification.get(id).count++;
//            groupNotifications.notificationHashMap = messagesInEachNotification;
//
//            AppUtil.saveNotifications(groupNotifications);
//
//            // add new message to the notification
//            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//            for(String string: messagesInEachNotification.get(id).messages)
//                inboxStyle.addLine(string);
//
//            inboxStyle.setBigContentTitle(messagesInEachNotification.get(id).count + " new " + objectType + " for " + studentName);
//
//            //add pending intent to group notification
//            PendingIntent groupPendingIntent = null;
//
//            if(objectType.equals("message")){
//                Intent messageIntent = new Intent(this, InboxActivity.class);
//                messageIntent.putExtra("studentId", studentId);
//                groupPendingIntent = PendingIntent.getActivity(this, id, messageIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            }
//
//            NotificationCompat.Builder groupNotificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.logo)
//                    .setContentTitle(messagesInEachNotification.get(id).count + " new " + objectType + " for " + studentName)
//                    .setStyle(inboxStyle)
//                    .setGroup(studentId)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(groupPendingIntent);
//
//            notificationManager.notify(id, groupNotificationBuilder.build());
//        }else{
//            Message notification = new Message();
//            notification.count++;
//            notification.objectType = objectType;
//            notification.messages.add(message);
//
//            messagesInEachNotification.put(id, notification);
//            groupNotifications.notificationHashMap = messagesInEachNotification;
//            AppUtil.saveNotifications(groupNotifications);
//        }
    }

}