package ca.mcgill.ecse211.project;


import static ca.mcgill.ecse211.project.Resources.*;
import lejos.hardware.Button;
import static ca.mcgill.ecse211.project.Main.sleepFor;

public class Navigation {
  /**
   * the x displacement of our desired destination in feet
   */
  int xDest_FT;
  /**
   * the y displacement of our desired destination in feet  
   */
  int yDest_FT;  
  /**
   * the current x displacement of the EV3 in cm
   */
  double xCurrent_CM;
  /**
   * the current y displacement of the EV3 in cm
   */
  double yCurrent_CM;
  /**
   * the current clockwise angle of the EV3 from the 0 axis
   */
  double thetaCurrent_RAD; //angle from vertical CW
 
  double thetacurrent_RAD_Localizer;
  /**
   *  the displacement change required in the x-axis to reach our desired destination
   */
  double displX;
  /**
   *  the displacement change required in the y-axis to reach our desired destination
   */
  double displY;
  /**
   * the angle from the 0 axis that we need to rotate to in order to go in the direction of our desired destination
   */
  double displTheta_RAD;
  /**
   * the distance in cm we need to travel to reach our desired destination
   */
  double distance_needed_to_cover;
  
  /**
   * Map data
   */
  int[][] map0 = {{1, 3}, {2, 2}, {3, 3}, {3, 2}, {3, 1}};
  int[][] chosenMap;
  
  int corner = 3;
  int ZoneLLx = 0;
  int ZoneLLy = 0;
  int ZoneURx = 4;
  int ZoneURy = 4;
  int TunnelLLx = 2;
  int TunnelLLy = 2;
  int TunnelURx = 3;
  int TunnelURy = 4;
  
  int startingX;
  int startingY;
  int destX;
  int destY;
  int distX;
  int distY;
  int reachedX;
  int reachedY;
  int tempdistX;
  int tempdistY;
  
  double tunnelWidth;
  
//-----------------------------------------------------------------------------------------------------------------------
  public void run( ) {
      
    if (corner == 0) {
      
      startingX = 1;
      startingY = 1;
      odometer.setXyt( startingX*TILE_SIZE_cm, startingY*TILE_SIZE_cm, 0);
      reachedX = 1;
      reachedY = 1;
      if (Math.abs(TunnelURy - TunnelLLy) < Math.abs(TunnelURx - TunnelLLx)) {      //tunnel is horizontal
        System.out.println("in horizontal tunnel");
        tunnelWidth = (TunnelURy - TunnelLLy) * 30.48;
        if (TunnelLLy == 0) {          //exception case
          destX = TunnelLLx - 1;
          destY =  TunnelURy;
          distX = destX - startingX;
          distY = destY - startingY; 
          NavigateXY(); 
          UltrasonicLocalizer.turnBy(180);
          moveStraightFor2(tunnelWidth/2);
          UltrasonicLocalizer.turnBy(-90);
        }
        else {
        destX = TunnelLLx - 1;
        destY = TunnelLLy;
        distX = destX - startingX;
        distY = destY - startingY;      
        NavigateXY();       
        moveStraightFor2(tunnelWidth/2);
        UltrasonicLocalizer.turnBy(90);
        }
        
      } //end of horizontal tunnel
      else {                           
        tunnelWidth = (TunnelURx - TunnelLLx) * 30.48;
        if (TunnelLLx == 0) {   // exception case
          destX = TunnelURx;
          destY = TunnelLLy - 1;
          distX = destX - startingX;
          distY = destY - startingY;
          NavigateXY(); 
          UltrasonicLocalizer.turnBy(-90);
          moveStraightFor2(tunnelWidth/2);
          UltrasonicLocalizer.turnBy(90);
        }
        else {
        destX = TunnelLLx;
        destY = TunnelLLy - 1;
        distX = destX - startingX;
        distY = destY - startingY;
        NavigateXY();
        UltrasonicLocalizer.turnBy(90);
        moveStraightFor2(tunnelWidth/2);
        UltrasonicLocalizer.turnBy(-90);
        }
      } // end of vertical tunnel
   
    } //end of if(corner == 0)
    
    else  if (corner == 1) {
      
      startingX = 14;
      startingY = 1;
      odometer.setXyt(startingX*TILE_SIZE_cm, startingY*TILE_SIZE_cm, 0);
      reachedX = 14;
      reachedY = 1;
      if (Math.abs(TunnelURy - TunnelLLy) < Math.abs(TunnelURx - TunnelLLx)) {      //tunnel is horizontal
        tunnelWidth = (TunnelURy - TunnelLLy) * 30.48;   
        if (TunnelLLy == 0) {  // exception case
          destX = TunnelURx + 1;
          destY = TunnelURy;
          distX = destX - startingX;
          distY = destY - startingY;  
          NavigateXY(); 
          UltrasonicLocalizer.turnBy(180);
          moveStraightFor2(tunnelWidth/2);
          UltrasonicLocalizer.turnBy(90);
        }
        else {
        destX = TunnelURx + 1;
        destY = TunnelLLy;
        distX = destX - startingX;
        distY = destY - startingY;    
        NavigateXY();      
        moveStraightFor2(tunnelWidth/2);
        UltrasonicLocalizer.turnBy(-90);
        }
        
      } //end of horizontal tunnel
      else {
        tunnelWidth = (TunnelURx - TunnelLLx) * 30.48;
        if (TunnelURx == 15) {  //exception case
          destX = TunnelLLx;
          destY = TunnelLLy - 1;
          distX = destX - startingX;
          distY = destY - startingY;
          NavigateXY();
          UltrasonicLocalizer.turnBy(90);
          moveStraightFor2(tunnelWidth/2);
          UltrasonicLocalizer.turnBy(-90);
        }
        else {
        destX = TunnelURx;
        destY = TunnelLLy - 1;
        distX = destX - startingX;
        distY = destY - startingY;
        NavigateXY();
        UltrasonicLocalizer.turnBy(-90);
        moveStraightFor2(tunnelWidth/2);
        UltrasonicLocalizer.turnBy(90);
        }
        
      } // end of vertical tunnel
   
    } //end of if(corner == 1)
    
    else  if (corner == 2) {
      
      startingX = 14;
      startingY = 8;
      odometer.setXyt(startingX*TILE_SIZE_cm, startingY*TILE_SIZE_cm, 180);
      reachedX = 14;
      reachedY = 8;
      if (Math.abs(TunnelURy - TunnelLLy) < Math.abs(TunnelURx - TunnelLLx)) {      //tunnel is horizontal
        tunnelWidth = (TunnelURy - TunnelLLy) * 30.48;   
       
        if (TunnelURy == 9) {           //exception case
          destX = TunnelURx + 1;
          destY = TunnelLLy;
          distX = destX - startingX;
          distY = destY - startingY;  
          NavigateXY(); 
          moveStraightFor2(tunnelWidth/2);
          UltrasonicLocalizer.turnBy(-90);
        }
        else {
        destX = TunnelURx + 1;
        destY = TunnelURy;
        distX = destX - startingX;
        distY = destY - startingY;     
        NavigateXY();    
        UltrasonicLocalizer.turnBy(180);
        moveStraightFor2(tunnelWidth/2);
        UltrasonicLocalizer.turnBy(90);
        }
        
      } //end of horizontal tunnel
      else {
        tunnelWidth = (TunnelURx - TunnelLLx) * 30.48;
        if (TunnelURx == 15) {      //exception case
          destX = TunnelLLx;
          destY = TunnelURy + 1;
          distX = destX - startingX;
          distY = destY - startingY;
          UltrasonicLocalizer.turnBy(90);
          moveStraightFor2(tunnelWidth/2);
          UltrasonicLocalizer.turnBy(90);
        }
        else {
        destX = TunnelURx;
        destY = TunnelURy + 1;
        distX = destX - startingX;
        distY = destY - startingY;
        NavigateXY();
        UltrasonicLocalizer.turnBy(-90);
        moveStraightFor2(tunnelWidth/2);
        UltrasonicLocalizer.turnBy(-90);
        }   
      } // end of vertical tunnel
   
    } //end of if(corner == 2)
    
    else  if (corner == 3) {
      
      startingX = 1;
      startingY = 8;
      odometer.setXyt(startingX*TILE_SIZE_cm, startingY*TILE_SIZE_cm, 180);
      reachedX = 1;
      reachedY = 8;
      if (Math.abs(TunnelURy - TunnelLLy) < Math.abs(TunnelURx - TunnelLLx)) {      //tunnel is horizontal
        tunnelWidth = (TunnelURy - TunnelLLy) * 30.48;   
       
        if (TunnelURy == 9) { //exception case
          destX = TunnelLLx - 1;
          destY = TunnelLLy;
          distX = destX - startingX;
          distY = destY - startingY; 
          NavigateXY(); 
          moveStraightFor2(tunnelWidth/2);
          UltrasonicLocalizer.turnBy(90);
        }
        else {
        destX = TunnelLLx - 1;          
        destY = TunnelURy;
        distX = destX - startingX;
        distY = destY - startingY;      
        NavigateXY();       
        UltrasonicLocalizer.turnBy(180);
        moveStraightFor2(tunnelWidth/2);
        UltrasonicLocalizer.turnBy(-90);
        }
        
      } //end of horizontal tunnel
      else {
        tunnelWidth = (TunnelURx - TunnelLLx) * 30.48;
       
        if (TunnelLLx == 0) {     //exception case
          destX = TunnelURx;
          destY = TunnelURy + 1;
          distX = destX - startingX;     
          distY = destY - startingY;
          NavigateXY();
          UltrasonicLocalizer.turnBy(-90);
          moveStraightFor2(tunnelWidth/2);
          UltrasonicLocalizer.turnBy(-90);
        }
        else {
        destX = TunnelLLx;         
        destY = TunnelURy + 1;     
        distX = destX - startingX;     
        distY = destY - startingY;   
        NavigateXY();
        UltrasonicLocalizer.turnBy(90);
        moveStraightFor2(tunnelWidth/2);
        UltrasonicLocalizer.turnBy(90);
        }
      } // end of vertical tunnel
   
    } //end of if(corner == 3)
    
//    chosenMap = map0;
//    odometer.setXyt(1*TILE_SIZE_cm, 1*TILE_SIZE_cm, 0);
//    if (USLocalDone) {
//      for(int i=0; i < chosenMap.length; i++) {
//        xDest = chosenMap[i][0];
//        yDest = chosenMap[i][1];
//        travelTo(xDest, yDest);
//        turnTo_LightLocalizer(0);
//        LightLocalizer.localizeDuringNavigation();
//        odometer.setXyt(xDest*TILE_SIZE_cm, yDest*TILE_SIZE_cm, 0);
//      }
//    }
//    else {
//      LightLocalizer.getSample_localize();
//      for(int i=0; i < chosenMap.length; i++) {
//        xDest = chosenMap[i][0];
//        yDest = chosenMap[i][1];
//        travelTo(xDest, yDest);
//        turnTo_LightLocalizer(0);
//        LightLocalizer.localizeDuringNavigation();
//        odometer.setXyt(xDest*TILE_SIZE_cm, yDest*TILE_SIZE_cm, 0);
//      }
//    }
 
// 
//    odometer.setXyt(30.48, 30.48, 0);
//  LightLocalizer.getSample_localize(); //starts taking samples
//  
//    travelTo(1,3);
//    turnTo_LightLocalizer(0);
//   LightLocalizer.localizeDuringNavigation();
//   odometer.setXyt(1*30.48, 3*30.48, 0);
//   //--
//   travelTo(2,2);
//   turnTo_LightLocalizer(0);
//   LightLocalizer.localizeDuringNavigation();
//   odometer.setXyt(2*30.48, 2*30.48, 0);
//   //--
//   travelTo(3,3);
//   turnTo_LightLocalizer(0);
//   LightLocalizer.localizeDuringNavigation();
//   odometer.setXyt(3*30.48, 3*30.48, 0);
//   //--
//   travelTo(3,2);
//   turnTo_LightLocalizer(0);
//   LightLocalizer.localizeDuringNavigation();
//   odometer.setXyt(3*30.48, 2*30.48, 0);
//   //--
//   travelTo(2,1);
//   turnTo_LightLocalizer(0);
//   LightLocalizer.localizeDuringNavigation();
//   odometer.setXyt(2*30.48, 1*30.48, 0);
   // done
  }
  
//----------------------------------------------------------------------------------------------------
  /**
   * This method takes as input the angle we wish to turn to, it then turns the EV3 to our desired angle
   * @param angle_RAD
   */
  public void turnTo(double angle_RAD) {           //has to turn by minimal angle
   
    double deltaT = angle_RAD*(180/Math.PI) -  thetaCurrent_RAD*(180/Math.PI);
    
    if (deltaT >= 0 && deltaT <= 180 ) {
      UltrasonicLocalizer.turnBy(deltaT);          
    }
    else if (deltaT > 180 ) {
      UltrasonicLocalizer.turnBy(deltaT -360 );
    }
    else if (deltaT < 0 && deltaT > -180 ) {
      UltrasonicLocalizer.turnBy(deltaT);
    }
    else if (deltaT < 0 && deltaT < -180 ) {
      UltrasonicLocalizer.turnBy(deltaT + 360);
    }
  } //end of turnTo method
  
 //--------------------------------------------------------------------------------------------------- 
  public void turnTo_LightLocalizer(double angle_RAD) {           //has to turn by minimal angle
    
    thetacurrent_RAD_Localizer = odometer.getXyt()[2] * RADS_PER_1DEG;
    double deltaT = angle_RAD*(180/Math.PI) -  thetacurrent_RAD_Localizer*(180/Math.PI);
    
    if (deltaT >= 0 && deltaT <= 180 ) {
      UltrasonicLocalizer.turnBy(deltaT);          
    }
    else if (deltaT > 180 ) {
      UltrasonicLocalizer.turnBy(deltaT -360 );
    }
    else if (deltaT < 0 && deltaT > -180 ) {
      UltrasonicLocalizer.turnBy(deltaT);
    }
    else if (deltaT < 0 && deltaT < -180 ) {
      UltrasonicLocalizer.turnBy(deltaT + 360);
    }
  } //end of turnTo_Localizer method
//----------------------------------------------------------------------------------------------------  
  /**
   * This method takes in the (x,y) coordinates of where we want to go, it then causes the EV3 to rotate and move to that specific
   * coordinate
   * @param x
   * @param y
   */
  public void travelTo(int x,int y) {  
        
    xDest_FT= x; //the x position (in feet) we want to reach
    yDest_FT= y; // the y position (in feet) we want to reach
    //get current position
    xCurrent_CM=odometer.getXyt()[0];   // our current x position in cm
    yCurrent_CM=odometer.getXyt()[1];   // our current y position in cm
    thetaCurrent_RAD=odometer.getXyt()[2] * RADS_PER_1DEG ;  // our current angle from the 0 degree axis
    
    displX= xDest_FT*TILE_SIZE_cm - xCurrent_CM;    //displX = the distance we need to travel in the x axis to reach where we want
    displY= yDest_FT*TILE_SIZE_cm - yCurrent_CM;    // displY = the distance we need to travel in the y axis to reach where we want
    
     if (displX != 0 && displY != 0)  {            // if we do not want to stay in the same position then..
    
       //1st quadrant 
        if (displX>0 && displY>0) {                                 
          displTheta_RAD=PI/2.0 - Math.atan(displY/displX);
          distance_needed_to_cover =  Math.sqrt((displX*displX) + (displY*displY));
          turnTo(displTheta_RAD);
          moveStraightFor2(distance_needed_to_cover);
          if (travelToNotCompleted) {
            completeTravelTo();
          }
        }
        //2nd quadrant
        else if (displX<0 && displY>0)                             
        {
          displTheta_RAD=1.5*PI + Math.atan(Math.abs(displY/displX)); // pi + (pi/2+angle) 
          distance_needed_to_cover =  Math.sqrt((displX*displX) + (displY*displY));
          turnTo(displTheta_RAD);
          moveStraightFor2(distance_needed_to_cover);
          if (travelToNotCompleted) {
            completeTravelTo();
          }
        }
        //3nd quadrant
        else if (displX<0 && displY<0)                             
        {
          displTheta_RAD =1.5*PI-Math.atan(Math.abs(displY/displX));  // pi + (pi/2-angle) 
          distance_needed_to_cover =  Math.sqrt((displX*displX) + (displY*displY));
          turnTo(displTheta_RAD);
          moveStraightFor2(distance_needed_to_cover);
          if (travelToNotCompleted) {
            completeTravelTo();
          }
        }
        //4th quadrant 
        else                                                        
        {  
          displTheta_RAD=0.5*PI+ Math.atan(Math.abs(displY/displX));
          distance_needed_to_cover =  Math.sqrt((displX*displX) + (displY*displY));// (pi/2) + angle 
          turnTo(displTheta_RAD);
          moveStraightFor2(distance_needed_to_cover);
          if (travelToNotCompleted) {
            completeTravelTo();
          }
        }
        
     } //end of if statement
     
     //vertical displacement 
    else if (displX==0)                                 
    {
        if     (displY>=0) displTheta_RAD=0;  //displacement forward
        else if(displY<0)  displTheta_RAD=PI; //displacement backward
        distance_needed_to_cover =  displY;
        turnTo(displTheta_RAD);
        moveStraightFor2(Math.abs(distance_needed_to_cover));
        if (travelToNotCompleted) {
          completeTravelTo();
        }
    }
     //horizontal displacement
    else if (displY==0)                     
    {
      if     (displX>0)   displTheta_RAD=PI/2.0; //displacement to the right
      else if(displX<0)   displTheta_RAD=1.5*PI; //displacement to the left 
      distance_needed_to_cover =  displX;
      leftMotor.setSpeed(50);
      rightMotor.setSpeed(50);
      turnTo(displTheta_RAD);
      moveStraightFor2(Math.abs(distance_needed_to_cover));
      if (travelToNotCompleted) {
        completeTravelTo();
      }
    }
  } //end of travelTo method
//-----------------------------------------------------------------------------------------------------------------------
  
  /**
   * Moves the robot straight for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveStraightFor(double distance) {
    leftMotor.setSpeed(ROTATION_SPEED);
    rightMotor.setSpeed(ROTATION_SPEED);
    leftMotor.rotate(UltrasonicLocalizer.convertDistance(distance), true);
    rightMotor.rotate(UltrasonicLocalizer.convertDistance(distance), false);
  }
  
  public static void moveStraightFor2(double distance) {
    
    int initialTacho = leftMotor.getTachoCount();
    int requiredTacho = UltrasonicLocalizer.convertDistance(distance);
    int currentTacho = initialTacho;
    
    while ((currentTacho - initialTacho) <  requiredTacho ) {

//########################################################################################
//    while (OBJECT_DETECTED) { 
//      continue; //when you need to jump to the next iteration of the loop immediately
//    } //end of inner while loop
//########################################################################################
     
      if (OBJECT_DETECTED) {
        travelToNotCompleted = true;
        return;
      }
      
    leftMotor.setSpeed(FWD_SPEED);
    rightMotor.setSpeed(FWD_SPEED);
    leftMotor.forward();
    rightMotor.forward();
    
    currentTacho = leftMotor.getTachoCount();
    } //end of outer while loop
    leftMotor.stop(true);
    rightMotor.stop(false);
  } //end of method
  
//-----------------------------------------------------------------------------------------------------------------------
  public void completeTravelTo() {
    while (obstacleAvoidanceInProgress) {
      continue;
    }
    //once obstacle avoidance is done
    travelToNotCompleted = false;
    travelTo(xDest,yDest);
  }
  
  public void CompleteLightLocalization_Direction_X() {
  turnTo_LightLocalizer(0);
  LightLocalizer.localizeDuringNavigation();
  odometer.setXyt(reachedX*TILE_SIZE_cm, startingY*TILE_SIZE_cm, 0);
  }
  
  public void CompleteLightLocalization_Direction_Y() {
  turnTo_LightLocalizer(0);
  LightLocalizer.localizeDuringNavigation();
  odometer.setXyt(reachedX*TILE_SIZE_cm, reachedY*TILE_SIZE_cm, 0);
  }
  
  public void NavigateXY() {
    if (distX == 0) {
      // do nothing
    }
    else if (distX == 1) {
     travelTo(reachedX + 1, 7);
     reachedX += 1;
     CompleteLightLocalization_Direction_X();
     distX = distX - 1;
    }
    else if (distX == -1) {
      travelTo(reachedX - 1, 1);
      reachedX -= 1;
      CompleteLightLocalization_Direction_X();
      distX += 1;
    }
    else if ((distX % 2 == 0) && distX > 0) {
      
      while (distX != 0) {
        travelTo( reachedX + 2, 7);
        reachedX += 2;
        CompleteLightLocalization_Direction_X();
        distX -= 2;
      } //end of while loop
    }
    
    else if (!(distX % 2 == 0) && distX > 0) {
        tempdistX = distX - 1;
        
        while (tempdistX != 0) {
          travelTo(reachedX + 2, 7);
          reachedX += 2;
          CompleteLightLocalization_Direction_X();
          tempdistX -= 2;
        }
        
        travelTo(reachedX + 1, 1);
        reachedX += 1;
        CompleteLightLocalization_Direction_X();
        distX = 0;
    }
    else if ((distX % 2 == 0) && distX < 0) {
      
      while (distX != 0) {
        travelTo( reachedX - 2, 7);
        reachedX -= 2;
        CompleteLightLocalization_Direction_X();
        distX += 2;
      } //end of while loop
    }
    
    else if (!(distX % 2 == 0) && distX < 0) {
        tempdistX = distX + 1;
        
        while (tempdistX != 0) {
          travelTo(reachedX - 2, 7);
          reachedX -= 2;
          CompleteLightLocalization_Direction_X();
          tempdistX += 2;
        }
        
        travelTo(reachedX - 1, 7);
        reachedX -= 1;
        CompleteLightLocalization_Direction_X();
        distX = 0;
    }
   
//----------------------------------------------------       
    if (distY == 0) {
      // do nothing
    }
    else if (distY == 1) {
     travelTo(reachedX , reachedY + 1);
     reachedY += 1;
     CompleteLightLocalization_Direction_Y();
     distY = distY - 1;
    }
    else if (distY == -1) {
      travelTo(reachedX, reachedY - 1);
      reachedY -= 1;
      CompleteLightLocalization_Direction_Y();
      distY += 1;
    }
    else if ((distY % 2 == 0) && distY > 0) {
      
      while (distY != 0) {
        travelTo( reachedX, reachedY + 2);
        reachedY += 2;
        CompleteLightLocalization_Direction_Y();
        distY -= 2;
      } //end of while loop
    }
    
    else if (!(distY % 2 == 0) && distY > 0) {
        tempdistY = distY - 1;
        
        while (tempdistY != 0) {
          travelTo(reachedX, reachedY + 2);
          reachedY += 2;
          CompleteLightLocalization_Direction_X();
          tempdistY -= 2;
        }
        
        travelTo(reachedX,reachedY + 1);
        reachedY += 1;
        CompleteLightLocalization_Direction_Y();
        distY = 0;
    }
    else if ((distY % 2 == 0) && distY < 0) {
      
      while (distY != 0) {
        travelTo( reachedX , reachedY - 2);
        reachedY = reachedY - 2;
        CompleteLightLocalization_Direction_Y();
        distY += 2;
      } //end of while loop
    }
    
    else if (!(distY % 2 == 0) && distY < 0) {
        tempdistY = distY + 1;
        
        while (tempdistY != 0) {
          travelTo(reachedX, reachedY - 2);
          reachedY -= 2;
          CompleteLightLocalization_Direction_Y();
          tempdistY += 2;
        }
        
        travelTo(reachedX, reachedY - 1);
        reachedY -= 1;
        CompleteLightLocalization_Direction_Y();
        distY = 0;
    }
}
  
} //end of Navigation class

