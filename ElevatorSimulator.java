import java.io.FileInputStream;
import javazoom.jl.player.Player;
import java.util.*;


public class ElevatorSimulator {
    private final Random random;
    private final int floors;
    private final int elevators;
    private List<User> userList;
    private boolean fireServiceModeActive;
    
    public ElevatorSimulator(int floors, int elevators) {
        this.floors = floors;
        this.elevators = elevators;
        this.random = new Random();
        userList = new ArrayList<>();
        fireServiceModeActive = false;


    }

    public void runSimulation() {
      
      Building building = new Building(floors, elevators);
      generateRandomUser();
      generateRandomUser();
      generateRandomUser();
      generateRandomUser();
      generateRandomUser();

      boolean allUsersServiced = false; //We check if all users have been serviced in the elevator
      while (!allUsersServiced) {
        List<String> elevatorStatusList = new ArrayList<>();
        for (Elevator elevator : building.getElevatorList()) {
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
    System.out.println("-----------------------------------------------------------------------------");
          
        int randomNumber = random.nextInt(10);
          if (randomNumber > 5) {
            generateRandomUser();
          }

        for (Elevator elevator : building.getElevatorList()) { //runs for each elevator in the building
          elevator.processQueue(); // Call processQueues() method for each elevator

      
         // displayFrame(elevator, userList);
          elevator.move(userList);

        }

          if (userList.isEmpty() && building.getElevatorList().stream().allMatch(Elevator::isEmpty)) {
             break;
      }
      if (!fireServiceModeActive && fireServiceModeActivates()) { //checks for fire service mode and breaks from the loop
        activateFireServiceMode(building);
        break;
    }
        }
        if (userList.isEmpty() && building.getElevatorList().stream().allMatch(Elevator::isEmpty) && !fireServiceModeActive) {
            System.out.println("All users have been serviced.");
        }
    }

    private boolean fireServiceModeActivates() {
        if (fireServiceModeActive) {
            return false;
        }
        double probability = 0.01; // 1% chance for activation
        return random.nextDouble() < probability;
    }

    private void activateFireServiceMode(Building building) {
        fireServiceModeActive = true;
        for (Elevator elevator : building.getElevatorList()) { //this will move all the elevators to floor 1
            elevator.moveToFloor(1);
        }
        System.out.println("Fire service mode activated. All elevators will now move to the first floor.");
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
      user.setAssignedElevator(assignedElevator);
      // Add the user to the assigned elevator's passengers list
      assignedElevator.getPassengers().add(user);
      System.out.println("A new user has called the elevator!");
      System.out.println("Starting floor for this user: " + startFloor
          + " Destination floor for this user: " + destinationFloor);
      System.out.println("-----------------------------------------------------------------------------");
      userList.add(user); //when finished with all users, returns the list
     // System.out.println("Current users in list: " + userList);

    }

   private Elevator getElevator(int elevatorId) {
    return new Elevator(elevatorId); // Create a new Elevator object with the specified ID
}

private Elevator getAssignedElevator(User user) {
    int minDistance = Integer.MAX_VALUE;
    Elevator closestElevator = null;
    
    for (int i = 1; i <= elevators; i++) {
        Elevator elevator = getElevator(i);
        int distance = Math.abs(elevator.getCurrentFloor() - user.getStartFloor());
        
        if (distance < minDistance && elevator.getPassengers().isEmpty()) {
            minDistance = distance;
            closestElevator = elevator;
        }
    }
    
    return closestElevator;
}
public boolean isFireServiceModeActive() {
    return fireServiceModeActive;
}




public static void main(String[] args) {
    new Thread(() -> {
        try {
            FileInputStream fileInputStream = new FileInputStream("music/glass-of-wine-143532.mp3");
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
    System.out.println();

   /*  int users = 0; //same logic as above
    while (users <= 0) {
        System.out.print("Enter the number of initial users (positive integer only): ");
        if (scanner.hasNextInt()) {
            users = scanner.nextInt();
            if (users <= 0) {
                System.out.println("Please enter a positive integer.");
            }
        } else {
            System.out.println("Please enter a positive integer.");
            scanner.next();
        }
    } */

    ElevatorSimulator simulator = new ElevatorSimulator(floors, elevators); // creates a new simulator based off the values, and runs the simulation
    simulator.runSimulation();
    System.exit(0);
}


}
