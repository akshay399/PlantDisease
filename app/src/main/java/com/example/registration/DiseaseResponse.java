package com.example.registration;

import java.util.ArrayList;

public class DiseaseResponse {


    private String crop;
    private String status;
    private String disease;
    private String disease_summary;
    private ArrayList cause;
    private ArrayList cure;

    public DiseaseResponse(String crop,String status,String disease,String disease_summary,ArrayList cause,ArrayList cure){
        this.crop = crop;
        this.status = status;
        this.disease = disease;
        this.disease_summary = disease_summary;
        this.cause = cause;
        this.cure = cure;

    }

    public ArrayList getCause() {
        return cause;
    }

    public String getCrop() {
        return crop;
    }

    public ArrayList getCure() {
        return cure;
    }

    public String getStatus() {
        return status;
    }

    public String getDisease() {
        return disease;
    }

    public String getDisease_summary() {
        return disease_summary;
    }

    public void setCause(ArrayList cause) {
        this.cause = cause;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public void setCure(ArrayList cure) {
        this.cure = cure;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setDisease_summary(String disease_summary) {
        this.disease_summary = disease_summary;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
