package com.trial.edupay.Model;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public class Message extends BaseEntity {

    public String _id;
    public transient Boolean isSystemGenerated;
    public transient Boolean sendPush;
    public transient Boolean sendSms;
    public transient String senderId;
    public transient Boolean isSendToAll;
    public String content;
    public Boolean deleted;
    public int contentCount;
    public Student studentId;
    public String messageItemId;
    public Boolean isRead;
    public Organisation organisationId;
    //public String category;
}
