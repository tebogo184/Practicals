package acsse.csc2a.fmb.model;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class defines the RocketFirework details
 * It extends the Firework class and implements IDisplayable interface
 * @author Tshukudu T 222041905
 *
 */
public class RocketFirework extends Firework implements IDisplayable {
	
	// regex Pattern 
	private  final static Pattern pattern=Pattern.compile("FR\\d{6}\\s[A-Za-z]+\\s[A-Za-z]+\\s[0-9]\\.[1-9]+\\s[A-Z]+\\s\\d+\\s([0-9]+\\.[0-9]+)\\sRED"); 
	private E_COLOUR blackColourPowder; 
	private int startCount; 
	private  double starRadius;
	
	/**
	 * Args constructor that initializes all member variables including the extend Firework
	 * Initializes the Fireworks member variables using the super keyword  
	 *@param id - Firework id
	 * @param name - Firework Name
	 * @param fuse_length - Firework fuse Length
	 * @param enum_colour - Firework color
	 * @param blackColourPowder - RocketFirework blackColourPowder
	 * @param startCount - RocketFirework starCount
	 * @param starRadius - RocketFirework starRadius 
	 */
	public RocketFirework(String id, String name, double fuse_length, E_COLOUR enum_colour, E_COLOUR blackColourPowder,
			int startCount, double starRadius) {
		super(id, name, fuse_length, enum_colour);
		this.blackColourPowder = blackColourPowder;
		this.startCount = startCount;
		this.starRadius = starRadius;
	}
	/**
	 * Gets the RocketFirework blackColourPowder
	 * @return blackColourPowder - Rocket blackColourPowder
	 */
	public E_COLOUR getBlackColourPowder() {
		return blackColourPowder;
	}
	/**
	 * Sets the RocketFirework blackColourPowder
	 * @param blackColourPowder - RocketFirework blackColourPowder
	 */
	public void setBlackColourPowder(E_COLOUR blackColourPowder) {
		this.blackColourPowder = blackColourPowder;
	}
	
	/**
	 * Gets the RocketFirework starCount
	 * @return starCount- RocketFirework startCount
	 */
	public int getStartCount() {
		return startCount;
	}
	
	/**
	 * Sets the RocketFirework startCount
	 * @param startCount -RocketFirework startCount
	 */
	public void setStartCount(int startCount) {
		this.startCount = startCount;
	}
	
	/**
	 * Gets the RocketFirework starRadius
	 * @return starRadius - FountainFirework Radius
	 */
	public double getStarRadius() {
		return starRadius;
	}
	
	/**
	 * Sets the RocketFirework starRadius
	 * @param starRadius- RocketFirework Radius
	 */
	public void setStarRadius(int starRadius) {
		this.starRadius = starRadius;
	}
	
	/**
	  * Used to display the textual representation of the FountianFirework class
	  * @return formattedString  the textual representation of the class
	  */
	@Override
	public String toString() {
		return String.format("Id:%s\t Name:%s\t fuseLength:%s\t colour:%s\t StarCount:%s\t starRadius:%s\t blackColourPowder:%s\t",this.getId(),this.getName(),this.getFuse_length(),this.getEnum_colour(),
				this.getStartCount(),this.starRadius,this.blackColourPowder);
	}
	
	
	/**
	 * This method checks if a line is valid or not
	 * @param line the line being validated
	 * @return True/false
	 */
	public static boolean validate(String line) {
		Matcher matcher =pattern.matcher(line);
		if(!matcher.matches()) {
			System.err.format(" Rocket Firework line is invalid: %s\n",line);
			return false;
			
		}
		return true;
	}
	
	/**
	 * Static method used to create a new RocketFirework from a line
	 * @param line- the line being processed
	 * @return RocketFirework
	 */
	public static RocketFirework proccessLine(String line) {
		StringTokenizer token=new StringTokenizer(line,"\t");
		
		String id =token.nextToken();
		String name=token.nextToken();
		double length=Double.parseDouble(token.nextToken());
		E_COLOUR enum_colour= E_COLOUR.valueOf(token.nextToken());
		int starCount=Integer.parseInt(token.nextToken());
		double radius =Double.parseDouble(token.nextToken());
		E_COLOUR blackPowderColour= E_COLOUR.valueOf(token.nextToken());
		
		return new RocketFirework(id,name,length,enum_colour,blackPowderColour,starCount,radius);
	}
	
	/**
	 * Used to display the the toString function
	 */
	@Override
	public void display() {
		System.out.println(toString());
		
	}
	
	
	

}
