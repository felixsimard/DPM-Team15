package ca.mcgill.ecse211.project;

/**
 * 
 * Class to hold all the methods required for identify an approaching obstacle, 
 * identify if that object is an actual obstacle, another EV3 robot, or the stranded vehicle.
 * 
 * Also, holds all methods to actually avoid an obstacle.
 * 
 */
public class Obstacle {

  // for obstacle avoidance and obstacle identification

  /**
   * Returns true when robot approaches an object/obstacle.
   * 
   * @param distance    the distance value read by the US sensor
   * @return      True if object approaching
   */
  public boolean hasDetectedObstacle(int distance) {
    return true;
  }
  
  /**
   * Method to actually avoid an obstacle.
   * Basic logic implemented to avoid based on stopping and sharp turn right.
   */
  public void avoidObstacle() {
    
  }
  
  /**
   * Method to identify/differentiate the object/obstacle facing the robot.
   * 
   * @param readings   an array of integers recorded a US sensor left-right sweep of the obstacle
   * @return       a string representation of the identified obstacle.
   */
  public String identifyObstacle(int[] readings) {
    return "stranded_vehicle";
  }
  
  
  // more methods t be added...
  

}
