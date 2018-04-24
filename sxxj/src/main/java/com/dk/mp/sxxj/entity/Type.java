package com.dk.mp.sxxj.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2018-4-17.
 */

public class Type  implements Serializable {
    private String id;
    private String name;
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

	public Type(String id, String name) {
//      super();
		this.id = id;
		this.name = name;
	}
}
