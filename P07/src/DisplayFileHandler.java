package acsse.csc2a.fmb.file;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import acsse.csc2a.fmb.model.FireworkDisplay;
import acsse.csc2a.fmb.model.FountainFirework;
import acsse.csc2a.fmb.model.PyroTechnician;
import acsse.csc2a.fmb.model.RocketFirework;

/**
 * This class is used to read files and display information read
 * @author Tshukudu T 222041905
 * 
 *
 */
public class DisplayFileHandler{
	
	
	
	/**
	 * This method reads a file.
	 * It display the the information of the file it read
	 * @param filename - the name of the file to be read
	 * @return FireworkDisplay
	 */
	public FireworkDisplay readDisplay(String filename) {
		
		Scanner scFile=null;
	   FireworkDisplay fireworkDisplay=null;
		
		
		try {
			//Use the scanner to read the file
			scFile= new Scanner(new File(filename));
			
			
			if(scFile.hasNextLine()) {
				
				//process the FirewokDisplay line.If the line is invalid an error will be displayed
				//If the fireworkDisplay line is valid the pyroLine will be processed
				String FDline=scFile.nextLine();
				if(FireworkDisplay.validate(FDline)) {
					
					fireworkDisplay=FireworkDisplay.procesLine(FDline);
					fireworkDisplay.display();//display the fireworkDisplay details
				
					
					//process the pyroTechician Line
					String pryroLine =scFile.nextLine();
					if(PyroTechnician.validate(pryroLine)) {
						
						if(fireworkDisplay!=null) {
						PyroTechnician pyroTech =PyroTechnician.proccessLine(pryroLine);
						 
						 fireworkDisplay.setLeadTech(pyroTech);
						 pyroTech.display();//display the PyroTech details
						 				
					}
				}
					
				}
				
				//process the Fountain Fireworks line 
				String fountainLine=scFile.nextLine();
				if(FountainFirework.validate(fountainLine)) {
					
					if(fireworkDisplay!=null) {
					FountainFirework fountainFirework=FountainFirework.proccessLine(fountainLine);
					
					fireworkDisplay.addFirework(fountainFirework);//add the firework to the fireworkDetails
					
					fountainFirework.display();//display the Fountain fireworks details
					
					}
				}
				
				
				
				//process the RocketFireworkline
				String rocketLine=scFile.nextLine();
				
				
				if(RocketFirework.validate(rocketLine)) {
					
				
					if(fireworkDisplay!=null) {
					RocketFirework rocketFirework=RocketFirework.proccessLine(rocketLine);
					fireworkDisplay.addFirework(rocketFirework);//add the FontianFirework
					rocketFirework.display();//display the Fountain Firework information
					}
					
				}
			
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(scFile!=null) {
				scFile.close();
			}
		}
		
		
		return fireworkDisplay;
	}

	
	
	

}
