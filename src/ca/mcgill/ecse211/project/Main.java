package ca.mcgill.ecse211.project;

//static import to avoid duplicating variables and make the code easier to read
import static ca.mcgill.ecse211.project.Resources.*;

import lejos.hardware.Button;

/**
 * The main driver class for the lab.
 */
public class Main {
 
  /**
   * The main entry point.
   * 
   * @param args not used
   */
  public static void main(String[] args) {
    
   
    // Start threads here
    
    
    Button.waitForAnyPress();
    System.exit(0);


  }
 

  /**
   * Sleeps for the specified duration.
   * @param millis the duration in milliseconds
   */
  public static void sleepFor(long millis) {           
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      // Nothing to do here
    }
  }

}
