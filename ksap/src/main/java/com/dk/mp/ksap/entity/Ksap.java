package com.dk.mp.ksap.entity;

import java.util.List;

/**
 * Created by abc on 2018-4-17.
 */

public class Ksap {

    private String date;
    List<Kskc> kslist;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Kskc> getKslist() {
        return kslist;
    }

    public void setKslist(List<Kskc> kslist) {
        this.kslist = kslist;
    }


}
