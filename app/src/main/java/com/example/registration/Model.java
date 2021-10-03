package com.example.registration;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class Model {

        private String imageUrl;
        private String Crop;
        private String Disease;

        @SerializedName(value = "Disease-Summary")
        private String Disease_Summary;

        private  ArrayList Cause;
        private  ArrayList Cure;

        public Model(String imageUrl, String crop, String Disease, String Disease_Summary, ArrayList Cause, ArrayList Cure) {
                this.imageUrl = imageUrl;
                this.Crop = crop;
                this.Disease = Disease;
                this.Disease_Summary = Disease_Summary;
                this.Cause = Cause;
                this.Cure = Cure;

        }

        public String getImageUrl() {
                return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
        }

        public String getCrop() {
                return Crop;
        }

        public void setCrop(String crop) {
                this.Crop = crop;
        }

        public void setDisease(String disease) {
                Disease = disease;
        }

        public String getDisease() {
                return Disease;
        }

        public void setCure(ArrayList cure) {
                Cure = cure;
        }

        public ArrayList getCure() {
                return Cure;
        }

        public void setCause(ArrayList cause) {
                Cause = cause;
        }

        public ArrayList getCause() {
                return Cause;
        }

}


