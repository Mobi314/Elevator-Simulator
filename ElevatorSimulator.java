import java.io.FileInputStream;
import javazoom.jl.player.Player;
import java.util.*;


public class ElevatorSimulator {
    private final Random random;
    private final int floors;
    private final int elevators;
    public List<User> userList;
    
    
    public ElevatorSimulator(int floors, int elevators) {
      this.floors = floors;
      this.elevators = elevators;
      this.random = new Random();
      userList = new ArrayList<>();

    }
    
   
    public void runSimulation() {
      
      Building building = new Building(floors, elevators); //Creates a new building
      generateRandomUser();
      generateRandomUser();
      generateRandomUser();
      generateRandomUser();
      generateRandomUser(); //Generates 5 initial users within the building

      boolean allUsersServiced = false; //We check if all users have been serviced in the elevator
      while (!allUsersServiced) {
        List<String> elevatorStatusList = new ArrayList<>(); //Creates a List to store the status of each elevator
        for (Elevator elevator : building.getElevatorList()) { //For every elevator in the building, store the current status
          String elevatorStatus = "Elevator " + elevator.getId() + ": ";
          elevatorStatus += "Direction: " + elevator.getDirection() + " | ";
          elevatorStatus += "Current Floor: " + elevator.getCurrentFloor() + " | ";
          elevatorStatus += "Passengers waiting: " + userList.size() + " | ";
          elevatorStatus += "Passengers in elevator: " + elevator.getPassengerCount() + " | ";
          elevatorStatus += "Total weight: " + elevator.getCurrentWeight() + " | ";
          elevatorStatusList.add(elevatorStatus);
    }

    // Print elevator status side by side
    for (String elevatorStatus : elevatorStatusList) {
        System.out.println(elevatorStatus);
    }
    System.out.println("-----------------------------------------------------------------------------"); //seperator for aesthetics
          
        int randomNumber = random.nextInt(10); //Creates a random integer from 1-10. For every tick of the program, if the number is 7 or higher, generate another random user
          if (randomNumber > 6) {
            generateRandomUser();
          }

        for (Elevator elevator : building.getElevatorList()) { //runs for each elevator in the building
          elevator.move(userList); //moves the elevator
        }

          if (userList.isEmpty() && building.getElevatorList().stream().allMatch(Elevator::isEmpty)) { //If there are no users left, exit the loop
             break;
      }
        } 

      System.out.println("All users have been serviced."); //prints out all users have been serviced once it is true
    }

    private void generateRandomUser() {
      double mean = 180.0;
      double stddev = 30.0; //values used for random weights via normal distribution
      int startFloor = random.nextInt(floors) + 1; //gives each user a random starting floor
      int destinationFloor = random.nextInt(floors) + 1; //gives each user a random destination floor
      while (startFloor == destinationFloor) { // ensure start and destination floors are different
        destinationFloor = random.nextInt(floors) + 1;
      }
      int weight = (int) Math.round(random.nextGaussian() * stddev + mean); //uses normal distribution of 180 to find the random weights
      weight = Math.max(50, Math.min(300, weight)); //math stuff
      User user = new User(startFloor, destinationFloor, weight); //adds the generated user with their values to the list
      Elevator assignedElevator = getAssignedElevator(user); 
      user.setAssignedElevator(assignedElevator); //Assigns an elevator to service the user
      // Add the user to the assigned elevator's passengers list
      assignedElevator.getPassengers().add(user);
      System.out.println("A new user has called the elevator!");
      System.out.println("Starting floor for this user: " + startFloor
          + " Destination floor for this user: " + destinationFloor);
      System.out.println("-----------------------------------------------------------------------------");
      userList.add(user); //adds the newly generated user to userList
     // System.out.println("Current users in list: " + userList); //Commented out line, used for testing purposes

    }

 public Elevator getElevator(int elevatorId) {
    return new Elevator(elevatorId); // Create a new Elevator object with the specified ID
}

private Elevator getAssignedElevator(User user) {
    int minDistance = Integer.MAX_VALUE;
    Elevator closestElevator = null; //variables to help determine the best elevator for a user
    
     for (int i = 1; i <= elevators; i++) {
        Elevator elevator = getElevator(i);
        if (elevator.getDirection() == user.getDirection() && elevator.getPassengers().isEmpty()) {
            return elevator; // Assign to the elevator moving in the same direction
        }
    }

    for (int i = 1; i <= elevators; i++) {
      Elevator elevator = getElevator(i);
      int distance = Math.abs(elevator.getCurrentFloor() - user.getStartFloor()); //for each elevator, determine distance from next user

      if (distance < minDistance && elevator.getPassengers().isEmpty()) {
        minDistance = distance;
        closestElevator = elevator; //assigns this elevator as the closest elevator
      }
    }
    
    if (closestElevator != null) {
        return closestElevator; // Assign to the closest elevator
    }
    
    return getElevator(1);
     
}




    
public static void main(String[] args) {
     new Thread(() -> {
            try {
                FileInputStream fileInputStream = new FileInputStream("music/flaing-piano-loop-8782 (mp3cut.net).mp3");
                Player player = new Player(fileInputStream);
                player.play();
            } catch (Exception e) {
                System.out.println(e);
            }
        }).start(); 
    Scanner scanner = new Scanner(System.in); // creates a scanner for input

    int floors = 0;
    while (floors <= 0) { //if a positive integer is entered, it moves on
      System.out.print("Enter the number of floors (positive integer only): ");
      if (scanner.hasNextInt()) {
        floors = scanner.nextInt();
        if (floors <= 0) {
          System.out.println("Please enter a positive integer."); //checks if integer entered is negative
        }
      } else {
        System.out.println("Please enter a positive integer."); //checks everything else
        scanner.next();
      }
    }
   
    int elevators = 0; //same logic as above
    while (elevators <= 0) {
      System.out.print("Enter the number of elevators (positive integer only): ");
      if (scanner.hasNextInt()) {
        elevators = scanner.nextInt();
        if (elevators <= 0) {
          System.out.println("Please enter a positive integer.");
        }
      } else {
        System.out.println("Please enter a positive integer.");
        scanner.next();
      }
    }
    System.out.println(); //Prints a space for aesthetic purposes

    ElevatorSimulator simulator = new ElevatorSimulator(floors, elevators); // creates a new simulator based off the values, and runs the simulation
    simulator.runSimulation();
}



}
