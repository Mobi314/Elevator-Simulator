import java.io.FileInputStream;
import javazoom.jl.player.Player;
import java.util.*;

public class ElevatorSimulator {
    private final Random random;
    private final int floors;
    private final int elevators;
    private final int users;
    
    public ElevatorSimulator(int floors, int elevators, int users) {
        this.floors = floors;
        this.elevators = elevators;
        this.users = users;
        this.random = new Random();
    } 
    
    public void runSimulation() {
        Building building = new Building(floors, elevators); //Creates a new building
        List<User> userList = generateUsers(users, floors); //Creates a new list for all generated users

        boolean allUsersServiced = false; //We check if all users have been serviced in the elevator
        while (!allUsersServiced) {  
            boolean hasUsersToPickup = false;

            for (Elevator elevator : building.getElevatorList()) { //runs for each elevator in the building
                elevator.processQueues(); // Call processQueues() method for each elevator

                elevator.move(userList);
                displayFrame(elevator, userList);

                hasUsersToPickup = elevator.hasUsersToPickup(userList);
            }

            allUsersServiced = userList.isEmpty() && !hasUsersToPickup && building.getElevatorList().stream() //checks if there are no users to pickup and the elevators are empty. If they are empty, allUsersServiced becomes true
                    .allMatch(Elevator::isEmpty);
        }

        System.out.println("All users have been serviced."); //prints out all users have been serviced once it is true
    }

    private List<User> generateUsers(int users, int floors) { //method to generate random users for the elevator
        List<User> userList = new ArrayList<>(); //create a new list to hold all generated users
        double mean = 180.0;
        double stddev = 30.0; //values used for random weights via normal distribution
        for (int i = 0; i < users; i++) { //iterates through every user requested
            int startFloor = random.nextInt(floors) + 1; //gives each user a random starting floor
            int destinationFloor = random.nextInt(floors) + 1; //gives each user a random destination floor
            while (startFloor == destinationFloor) { // ensure start and destination floors are different
                destinationFloor = random.nextInt(floors) + 1;
            }
            int weight = (int) Math.round(random.nextGaussian() * stddev + mean); //uses normal distribution of 180 to find the random weights
            weight = Math.max(50, Math.min(300, weight)); //math stuff
            userList.add(new User(startFloor, destinationFloor, weight)); //adds the generated user with their values to the list
            System.out.println("Destination floor for this user: " + destinationFloor
                    + " Starting floor for this user: " + startFloor);
        }
        System.out.println("--------------------");
        return userList; //when finished with all users, returns the list
    }

    public static Direction closestUserDirection(Elevator elevator, List<User> userList) { //unused method work in progress
        int minDistance = Integer.MAX_VALUE;
        Direction closestDirection = Direction.NONE;

        for (User user : userList) {
            int distance = Math.abs(elevator.getCurrentFloor() - user.getStartFloor());
            if (distance < minDistance) {
                minDistance = distance;
                closestDirection = user.getDirection();
            }
        }

        return closestDirection;
    }

    public static void displayFrame(Elevator elevator, List<User> userList) { //display frame printing all info needed while program runs
      System.out.println("Elevator ID: " + elevator.getId());
      System.out.println("Direction: " + elevator.getDirection());
      //System.out.println("DisplayState: " + elevator.getDisplayState());
      System.out.println("CurrentFloor: " + elevator.getCurrentFloor());
      System.out.println("Users in elevator: " + elevator.getPassengerCount());
      System.out.println("Users waiting: " + userList.size());
      System.out.println("Total weight: " + elevator.getCurrentWeight());
      System.out.println("--------------------");
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

    int users = 0; //same logic as above
    while (users <= 0) {
        System.out.print("Enter the number of users (positive integer only): ");
        if (scanner.hasNextInt()) {
            users = scanner.nextInt();
            if (users <= 0) {
                System.out.println("Please enter a positive integer.");
            }
        } else {
            System.out.println("Please enter a positive integer.");
            scanner.next();
        }
    }

    ElevatorSimulator simulator = new ElevatorSimulator(floors, elevators, users); // creates a new simulator based off the values, and runs the simulation
    simulator.runSimulation();
}


}
