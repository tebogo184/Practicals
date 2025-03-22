package acsse.csc2a.fmb.model;



/**
 * This class defines all the attributes of the fireworks
 * 
 * @author Tshukudu T 222041905
 *
 */
public abstract class Firework {

	private String Id;
	private String fullname;
	private double FuseLength;
	private E_COLOUR enumColour;
	
	

	/**
	 * Parameterized constructor responsible for initializing member variables
	 * 
	 * @param id          firework id
	 * @param name        firework name
	 * @param fuse_length firework fuse length in seconds
	 * @param enum_colour firework colour
	 */
	public Firework(String id, String name, double fuse_length, E_COLOUR enum_colour) {

		Id = id;
		this.fullname = name;
		FuseLength = fuse_length;
		this.enumColour = enum_colour;
	}

	/**
	 * gets the firework id
	 * 
	 * @return Id firework id
	 */

	public String getId() {
		return Id;
	}

	/**
	 * gets the firework name
	 * 
	 * @return name firework name
	 */
	public String getName() {
		return fullname;
	}

	/**
	 * Gets the firework fuse length
	 * 
	 * @return FuseLength firework fuse length
	 */
	public double getFuse_length() {
		return FuseLength;
	}

	/**
	 * gets the firework colour
	 * 
	 * @return enumColour firework colour
	 */
	public E_COLOUR getEnum_colour() {
		return enumColour;
	}
	
	/**
	 * @return textualRepresentation of the class
	 */
	@Override
	public String toString() {
		return String.format("Id:%s\t Name:%s\t Fuse-Length:%ss\t Colour:%s\n", this.getId(), this.getName(),
				this.getFuse_length(), this.getEnum_colour());
	}
	
	
	
}
