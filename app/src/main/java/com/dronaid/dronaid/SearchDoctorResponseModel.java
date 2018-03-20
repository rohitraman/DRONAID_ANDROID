package com.dronaid.dronaid;

/**
 * Created by bennyhawk on 12/21/17.
 */

public class SearchDoctorResponseModel {

        private Username[] username;

        private String success;

        public Username[] getUsername ()
        {
            return username;
        }

        public void setUsername (Username[] username)
        {
            this.username = username;
        }

        public String getSuccess ()
        {
            return success;
        }

        public void setSuccess (String success)
        {
            this.success = success;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [username = "+username+", success = "+success+"]";
        }

}
