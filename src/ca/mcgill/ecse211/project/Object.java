package ca.mcgill.ecse211.project;

/**
 * This class is responsible for differentiating between an obstacle and a stranded vehicle.
 * It is also responsible for obstacle avoidance.
 *
 */
public class Object {

 /**
  * This method determines whether the object is an obstacle or a stranded vehicle.
  * The robot should then scan the object from left to right or right to left. As the scan is performed, the robot should be aware of the values returned by the ultrasonic sensor. 
  * If the values returned by the ultrasonic sensor between the left extreme point and right extreme point of the object are greater than a specific distance x, then the object is a stranded vehicle. 
  * The reason being that, the stranded vehicle has a lot of gaps and holes between its wheels which would result in a sudden rise in the distance returned by the ultrasonic sensor.  
  *  We must remind you that the left extreme point and the right extreme point of the object from the point of view of the robot have to be determined.
  * 
  * DetermineExtremPointsOfObject() from the Object class is called to determine the two extreme points of the object.
  */
 public static void ObjectDifferentiation() {
   
 }
  
 /**
  * From the point of view of he robot, the left extreme point and the right extreme point of the object is determined.
  */
 public static void DetermineExtremPointsOfObject() {
   
 }
  
 /**
  * If the robot is on its way back to its start zone OR on its way to the search zone and an obstacle is detected, this method is called
  * so that the robot bypasses the obstacle and continues on its path.
  */
 public static void ObstacleAvoidance() {
   
 }
 
}
