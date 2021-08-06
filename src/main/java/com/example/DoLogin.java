package com.example;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DoLogin {
	  @NotNull
	  @Size(min=5, max=20,message = "{ 帳號長度5-20 }")
	  private String id;
	  
	  @NotNull
	  @Pattern(regexp = "^[a-zA-Z].{6,}",message = "{ 密碼開頭大小寫英文，至少6個英數字 }")
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
