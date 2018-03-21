package com.trial.edupay.Model;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by mallikapriyakhullar on 24/12/17.
 */

public class User extends BaseEntity {
        public String _id;
        public String name;
        public String email;
        public String occupation;
        public String address;
        public String mobile;
        public transient DateTime dob;
        public String image;
        public ArrayList<Student> students = new ArrayList<>();
        public ArrayList<String> userType = new ArrayList<>();
}

