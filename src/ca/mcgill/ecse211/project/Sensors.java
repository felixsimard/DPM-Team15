package ca.mcgill.ecse211.project;
import static ca.mcgill.ecse211.project.Resources.*;
import lejos.robotics.SampleProvider;

/**
 * This class is responsible for running the 3 colour sensors and the ultrasonic sensor in threads and storing the data returned
 * by each sensor.
 * Some methods in this class are threads themselves. The left wheel colour sensor and the right wheel colour sensor are in one thread.
 * The front colour sensor responsible for colour detection is in one thread. The ultrasonic sensor is in one thread.
 * Each thread has its own variable for putting the thread to sleep.
 * 
 *
 */
public class Sensors {

 // variables for the left wheel colour sensor and the right wheel colour sensor 
  
  /**
   * Sample provider for the left wheel colour sensor
   */
private static SampleProvider colourSensor_leftwheel_SampleProvider = colourSensor_leftwheel.getMode("Red");
/**
 * array to store the values returns by the left wheel colour sensor
 */
private static float colourData_leftwheel[] = new float[colourSensor_leftwheel_SampleProvider.sampleSize()];
/**
 * Variable to store the value returned by the left wheel colour sensor
 */
public static float value_of_colour_leftwheel;
/**
 * Sample provider for the right wheel colour sensor
 */
private static SampleProvider colourSensor_rightwheel_SampleProvider = colourSensor_rightwheel.getMode("Red");
/**
 * array to store the values returns by the right wheel colour sensor
 */
private static float colourData_rightwheel[] = new float[colourSensor_rightwheel_SampleProvider.sampleSize()];
/**
 * Variable to store the value returned by the right wheel colour sensor
 */
public static float value_of_colour_rightwheel;
// variables for the colour sensor used to detect/identify the vertical pin of the straned vehicle
/**
 * Sample provider for the colour sensor used for detecting the vertical pin of the stranded vehicle via colour detection
 */
private static SampleProvider colourSensor_front_SampleProvider = colourSensor_front.getRGBMode();
/**
 * array to store the values returns by the front colour sensor
 */
private static float colourData_front[] =  new float [colourSensor_front_SampleProvider.sampleSize()];
/**
 * Variable to store the value returned by the front colour sensor
 */
public static int RGBdata[] = new int[2];
// variables for the ultrasonic sensor
/**
 * Sample provider for the ultrasonic sensor
 */
private static SampleProvider myDistance = usSensor.getMode("Distance");
/**
 * array to store the values returned by the ultrasonic sensor
 */
private static float[] usData = new float[usSensor.sampleSize()]; 
/**
 * variable to store the value returned by the ultrasonic sensor
 */
public static int distance;   

//------------------------------------------------------------------------------------------------
/**
 * In this method, data is retrieved and refined from the left wheel colour sensor and the right wheel colour sensor.
 * This method itself is running in a thread.
 */
public static void getSamples_wheelColorSensors() {

(new Thread() {
  public void run() {

    while(true) {

      colourSensor_leftwheel_SampleProvider.fetchSample(colourData_leftwheel, 0);
      value_of_colour_leftwheel =   colourData_leftwheel[0] * 100;
      colourSensor_rightwheel_SampleProvider.fetchSample(colourData_rightwheel, 0);
      value_of_colour_rightwheel =   colourData_rightwheel[0] * 100;
      try {
        Thread.sleep(LIGHT_LOCALIZER_SLEEP);
      } catch (InterruptedException e) {
        //no code
      }
    } //end of while loop
  }     //end of run method
}).start();

} //end of getSample_localize method    

/**
 * 
 * @return the value returned by the left wheel colour sensor 
 */
public static float get_value_of_colour_leftwheel() {
return value_of_colour_leftwheel;
  }
/**
 * 
 * @return the value returned by the right wheel colour sensor 
 */
public static float get_value_of_colour_rightwheel() {
return value_of_colour_rightwheel;

  }
//------------------------------------------------------------------------------------------------
/**
 * In this method, data is retrieved and refined from the front colour sensor responsible for colour detection.
 * This method itself is running in a thread.
 */
public static void getSamples_frontColourSensor() {
  (new Thread() {
    public void run() {
      while(true) {
        colourSensor_front_SampleProvider.fetchSample(colourData_front, 0);
        RGBdata[0] = (int)Math.round(colourData_front[0]*1000); // Red
        RGBdata[1] = (int)Math.round(colourData_front[1]*1000); // Green
        RGBdata[2] = (int)Math.round(colourData_front[2]*1000); // Blue
        try {
          Thread.sleep(COLOUR_DETECTOR_SLEEP);
        } catch (InterruptedException e) {
          //no code
        }
      }
    }
  }).start();  
}
/**
 * 
 * @return the RGB values retrieved from the front colour sensor.
 */
public static int[] getRGBdata() {
  return RGBdata;
}
//------------------------------------------------------------------------------------------------

/**
 * In this method, data is retrieved and refined from the ultrasonic sensor.
 * This method itself is running in a thread.
 */
public static void readUsDistance() {
  (new Thread() {
     public void run() {
       while (true) {
       myDistance.fetchSample(usData, 0);
       distance = ((int) (usData[0] * 100.0));
       try {
         Thread.sleep(US_LOCALIZER_SLEEP);
       } catch (InterruptedException e) {
         //no code
       } 
      } //end of while loop
     } //end of run method
  }).start();
}

/**
 * 
 * @return Returns the distance calculated by the Ultrasonic sensor
 */
public static int getDistance() {     
  return distance;
}
//------------------------------------------------------------------------------------------------

} //end of Sensors class
