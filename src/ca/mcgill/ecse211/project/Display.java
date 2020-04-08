package ca.mcgill.ecse211.project;
import static ca.mcgill.ecse211.project.Resources.*;
import java.text.DecimalFormat;

/**
 * This class is used to display the content of the odometer variables (x, y, theta).
 */
public class Display implements Runnable {
  
  /**
   * Constructor
   */
  public Display () {
  }
  /**
   * This double array stores the values of x,y and theta values of the odometer.
   * These values are what is displayed onto the screen of the robot
   */
  private double[] position;
 /**
  * Used in determining when the display thread should go to sleep
  */
  private static final long DISPLAY_PERIOD = 25;
  /**
   * Also used in determining when the display thread should go to sleep
   */
  private long timeout = Long.MAX_VALUE;
//----------------------------------------------------------------------------------------------
  /**
   * This method is responsible for displaying the x,y, and theta values of the odometer onto the screen of the robot
   */
  public void run() {

    lcd.clear();

    long updateStart;
    long updateEnd;

    long startTime = System.currentTimeMillis();
    do {
      updateStart = System.currentTimeMillis();

      // Retrieve x, y and Theta information
      position = odometer.getXyt();
     
      DecimalFormat numberFormat = new DecimalFormat("######0.00");
     
      
      lcd.drawString("US Distance: " + numberFormat.format(Sensors.getDistance()), 0, 0);
      lcd.drawString("X: " + numberFormat.format(position[0] / 30.48), 0, 1);
      lcd.drawString("Y: " + numberFormat.format(position[1] / 30.48), 0, 2);
      lcd.drawString("Theta: " + numberFormat.format(position[2]), 0, 3);
     
      
      
      // this ensures that the data is updated only once every period
      updateEnd = System.currentTimeMillis();
      if (updateEnd - updateStart < DISPLAY_PERIOD) {
        Main.sleepFor(DISPLAY_PERIOD - (updateEnd - updateStart));
      }
    } while ((updateEnd - startTime) <= timeout);

  }
//----------------------------------------------------------------------------------------------
  /**
   * Sets the timeout in ms.
   * 
   * @param timeout the timeout in milliseconds
   */
  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }
//----------------------------------------------------------------------------------------------
  /**
   * Shows the text on the LCD, line by line.
   * 
   * @param strings comma-separated list of strings, one per line
   */
  public static void showText(String... strings) {
    lcd.clear();
    for (int i = 0; i < strings.length; i++) {
      lcd.drawString(strings[i], 0, i);
    }
  }
  
} //end of class