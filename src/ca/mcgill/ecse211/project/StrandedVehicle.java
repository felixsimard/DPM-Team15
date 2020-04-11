package ca.mcgill.ecse211.project;

/**
 * This class is responsible for detecting the vertical pin of the stranded vehicle and eventually lifting the stranded vehicle
 *
 */
public class StrandedVehicle {

  /**
   * After this method is completed, the stranded vehicle has been lifted and the robot must then navigate back to its starting zone
   * In this method, FindVerticalPin() from the StrandedVehicle class is called to find and identify the vertical pin on the stranded vehicle.
   * GrabVerticalPin() from the StrandedVehicle class is then called to grab the vertical pin and perform a reverse maneuver.
   * LiftStrandedVehicle() from the Stranded Vehicle class is then called to connect to and lift the stranded vehicle
   */
  public static void RescueStrandedVehicle() {
    
  }
  
  /**
   * In this method, the robot attempts to find the vertical pin of the stranded vehicle via colour detection. 
   * The assumption is that the colour of the vertical pin is known and that we have the RGB values of the colour we are looking for.
   * The robot should travel around the stranded vehicle and find the vertical pin by using its colour sensor. 
   * The robot should move a fixed distance along the stranded vehicle, turn towards the vehicle, 
   * scan the vehicle from left to right or right to left in order to find and identify the vertical pin. 
   * If the vertical pin is not identified, the robot should again move a fixed distance 
   * along the stranded vehicle and repeat the scan procedure.
   */
  public static void FindVerticalPin() {
    
  }
  /**
   * In this method the robot would hook onto the vertical pin of the stranded vehicle and perform a reverse
   * maneuver such that regardless of the position from which the robot hooked onto the vertical pin, 
   * once the robot reverses, the stranded vehicle straightens and is aligned with the
   * robot. The robot will then move forward for a fixed distance letting go of the vertical pin.
   * 
   * Keep in mind that the grabbing mechanism is just a hook. This hook is directly below the colour sensor, not only that,
   * this hook is shorter in length than the colour sensor. So once the vertical pin is detected, the robot needs to turn 
   * to the left for a fixed distance, move forward for a fixed distance, then turn to the right for a fixed distance so that
   * the robot is once again facing the vertical pin. The robot will then perform a reverse maneuver during which the robot hooks
   * onto the vertical pin. Due to this reverse maneuver the stranded vehicle will straighten and be aligned with the robot.
   *
   */
  public static void GrabVerticalPin() {
    
  }
  
  /**
   * Once the stranded vehicle has been straightened, The robot performs a 180-degree anticlockwise turn.
   * The robot then reverses for a fixed distance such that the lifting components of the robot connect 
   * to the stranded vehicle. The robot then lifts the stranded vehicle (Using the motor attached to the lifting mechanism).
   */
  public static void LiftStrandedVehicle() {
    
  }
}
