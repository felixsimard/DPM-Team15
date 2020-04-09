package ca.mcgill.ecse211.project;
import static ca.mcgill.ecse211.project.Resources.*;
import ca.mcgill.ecse211.playingfield.Point;
/**
 * This class is primarily responsible for the navigation of the robot to the island from its starting zone, it is also responsible
 * for the navigation of the robot to its starting zone from the island.
 *
 */
public class Navigation {
  /**
   * the x displacement of our desired destination in feet
   */
  double xDest_FT;
  /**
   * the y displacement of our desired destination in feet  
   */
  double yDest_FT;  
  /**
   * the current x displacement of the EV3 in cm
   */
  double xCurrent_CM;
  /**
   * the current y displacement of the EV3 in cm
   */
  double yCurrent_CM;
  /**
   * the current clockwise angle of the EV3 from the 0 axis, value set in travelTo(double x,double y)
   */
  double thetaCurrent_RAD; 
  /**
   * the current clockwise angle of the EV3 from the 0 axis, value set in turnTo_LightLocalizer(double angle_RAD)
   */
  static double thetaCurrent_RAD_Localizer;
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
   * The x coordinate of the starting position of the robot
   */
  double startingX;
  /**
   * The y coordinate of the starting position of the robot
   */
  double startingY;
  /**
   * the x coordinate of the gridpoint that the robot must navigate to
   */
  double destX;
  /**
   * the y coordinate of the gridpoint that the robot must navigate to
   */
  double destY;
  /**
   * the x coordinate of the current position of the robot
   */
  double reachedX;
  /**
   * the y coordinate of the current position of the robot
   */
  double reachedY;
  /**
   * the number of x coordinates the robot must travel to reach its desired destination
   */
  double distX;
  /**
   * the number of y coordinates the robot must travel to reach its desired destination
   */
  double distY;
  /**
   * the number of x coordinates the robot must travel to reach a temporary point between its starting position and its desired destination.
   */
  double tempdistX;
  /**
   * the number of y coordinates the robot must travel to reach a temporary point between its starting position and its desired destination.
   */
  double tempdistY;
  /**
   * The width of the entrance of the tunnel in cm.
   */
  double tunnelWidth;
 
  /**
   * Starting corner of the robot (0-3)
   */
  int corner;
  /**
   * x coordinate of the starting zone of the robot
   */
  int ZoneLLx;
  /**
   * y coordinate of the starting zone of the robot
   */
  int ZoneLLy;
  /**
   * x coordinate of the upper right corner of the starting zone
   */
  int ZoneURx;
  /**
   * y coordinate of the upper right corner of the starting zone
   */
  int ZoneURy;
  /**
   * x coordinate of the lower left corner of the tunnel
   */
  int TunnelLLx;
 /**
  *  y coordinate of the lower left corner of the tunnel
  */
  int TunnelLLy;
  /**
   *  x coordinate of the upper right corner of the tunnel
   */
  int TunnelURx;
  /**
   *  y coordinate of the upper right corner of the tunnel
   */
  int TunnelURy;
  
//-----------------------------------------------------------------------------------------------------------------------
 
  /**
   * This method takes as input the angle we wish to turn to, it then turns the robot to our desired angle
   * @param angle_RAD
   */
  public void turnTo(double angle_RAD) {           //has to turn by minimal angle
   
    double deltaT = angle_RAD*(180/Math.PI) -  thetaCurrent_RAD*(180/Math.PI);
    
    if (deltaT >= 0 && deltaT <= 180 ) {
      turnBy(deltaT);          
    }
    else if (deltaT > 180 ) {
      turnBy(deltaT -360 );
    }
    else if (deltaT < 0 && deltaT > -180 ) {
      turnBy(deltaT);
    }
    else if (deltaT < 0 && deltaT < -180 ) {
      turnBy(deltaT + 360);
    }
  } //end of turnTo method
  
 //--------------------------------------------------------------------------------------------------- 
  /**
   * This method also takes as input the angle we wish to turn to, it then turns the robot to our desired angle
   * The difference between this method and turnTo(double angle_RAD) is that turnTo(double angle_RAD) is called from within
   * travelTo(int x,int y), and the current angle of the robot (from the 0-degree axis) used in turnTo(double angle_RAD) is determined 
   * in travelTo(int x,int y) before turnTo(double angle_RAD) is called.
   * @param angle_RAD
   */
  public static void turnTo_LightLocalizer(double angle_RAD) {           //has to turn by minimal angle
    
    thetaCurrent_RAD_Localizer = odometer.getXyt()[2] * RADS_PER_1DEG;
    double deltaT = angle_RAD*(180/Math.PI) -  thetaCurrent_RAD_Localizer*(180/Math.PI);
    
    if (deltaT >= 0 && deltaT <= 180 ) {
      turnBy(deltaT);          
    }
    else if (deltaT > 180 ) {
      turnBy(deltaT -360 );
    }
    else if (deltaT < 0 && deltaT > -180 ) {
      turnBy(deltaT);
    }
    else if (deltaT < 0 && deltaT < -180 ) {
      turnBy(deltaT + 360);
    }
  } //end of turnTo_Localizer method
//----------------------------------------------------------------------------------------------------  
  /**
   * This method takes in the (x,y) coordinates of where we want to go, it then causes the EV3 to rotate and move to that specific
   * coordinate
   * @param x
   * @param y
   */
  public void travelTo(double x,double y) {  
        
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
          moveStraightForUsingTachoCount(distance_needed_to_cover);
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
          moveStraightForUsingTachoCount(distance_needed_to_cover);
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
          moveStraightForUsingTachoCount(distance_needed_to_cover);
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
          moveStraightForUsingTachoCount(distance_needed_to_cover);
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
        moveStraightForUsingTachoCount(Math.abs(distance_needed_to_cover));
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
      moveStraightForUsingTachoCount(Math.abs(distance_needed_to_cover));
      if (travelToNotCompleted) {
        completeTravelTo();
      }
    }
  } //end of travelTo method
//-----------------------------------------------------------------------------------------------------------------------
/**
 *  This method is used to complete navigation to a gridpoint once an obstacle is avoided.
 */
public void completeTravelTo() {
    while (obstacleAvoidanceInProgress) {
      continue;
    }
    //once obstacle avoidance is done
    travelToNotCompleted = false;
    travelTo(xDest,yDest);
  }
//-----------------------------------------------------------------------------------------------------------------------

  /**
   * Moves the robot straight for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveStraightForWithoutStopping(double distance) {
    leftMotor.setSpeed(ROTATION_SPEED);
    rightMotor.setSpeed(ROTATION_SPEED);
    leftMotor.rotate(convertDistance(distance), true);
    rightMotor.rotate(convertDistance(distance), false);
  }
//-----------------------------------------------------------------------------------------------------------------------
/**
 * Moves the robot straight for the given distance.
 * The difference between this method and moveStraightForWithoutStopping(double distance) is that in this method, the robot continuously
 * checks if an obstacle has been detected, if so, this method is exited, obstacle avoidance is done, and then the robot completes its navigation
 * procedure
 * @param distance
 */
  public static void moveStraightForUsingTachoCount(double distance) {
    
    int initialTacho = leftMotor.getTachoCount();
    int requiredTacho = convertDistance(distance);
    int currentTacho = initialTacho;
    
    while ((currentTacho - initialTacho) <  requiredTacho ) {
   
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

  /**
   * In this method, assuming that the robot has completed ultrasonic localization and is at the appropriate gridpoint -(1,1) if the starting corner was 0 - the robot
   * navigates to the entrance of the tunnel and stops.
   * 
   */
  public void NavigationToEntranceOfIslandFromStartZone( ) {
    
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
          turnBy(180);
          moveStraightForUsingTachoCount(tunnelWidth/2);
          turnBy(-90);
        }
        else {
        destX = TunnelLLx - 1;
        destY = TunnelLLy;
        distX = destX - startingX;
        distY = destY - startingY;      
        NavigateXY();       
        moveStraightForUsingTachoCount(tunnelWidth/2);
        turnBy(90);
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
          turnBy(-90);
          moveStraightForUsingTachoCount(tunnelWidth/2);
          turnBy(90);
        }
        else {
        destX = TunnelLLx;
        destY = TunnelLLy - 1;
        distX = destX - startingX;
        distY = destY - startingY;
        NavigateXY();
        turnBy(90);
        moveStraightForUsingTachoCount(tunnelWidth/2);
        turnBy(-90);
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
          turnBy(180);
          moveStraightForUsingTachoCount(tunnelWidth/2);
          turnBy(90);
        }
        else {
        destX = TunnelURx + 1;
        destY = TunnelLLy;
        distX = destX - startingX;
        distY = destY - startingY;    
        NavigateXY();      
        moveStraightForUsingTachoCount(tunnelWidth/2);
        turnBy(-90);
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
          turnBy(90);
          moveStraightForUsingTachoCount(tunnelWidth/2);
          turnBy(-90);
        }
        else {
        destX = TunnelURx;
        destY = TunnelLLy - 1;
        distX = destX - startingX;
        distY = destY - startingY;
        NavigateXY();
        turnBy(-90);
        moveStraightForUsingTachoCount(tunnelWidth/2);
        turnBy(90);
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
          moveStraightForUsingTachoCount(tunnelWidth/2);
          turnBy(-90);
        }
        else {
        destX = TunnelURx + 1;
        destY = TunnelURy;
        distX = destX - startingX;
        distY = destY - startingY;     
        NavigateXY();    
        turnBy(180);
        moveStraightForUsingTachoCount(tunnelWidth/2);
        turnBy(90);
        }
        
      } //end of horizontal tunnel
      else {
        tunnelWidth = (TunnelURx - TunnelLLx) * 30.48;
        if (TunnelURx == 15) {      //exception case
          destX = TunnelLLx;
          destY = TunnelURy + 1;
          distX = destX - startingX;
          distY = destY - startingY;
          turnBy(90);
          moveStraightForUsingTachoCount(tunnelWidth/2);
          turnBy(90);
        }
        else {
        destX = TunnelURx;
        destY = TunnelURy + 1;
        distX = destX - startingX;
        distY = destY - startingY;
        NavigateXY();
        turnBy(-90);
        moveStraightForUsingTachoCount(tunnelWidth/2);
        turnBy(-90);
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
          moveStraightForUsingTachoCount(tunnelWidth/2);
          turnBy(90);
        }
        else {
        destX = TunnelLLx - 1;          
        destY = TunnelURy;
        distX = destX - startingX;
        distY = destY - startingY;      
        NavigateXY();       
        turnBy(180);
        moveStraightForUsingTachoCount(tunnelWidth/2);
        turnBy(-90);
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
          turnBy(-90);
          moveStraightForUsingTachoCount(tunnelWidth/2);
          turnBy(-90);
        }
        else {
        destX = TunnelLLx;         
        destY = TunnelURy + 1;     
        distX = destX - startingX;     
        distY = destY - startingY;   
        NavigateXY();
        turnBy(90);
        moveStraightForUsingTachoCount(tunnelWidth/2);
        turnBy(90);
        }
      } // end of vertical tunnel
   
    } //end of if(corner == 3)

  }
  
//----------------------------------------------------------------------------------------------------
/**
 *  In this method, the robot navigates to a given gridpoint, this method is called from within the NavigationToEntranceOfIslandFromStartZone() method.
 *  Keep in mind that the robot travels in the x-direction until it reaches the x coordinate of its destination, the robot then travels in the y-direction until it reaches
 *  the y coordinate of its destination.
 */
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
//----------------------------------------------------------------------------------------------------
/**
 * In this method,when navigating in the x-direction, the robot performs light localization at appropriate gridpoints.
 * This method is called from within the NavigateXY() method.
 */
 public void CompleteLightLocalization_Direction_X() {
 LightLocalizer.Light_Localization_PartOne();
 odometer.setXyt(reachedX*TILE_SIZE_cm, startingY*TILE_SIZE_cm, 0);
 }
//---------------------------------------------------------------------------------------------------- 
 /**
  * In this method,when navigating in the y-direction, the robot performs light localization at appropriate gridpoints.
  * This method is called from within the NavigateXY() method.
  */
 public void CompleteLightLocalization_Direction_Y() {
 LightLocalizer.Light_Localization_PartOne();
 odometer.setXyt(reachedX*TILE_SIZE_cm, reachedY*TILE_SIZE_cm, 0);
 }



 /**
  * Turns the robot by a specified angle. Note that this method is different from {@code Navigation.turnTo()}. For
  * example, if the robot is facing 90 degrees, calling {@code turnBy(90)} will make the robot turn to 180 degrees, but
  * calling {@code Navigation.turnTo(90)} should do nothing (since the robot is already at 90 degrees).
  * 
  * @param angle the angle by which to turn, in degrees
  */
 public static void turnBy(double angle) {
   leftMotor.rotate(convertAngle(angle), true);
   rightMotor.rotate(-convertAngle(angle), false);
 }

 /**
  * Converts input angle to the total rotation of each wheel needed to rotate the robot by that angle.
  * 
  * @param angle the input angle
  * @return the wheel rotations necessary to rotate the robot by the angle
  */
 public static int convertAngle(double angle) {
   return convertDistance(Math.PI * BASE_WIDTH * angle / 360.0);
 }

 /**
  * Converts input distance to the total rotation of each wheel needed to cover that distance.
  * 
  * @param distance the input distance
  * @return the wheel rotations necessary to cover the distance
  */
 public static int convertDistance(double distance) {
   return (int) ((180.0 * distance) / (Math.PI * WHEEL_RADIUS));
 }

 /**
  * When this method is called, the robot determines the closest corner of the search zone from its current position (Entrance of the island).
  * The robot then determines whether the closest left gridpoint or the closest right gridpoint is near to the closest corner of the search zone.
  * The robot then navigates to the appropriate closest grid point, it then performs light localization. The robot then navigates to the centre of the corner tile of the 
  * closest corner of the search zone. Once the robot has reached the centre of the tile, it faces the centre of the search zone.
  * 
  * DetermineAppropriateCornerofSearchZone() within the Navigation class is called to determine the closest corner of the search zone from the robots current position.
  * WhichGridPointIsCloser() within the Navigation class is called to determine whether the left closest gridpoint or the right closest grid point
  * is near to the closest corner of the search zone from the current position of the robot.
  * 
  * Once the methods DetermineAppropriateCornerofSearchZone() and WhichGridPointIsCloser() are completed, NavigateXY() within the Navigation
  * class should be called to navigate to the appropriate grid point (Keep in mind that NavigateXY() need to be modified to take into account
  * cases where the robot has to navigate to a point on the playing field that is not a grid point, for example (1.5,2.2)). 
  * Light_Localization_PartOne() within the LightLocalizer class should then be called to perform light Localization. NavigateXY should then be called again to 
  * navigate to the appropriate position in the search zone. CentreOfSearchZone() should then be called to determine the angle the robot should turn to face the centre of the
  * search zone. The robot then turns to this specific angle after which it will be facing the centre of the search zone.
  * 
  * ObstacleAvoidance() method in the Object class is called if an obstacle is to be detected
  * ScanSearchZone() method in the Navigation class is then called in which the robot continues the search and rescue process.
  *
  */
 public static void NavigateToSearchZone() {
   
 }
 
/**
 * Determines the x,y coordinates of the closest corner of the search zone from the current position of the robot
 * @return the gridpoint of the closest corner of the search zone from the current position of the robot
 */
 public static Point DetermineAppropriateCornerofSearchZone() {
   Point example = new Point (1,2); // This line is added just to remove the error "This method must return a result of type Point"
   return example;
 }

 /** 
  * Determines whether the left closest gridpoint or the right closest gridpoint is near to the closest corner of the search zone from the 
  * current position of the robot
  */
 public static void WhichGridPointIsCloser() {
   
 }
/**
 * Determines the angle to turn to (from the 0-degree axis) so that the robot faces the centre of the search zone.
 * @return The angle to turn to (from the 0-degree axis) so that the robot faces the centre of the search zone
 */
 public static double CentreOfSearchZone() {
 double angle = 1.0; // This line is added just to remove the error "This method must return a result of type double"
 return angle;
 }
 
 /**
  * In this method, the robot scans the search zone from left to right OR right to left. If a single object is detected, the angle at which the 
  * object was detected is stored. The robot then approaches the object and detects what it is, if it is NOT a stranded vehicle, the robot goes to
  * the closest corner of the search zone that it has not visited yet. The robot then positions itself appropriately and scans the search zone again and repeats
  * the procedure until a stranded vehicle is detected. If two objects are detected from one corner of the search zone, the robot approaches each object one by one 
  * until it detects a stranded vehicle
  * 
  * ObjectDifferentiation() method from the Object class is called to determine whether the object is an obstacle or a stranded vehicle
  * If it is a stranded vehicle,RescueStrandedVehicle() method from the Stranded vehicle class is called in which the stranded vehicle is
  * eventually lifted.
  * 
  * Once the stranded vehicle is lifted, NavigateToStartingZone() method in the Navigation class is called making 
  * the robot navigate back to the its starting zone
  *
  */
 public static void ScanSearchZone() {
   
 }
 
 /**
  * In this method, the robot navigates back to the starting Zone from its current position on the island
  * 
  * ObstacleAvoidance() method in the Object class is called if an obstacle is to be detected
  */
 public static void NavigateToStartingZone() {
   
 }
} //end of Navigation class

