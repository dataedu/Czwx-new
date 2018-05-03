package com.dk.mp.ksap.entity;

import java.io.Serializable;

/**
 * Created by abc on 2018-4-17.
 */

public class KskcItem implements Serializable {

   private String  title;//考试时间"
   private String   value;//课程名称","

   public KskcItem(String  title,String   value){

      this.title = title;
      this.value = value;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
