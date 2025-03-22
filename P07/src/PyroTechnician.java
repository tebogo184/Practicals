package acsse.csc2a.fmb.model;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class stores the Pyro Technician details
 * 
 * @author Tshukudu T 222041905
 *
 */
public class PyroTechnician implements IDisplayable{

	private String fullName;
	private String phoneNumber;
	
	private  final static Pattern pattern=Pattern.compile("[A-Za-z?]+-[A-Za-z?]+\\s([1-9]{3}-\\d{3}-\\d{3}[1-9])");

	/**
	 * Argument constructor for initializing the member variables
	 * 
	 * @param fullName -pyro Technician fullName
	 * @param phoneNumber Pyro Technician phone number
	 * 
	 */

	public PyroTechnician(String fullName, String phoneNumber) {
		
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
	} 

	
 /**
  * Gets the Pyro Technician phone Number
  * @return phoneNumber
  */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * Gets the pyro Technician fullname
	 * @return fullName
	 */
	public String getFullName() {
		return fullName;
	}


/**
 * Sets the Pyro Technician fullName
 * @param fullName - PyroTech fullName
 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}



	/**
	 * Sets the pyro Technician phoneNumber
	 * @param phoneNumber -Pyro Technician phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	/**
	 * Method used to return the textual Representation of the class
	 * @return formatted String
	 */
	@Override
	public String toString() {
		return String.format("FullName:%s\t PhoneNumber:%s\n",this.getFullName(), this.getPhoneNumber());
	}
	
	/**
	 *Static method used to validate whether a line is valid or not 
	 * if the line is invalid, an error will be displayed
	 * @param line -The line being validated
	 * @return True/false
	 */
	public static boolean validate(String line) {
		
		Matcher matcher =pattern.matcher(line);
		if(!matcher.matches()) {
			System.err.format("PyroTech line is invalid: %s\n",line);
			
			return false;
			
		}
		return true;
		
	}
	
	/**
	 * Static method used to create a new Pyro Tech from a line
	 * @param line- the line being processed
	 * @return PyroTechnician
	 */
	public static PyroTechnician proccessLine(String line) {
		StringTokenizer token=new StringTokenizer(line,"\t");
		String fullname=token.nextToken();
		String phoneNumber=token.nextToken();
		
		
		return new PyroTechnician(fullname,phoneNumber);
	}



	/**
	 * This method is used to display the toString
	 */
	
	@Override
	public void display() {
		System.out.println(toString());
		
	}

}
