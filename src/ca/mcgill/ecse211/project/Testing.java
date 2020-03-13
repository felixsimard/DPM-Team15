package ca.mcgill.ecse211.project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;



import lejos.hardware.Sound;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

import static ca.mcgill.ecse211.project.Resources.*;
import java.util.ArrayList;
import java.util.Collections;


public class Testing {
  
  public static final String Test=""; 

//  public static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
  public static final TextLCD TEXT_LCD = LocalEV3.get().getTextLCD();

  private ArrayList<ArrayList<Double>> distanceReadings = new ArrayList<ArrayList<Double>>();
  private float[] colorData = new float[50];
  private float[] Data = new float[200];
  static File file;
  static String[] testData = new String[200];
  
  
  
  //constants for testing 
  private static int numOfReadings=100; // used for continuous readings 
  private static int timeBtwReadings=200; //in ms 
  
  //test names 
  /**
   * usSensorCurve: to trace the outline of obstacle when rotating 
   * usSensorDistance: to detect distance to obstacle when moving towards it 
   * OdoRotAccuracy: check theta when car rotates 360 deg 
   * 
   * USLocalization:  
   */
  /**
   * Method called when testing 
   * In the main Method, do the following
   * 1. Start the necessary threads 
   * 2. Start motors if needed
   * 3. Call the test method with input as the name of the test 
   * @param test
   */
  public static void test(String test) {
    
  //-------------------------------INITIALIZE DISPLAY----------------------------------------//
    TEXT_LCD.drawString("Test:"+test, 0, 0);
    TEXT_LCD.drawString("Press button to start", 0, 1);
    Button.waitForAnyPress();                                   
    TEXT_LCD.clear();
  //---------------------TEST FOR TRACING OF THE US SENSOR DATA FOR OBSTACLE ---------------//
    if (test.equals("usSensorCurve")) {
       
          int testIndex = 0;
          //continuous reading of us sensor 
          while(testIndex < numOfReadings) {
            
            //create arraylist
            ArrayList<Double> distanceReading =getDistance(); 
            //print list
            System.out.println(distanceReading);
            
            //add the trial number and the reading to string into the testData string array 
            testData[testIndex] = (testIndex+1) + ", " + distanceReading.get(0).toString();
          
            //sleep the testing class  
            try {
              Thread.sleep(timeBtwReadings);
            } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            //increment testIndex 
          testIndex++;
            
          }
          
          //prompt stop 
          TEXT_LCD.drawString("Press button to stop", 0, 1);
          Button.waitForAnyPress(); 
          TEXT_LCD.clear();
         
          // CREATE CSV  
         file = createfile("usSensorCurve.csv");
      
           try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file))) {
              if (! file.exists()) {
                  file.createNewFile();
              }
              //.append each column 
              csvWriter.append("Test # ");
              csvWriter.append(", ");
              csvWriter.append("Distance (cm)");
              
              csvWriter.newLine();
              
              for ( String data : testData) {
                  csvWriter.append(data);
                  csvWriter.newLine();
              }
              
              csvWriter.flush();
      
          } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
   
      
    }
    //---------------------TEST FOR sensor moving towards obstacle ---------------//
    
    else if(test.equals("usSensorDistance")) {
      
          int testIndex = 0;
          odometer.setY(0);
          
          while(testIndex < numOfReadings) {
          ArrayList<Double> distanceReading =getDistance(); 
            System.out.println(distanceReading);
            
            
          testData[testIndex] = (testIndex+1) + ", " + distanceReading.get(0).toString() + ", " +odometer.getXyt()[1] ;
          try {
            Thread.sleep(timeBtwReadings);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          testIndex++;
            
          }
          TEXT_LCD.drawString("Press button to stop", 0, 1);
          Button.waitForAnyPress(); 
          TEXT_LCD.clear();
         // CREATE CSV  
         file = createfile("usSensorDistance.csv");
      try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file))) {
            if (! file.exists()) {
                file.createNewFile();
            }
            csvWriter.append("Test # ");
            csvWriter.append(", ");
            csvWriter.append("Distance (cm)");
            csvWriter.append("Distance Travelled (cm)"); 
            csvWriter.newLine();
            
            for ( String data : testData) {
                csvWriter.append(data);
                csvWriter.newLine();
            }
            
            csvWriter.flush();
    
       } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
       }
    }
  //---------------------TEST ODOMETER ---------------//
    else if (test.equals("OdoRotAccuracy")) {
      int testIndex = 0;
      odometer.setTheta(0);
      
      while(testIndex < numOfReadings) {
      
        System.out.println(odometer.getXyt()[2]);

        
      testData[testIndex] = (testIndex+1) + ", " + odometer.getXyt()[2];
      try {
        Thread.sleep(timeBtwReadings);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      testIndex++;
        
      }
      TEXT_LCD.drawString("Press button to stop", 0, 1);
      Button.waitForAnyPress(); 
      TEXT_LCD.clear();
     // CREATE CSV  
     file = createfile("OdoRotAccuracy.csv");
  try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file))) {
        if (! file.exists()) {
            file.createNewFile();
        }
        csvWriter.append("Sample number");
        csvWriter.append(", ");
        csvWriter.append("Heading (deg)");
        
        csvWriter.newLine();
        
        for ( String data : testData) {
            csvWriter.append(data);
            csvWriter.newLine();
        }
        
        csvWriter.flush();

   } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
   }
    }
  //---------------------TEST FOR US LOCALIZATION ---------------//
    else if (test.equals("USLocalization & odoAccuracy")) {
      int testIndex = 0;  
      while(testIndex < 10) {
        
        
        TEXT_LCD.drawString("Trial #"+testIndex, 0, 0);
        TEXT_LCD.drawString("Press btn to calibrate.", 0, 1);
        Button.waitForAnyPress();
        TEXT_LCD.clear();
        
       
       
        

        
        System.out.println("x Reading: " + odometer.getXyt()[0]);
        System.out.println("y Reading: " + odometer.getXyt()[1]);
        System.out.println("theta Reading: " + odometer.getXyt()[2]);
        double idealAngle=45; 
        double error=Math.abs(odometer.getXyt()[2]-idealAngle); 
      testData[testIndex] = (testIndex+1) + ", " + odometer.getXyt()[0]+ ", " +
          odometer.getXyt()[1]+", "+ odometer.getXyt()[2] + error ;

      testIndex++;
        
      }
      file = createfile("usLocalization & odoAccuracy.csv");
      try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file))) {
            if (! file.exists()) {
                file.createNewFile();
            }
            csvWriter.append("Test # ");
            csvWriter.append(", ");
            csvWriter.append("x reading (cm)");
            csvWriter.append(", ");
            csvWriter.append("y reading (cm)"); 
            csvWriter.append(", ");
            csvWriter.append("theta reading (degrees)"); //mostly care about this one 
            csvWriter.append(", "); 
            csvWriter.append("error |theta-45|"); 
            csvWriter.newLine();
            
            for ( String data : testData) {
                csvWriter.append(data);
                csvWriter.newLine();
            }
            
            csvWriter.flush();
    
       } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
       }
      
    }
    
  }
  
  public void calibrate()  {
    int testIndex = 0;
  
//      colorReadings.add(new ArrayList<Double>());
//      colorReadings.add(new ArrayList<Double>());
//      colorReadings.add(new ArrayList<Double>());
    
    while(testIndex < 10) {
     

      TEXT_LCD.drawString("Calibration #"+testIndex, 0, 0);
      TEXT_LCD.drawString("Press btn to calibrate.", 0, 1);
      Button.waitForAnyPress();
      TEXT_LCD.clear();
      ArrayList<Double> rgbReading = getColorData();
      
//      colorReadings.get(0).add(new Double(rgbReading.get(0)));
//      colorReadings.get(1).add(new Double(rgbReading.get(1)));
//      colorReadings.get(2).add(new Double(rgbReading.get(2)));
      
      System.out.println("Reading: " + rgbReading);
      
    testData[testIndex] = (testIndex+1) + ", " + rgbReading.get(0).toString()+ ", " +
                       rgbReading.get(1).toString()+", "+ rgbReading.get(2).toString() ;

    testIndex++;
      
    }
    
    createCSV(testData);
    
    //System.out.println("Output: " + colorReadings);
   
  }


  public  ArrayList<Double> getColorData() {
    colorData =  ColorSensor.getRGBdata();
    ArrayList<Double> out = new ArrayList<Double>();
    // line below does not work, simply need to get RGB from color sensor as an array like: [R, G, B]
    // works now
    out.add(new Double(colorData[0]));
    out.add(new Double(colorData[1]));
    out.add(new Double(colorData[2]));
    return out;
  }
  public static ArrayList<Double> getDistance(){
    
    int data=USSensor.readUsDistance(); 
    ArrayList<Double> out = new ArrayList<Double>();
    out.add(new Double (data)); 
    return out; 
    
  }
  
  /**
   *  checks if file has been created successfully
   * @return
   */
  public static File createfile(String fileName) {
      return new File(fileName);
  }

//-------------------------------Setting up a CSV File----------------------------------------//
  
  public static void createCSV(String[] arr) {
  file = createfile("color_sensor.csv");
  try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file))) {
        if (! file.exists()) {
            file.createNewFile();
        }
        csvWriter.append("Test # ");
        csvWriter.append(", ");
        csvWriter.append("RED");
        csvWriter.append(", ");
        csvWriter.append("GREEN");
        csvWriter.append(", ");
        csvWriter.append("BLUE");
        csvWriter.newLine();
        
        for ( String data : arr) {
            csvWriter.append(data);
            csvWriter.newLine();
        }
        
        csvWriter.flush();

    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
//--------------------------------------------------------------------------------------------//
public static void testMotorDisp() {
  int testIndex = 0;  
  double idealAngle=45; 
  double error=Math.abs(odometer.getXyt()[2]-idealAngle); 
testData[testIndex] = (testIndex+1) + ", " + odometer.getXyt()[0]+ ", " +
    odometer.getXyt()[1]+", "+ odometer.getXyt()[2] + error ;

  Button.waitForAnyPress();                                   
  TEXT_LCD.clear();
  file = createfile("testMotorDisp.csv");
  try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(file))) {
        if (! file.exists()) {
            file.createNewFile();
        }
        csvWriter.append("Test # ");
        csvWriter.append(", ");
        csvWriter.append("x reading (cm)");
        csvWriter.append(", ");
        csvWriter.append("y reading (cm)"); 
        csvWriter.append(", ");
        csvWriter.append("theta reading (degrees)"); //mostly care about this one 
        csvWriter.append(", "); 
        csvWriter.append("error |theta-45|"); 
        csvWriter.newLine();
        
        for ( String data : testData) {
            csvWriter.append(data);
            csvWriter.newLine();
        }
        
        csvWriter.flush();

   } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
   }
}
}



