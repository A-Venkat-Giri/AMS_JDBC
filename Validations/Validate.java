package com.dev.validations;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Validate {
  /**This function is used for Id validation**/
	public Boolean idValidation(String id) {
		Pattern pat = Pattern.compile("\\d+"); 
		Matcher mat = pat.matcher(id);
		if(mat.matches() && (Integer.parseInt(id)>=0)) {

			return true;
		}

		return false;
		}
	  /**This function is used for Name validation**/

	public Boolean NameValidation(String name)  {
		Pattern pat = Pattern.compile("[A-Za-z]{1,25}"); 
		Matcher mat = pat.matcher(name);
		if(mat.matches()) {
			return true;
		}
		return false;
		}
	  /**This function is used for Password validation**/

	public Boolean PasswordValidation(String password) {
		Pattern pat = Pattern.compile("\\w+\\W+\\w+"); 
		Matcher mat = pat.matcher(password);
		if(mat.matches()) {
			return true;
		}
		return false;
		}
	  /**This function is used for Date validation**/

	public Boolean dateValidation(String date)  {
		Pattern pat = Pattern.compile("^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$"); 
		Matcher mat = pat.matcher(date);
		if(mat.matches()) { 
			return true;
		}
		else
		{
			return false;
		}
	}
}



