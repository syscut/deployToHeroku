package com.syscut.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DoLogin {
	  @NotNull
	  @Size(min=5, max=20)
	  private String id;
	  
	  @NotNull
	  @Pattern(regexp = "^[a-zA-Z].{6,}")
	  private String pd;
      
	  public String getId() {
	    return id;
	  }

	  public void setId(String id) {
	    this.id = id;
	  }
	  
	  public String getPd() {
		    return pd;
		  }

	  public void setPd(String password) {
		    this.pd = password;
		  }
	  
}
