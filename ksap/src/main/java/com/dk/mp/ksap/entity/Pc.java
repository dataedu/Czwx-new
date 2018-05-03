package com.dk.mp.ksap.entity;

import java.io.Serializable;

/**
 * Created by abc on 2018-4-17.
 */

public class Pc implements Serializable {

   private String  id;//批次id
   private String   name;//批次名称

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
