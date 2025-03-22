package acsse.csc2a.fmb.model;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to display all the fireworks information
 * 
 * @author Tshukudu T 222041905
 *
 */
public class FireworkDisplay implements IDisplayable{

	private String displayId;
	private String FireworkName;
	private String Theme;
	private PyroTechnician LeadTech = null;
	private Firework[] fireworks = null;
	private final int initialSize = 1;// initial Length of the fireworks array
	 
	private static final Pattern pattern = Pattern.compile(
			"FD\\d{4}\\s\\[[A-Za-z?]+\\s[A-Za-z?]+\\s[A-Za-z?]+\\]\\s(\"[A-Za-z?]+\\s[A-Za-z?]+\\s[A-Za-z?]+\")");

	/**
	 * A constructor with arguments.Used to initailze variable members
	 * 
	 * @param displaId     the display id
	 * @param fireworkName the firework display name
	 * @param theme        the theme of the firework display
	 * @param LeadTech     the Technician responsible for the fireworks
	 */
	public FireworkDisplay(String displaId, String fireworkName, String theme) {

		this.displayId = displaId;
		this.FireworkName = fireworkName;
		this.Theme = theme;
		this.fireworks = new Firework[initialSize];
		
	}

	/**
	 * Gets the fireworks display id
	 * 
	 * @return displayId of the fireworks
	 */
	public String getDisplaId() {
		return displayId;
	}

	/**
	 * gets the firework display name
	 * 
	 * @return FireworkName of the firework
	 */
	public String getFireworkName() {
		return FireworkName;
	}

	/**
	 * sets the firework display name
	 * 
	 * @param fireworkName firework display name
	 */
	public void setFireworkName(String fireworkName) {
		FireworkName = fireworkName;
	}

	/**
	 * gets the firework display theme
	 * 
	 * @return Theme of the firework display
	 */
	public String getTheme() {
		return Theme;
	}

	/**
	 * sets the firework display theme
	 * 
	 * @param theme theme of the firework display
	 */
	public void setTheme(String theme) {
		Theme = theme;
	}

	/**
	 * Gets the lead Tech details responsible for the firework
	 * 
	 * @return LeadTech details of the lead Technician
	 */
	public PyroTechnician getLeadTech() {
		return LeadTech;
	}

	/**
	 * sets the details of the lead Tech
	 * 
	 * @param leadTech details of the leadTech
	 */
	public void setLeadTech(PyroTechnician leadTech) {
		this.LeadTech = leadTech;
	}

	/**
	 * Gets the array with all the fireworks
	 * 
	 * @return fireworks array with all the fireworks
	 */
	public Firework[] getFireworks() {
		return fireworks;
	}

	/**
	 * Adds the firework into the array of the fireworks
	 * 
	 * @param objFirework new firework
	 */
	public void addFirework(Firework objFirework) {
		// increase the size of the firework array
		fireworks = resizeArray(fireworks);

		// if the new firework object is not null add it into the fireworks array
		if (objFirework instanceof Firework) {
			fireworks[fireworks.length - 1] = objFirework;
		}

	}

	/**
	 * Used to increase the size of the array
	 * @param copyFirework fireworks array to copy from
	 * @return temp resized array
	 * 
	 */
	private Firework[] resizeArray(Firework[] copyFirework) {

		// create an array with an increased size
		Firework[] temp = new Firework[copyFirework.length + 1];

		// function used to copy from copyfireworks array to the temp array
		System.arraycopy(copyFirework, 0, temp, 0, copyFirework.length);

		return temp;
	}
	
	/**
	  * Used to display the textual representation of the FireworkDisplay  class and the PyroTechnician class
	  * @return foramattedString
	  */
	@Override
	public String toString() {
		return String.format("Display ID:%s\t Display Name:%s\t Display Theme:%s\n", this.getDisplaId(),
				this.getFireworkName(), this.getTheme());
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
			System.err.format(" FireworkDispay line is invalid: %s\n",line);
			return false;
		}
		return true;
	}
	
	/**
	 * Creates a new FireworkDisplay from the line given and the pyroTech intance
	 * @param line that will be processed 
	 * @return FireworkDisplay
	 */
	public static FireworkDisplay procesLine(String line) {
		StringTokenizer token=new StringTokenizer(line,"\t");
		String id=token.nextToken();
		String name=token.nextToken();
		String theme=token.nextToken();
		
	
		return new FireworkDisplay(id,name,theme);
	}

	/**
	 * Used to display the toString method
	 */
	@Override
	public void display() {
		System.out.println(toString());
		
	}

}
