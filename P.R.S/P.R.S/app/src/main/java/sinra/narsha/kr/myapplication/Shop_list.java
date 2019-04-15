package sinra.narsha.kr.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Shop_list {
    String MARKET_NAME;
    String CLASS;
    String PETS;
    String SERVICE;
    String MARKET_NO;

    public Shop_list(String MARKET_NAME, String CLASS, String PETS, String SERVICE, String MARKET_NO) {
        this.MARKET_NAME = MARKET_NAME;
        this.CLASS = CLASS;
        this.PETS = PETS;
        this.SERVICE = SERVICE;
        this.MARKET_NO = MARKET_NO;
    }

    public String getMARKET_NO() {
        return MARKET_NO;
    }

    public void setMARKET_NO(String MARKET_NO) {
        this.MARKET_NO = MARKET_NO;
    }

    public String getMARKET_NAME() {
        return MARKET_NAME;
    }

    public void setMARKET_NAME(String MARKET_NAME) {
        this.MARKET_NAME = MARKET_NAME;
    }

    public String getCLASS() {
        return CLASS;
    }

    public void setCLASS(String CLASS) {
        this.CLASS = CLASS;
    }

    public String getPETS() {
        return PETS;
    }

    public void setPETS(String PETS) {
        this.PETS = PETS;
    }

    public String getSERVICE() {
        return SERVICE;
    }

    public void setSERVICE(String SERVICE) {
        this.SERVICE = SERVICE;
    }
}
