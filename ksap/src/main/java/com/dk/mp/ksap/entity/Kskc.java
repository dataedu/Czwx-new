package com.dk.mp.ksap.entity;

import java.io.Serializable;

/**
 * Created by abc on 2018-4-17.
 */

public class Kskc implements Serializable {

   private String  kssj;//考试时间"
   private String   kcmc;//课程名称","
   private String   ksdd;//考试地点",
   private String   zkls;//主考老师","
   private String   jkls;//监考老师"

   public String getKssj() {
      return kssj;
   }

   public void setKssj(String kssj) {
      this.kssj = kssj;
   }

   public String getKcmc() {
      return kcmc;
   }

   public void setKcmc(String kcmc) {
      this.kcmc = kcmc;
   }

   public String getKsdd() {
      return ksdd;
   }

   public void setKsdd(String ksdd) {
      this.ksdd = ksdd;
   }

   public String getZkls() {
      return zkls;
   }

   public void setZkls(String zkls) {
      this.zkls = zkls;
   }

   public String getJkls() {
      return jkls;
   }

   public void setJkls(String jkls) {
      this.jkls = jkls;
   }



}
