package acsse.csc2a.fmb.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is define the FountainFirework details
 * It inherits from the Firework and implements the IDisplayable
 * @author Tshukudu T 222041905
 *
 */

public class FountainFirework extends Firework implements IDisplayable{
	
	//Fountain regex Pattern 
	private  final static Pattern pattern= Pattern.compile("FF\\d{6}\\s[A-Za-z?]+\\s[A-Za-z?]+\\s([0-9]\\.[1-9]+)\\sYELLOW+\\s\\d+\\s(\\[YELLOW\\|CYAN\\])");
	private double fountainDuration;
	private String transitionColours;
	
	/**
	 * Args constructor that initializes all member variables including the extend Firework
	 * Initializes the Fireworks member variables using the super keyword 
	 * @param id - Firework id
	 * @param name - Firework Name
	 * @param fuse_length - Firework fuse Length
	 * @param enum_colour - Firework color
	 * @param totalDisplayTime -Fountain duration
	 * @param transitionColour -Fountain transition colors
	 */
	public FountainFirework(String id, String name, double fuse_length, E_COLOUR enum_colour, double totalDisplayTime,
			String transitionColour) {
		super(id, name, fuse_length, enum_colour);
		this.fountainDuration = totalDisplayTime; 
		this.transitionColours = transitionColour;
	}

	
	/**
	 * Gets the transitionColours of the FountianFirework
	 * @return transitionColours FountianFirework transition colours
	 */
	public String getTransitionColour() {
		return transitionColours;
	}

	/**
	 * Sets the transitionColours of the FountainFirework
	 * @param transitionColour- FountainFirework transitionColour
	 */
	public void setTransitionColour(String transitionColour) {
		this.transitionColours = transitionColour;
	}
	
	

	/**
	 * Gets the fountainDuration 
	 * @return fountainDuration - fountain Firework duration
	 */
	public double getFountainDuration() {
		return fountainDuration;
	}

	/**
	 * Sets the duration of the FountianFirework 
	 * @param totalDisplayTime- duration of the FountianFirework
	 */
	public void setFountainDuration(double fountainDuration) {
		this.fountainDuration = fountainDuration;
	}

	/**
	 * Gets the transitionColours of the FountianFirework
	 * @return transitionColours- fountain transitionColours
	 */
	public String getTransitionColours() {
		return transitionColours;
	}

	/**
	 * Sets the transitionColours of the FountainFirework
	 * @param transitionColours Fountain firework transition colours
	 */
	public void setTransitionColours(String transitionColours) {
		this.transitionColours = transitionColours;
	}
	
	
	/**
	 * This method returns a textual String representation of the class
	 */
	@Override
	public String toString() {
		return String.format("Id:%s\t name:%s\t fuseLength:%s\t color:%s\t duration:%s\t transitionColour:%s\n",this.getId(),this.getName(),this.getFuse_length(),
				this.getEnum_colour(),this.fountainDuration,this.transitionColours);
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
			System.err.format("Fountain Firework line is invalid: %s\n",line);
			return false;
			
		}
		return true;
	}
	
	/**
	 * Creates a new FountianFirework from a line
	 * @param line line being processed
	 * @return FountainFirework
	 */
 public	static FountainFirework proccessLine(String line) {
		String [] strValues= line.split("\t");
		String id=strValues[0];
		String fullname=strValues[1];
		double length= Double.parseDouble(strValues[2]);
		E_COLOUR colour= E_COLOUR.valueOf(strValues[3]);
		double duration = Double.parseDouble(strValues[4]);
		String transitionColor=strValues[5];
		
		
		
		return new FountainFirework(id,fullname,length,colour,duration,transitionColor);
		
		
		
	}

 /**
  * Used to display the textual representation of the FountianFirework class
  */
@Override
public void display() {
	System.out.println(toString());
	
}
	
	
	
	


}
