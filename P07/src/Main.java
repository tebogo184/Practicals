import acsse.csc2a.fmb.file.DisplayFileHandler;
import acsse.csc2a.fmb.model.FireworkDisplay;

public class Main {

	public static void main(String[] args) {
		//create the file handler
		DisplayFileHandler handler = new DisplayFileHandler();
		//load the dirty files that cannot not create a FireworkDisplay
		String[] dirtys = {
				"data/dirty_1.txt",
				"data/dirty_2.txt",
				"data/dirty_3.txt",
				"data/dirty_4.txt",

				
		};
		//load the partial files that will create a FireworkDisplay but have 1 corrupt Firework
		String[] partials = {
				"data/partial_1.txt",
				"data/partial_2.txt",
				"data/partial_3.txt",
				"data/partial_4.txt",
				"data/partial_5.txt",
				"data/partial_6.txt",
				"data/partial_7.txt",
				"data/partial_8.txt",
				"data/partial_9.txt",
				"data/partial_10.txt",
				"data/partial_11.txt",
				"data/partial_12.txt",
				
		};
		
		//load a clean file that will create a firework Display with 2 fireworks
		String clean1 = "data/clean_1.txt";
		
		//create an array to store the displays
		FireworkDisplay[] displays = new FireworkDisplay[13];
		int currentIndex = 0;

		
		System.out.println("*************************************************************\n");
		System.out.println("*                            Reading Files                  *\n");
		System.out.println("*************************************************************\n");
		System.out.println("======= CLEAN FILE: There should be no problems =============\n");
		
		System.out.printf("~~~~~~~~~~~~~     Working With: %s:\n", clean1);
		FireworkDisplay fd = handler.readDisplay(clean1);
		if(fd != null) {
			displays[currentIndex] = fd;
			currentIndex ++;
		}
		
		System.out.println("\n======= DIRTY FILEs: These should not create a Display =============\n");
		for(String file : dirtys)
		{
			System.out.printf("\n~~~~~~~~~~~~~     Working With: %s:\n", file);
			fd = handler.readDisplay(file);
			
			if(fd != null) {
				displays[currentIndex] = fd;
				currentIndex ++;
			}
			
		}
		
		System.out.println("\n======= PARTIAL FILEs: These should create a display but 1 firework is corrupt ====\n");
		for(String file : partials)
		{
			System.out.printf("~~~~~~~~~~~~~     Working With: %s:\n", file);
			fd = handler.readDisplay(file);
			
			if(fd != null) {
				displays[currentIndex] = fd;
				currentIndex ++;
			}
			
		}
		
		//Print the Firework Displays
		System.out.println("\n*************************************************************\n");
		System.out.println("                   Firework Management Bureau                \n");
		System.out.println("                  ==  Firework Management  =                 \n");
		System.out.println("*************************************************************\n");
		System.out.println("<<<<<        Printing Firework Display Information      >>>>>\n");
		int  validCounter = 0;
		for(FireworkDisplay display : displays)
		{
			if(display != null)
			{
				display.display();
				validCounter ++;
				System.out.println("\n\t\t* * *\n");
			}
			
		}
		System.out.printf("======= VALID Count is %s / 13 VALID files \n", validCounter);
		

	}

}
