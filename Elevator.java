import java.util.*;

public class Elevator {
    private int id;
    private Direction direction;
    private DisplayState displayState;
    private int currentFloor;
    private List<User> passengers;
    private int currentWeight;
    private int MAX_WEIGHT = 3000;// added variable
    private boolean isIdle;
    private boolean isMovingUp;
    private List<User> pickupQueue;
    private Queue<Integer> upQueue;
    private Queue<Integer> downQueue;
    private boolean idle;
    private boolean movingUp;

    public Elevator(int id) {
      this.id = id;
      this.direction = Direction.UP;
      this.displayState = DisplayState.IDLE;
      this.currentFloor = 1;
      this.passengers = new ArrayList<>();
      this.currentWeight = 0;
      this.isIdle = true;
      this.isMovingUp = false;
      this.pickupQueue = new LinkedList<>();
      this.upQueue = new LinkedList<>();
      this.downQueue = new LinkedList<>();
    }
    
      public boolean hasExceededWeightLimit(User user) { //checks if the next user entering the elevator has exceeded the maximum weight capacity of 3000 on the elevator
        return currentWeight + user.getWeight() > 3000;
    }

    public boolean hasRoomForUser(User user) { //confirms the elevator has room for the user if he does not exceed the weigfht limit
      return !hasExceededWeightLimit(user);
    }
    public int getCurrentWeight() { //gets the current weight of all users in the elevator
    int currentWeight = 0;
    for (User passenger : passengers) {
        currentWeight += passenger.getWeight();
    }
    return currentWeight;
}

    // Getters and setters remain the same

    // If elevator has no passengers, it should remain idle
    // Move elevator towards the destination floor of the passengers
    public void move(List<User> users) { //Large method to determine how the elevator moves, and drops off/picks up passengers

        List<User> arrivedPassengers = new ArrayList<>(); //Creates a new list for all passengers that have arrived on the current floor
        for (User passenger : passengers) {
            if (passenger.getDestinationFloor() == currentFloor) { //For every passenger on the elevator, check if their destination floor matches the current floor
                System.out.println("A Passenger has arrived at floor " + currentFloor);
                arrivedPassengers.add(passenger); //If they match, add them to the list
            }
            try {
                Thread.sleep(100); //  delay for each passenger picked up or dropped off
            } catch (InterruptedException e) {
              e.printStackTrace();
              Thread.currentThread().interrupt();
            }
        }
        passengers.removeAll(arrivedPassengers); //After we determine all arrived passengers, remove them from the elevator

        List<User> pickedUpUsers = new ArrayList<>(); //Same concept as arrived passengers for picked up passengers
        int newWeight = getCurrentWeight(); //Creates a new weight variable to determine new weight of the elevator as each passenger gets on
        for (User user : users) {
            if (user.getStartFloor() == currentFloor && user.getDirection() == direction) { //Checks if a user has the same start floor and is going in the same direction as the elevator
                if (newWeight + user.getWeight() <= MAX_WEIGHT) { //Verifies the user does not go over the maximum weight in the elevator
                    System.out.println("Elevator has picked up a passenger on floor " + currentFloor);
                    pickedUpUsers.add(user);
                    passengers.add(user);
                    newWeight += user.getWeight(); //Picks up the passenger and adds him to the total weight of the elevator
                    try {
                        Thread.sleep(100 * pickedUpUsers.size()); // delay for each passenger picked up or dropped off
                    } catch (InterruptedException e) {
                      e.printStackTrace();
                      Thread.currentThread().interrupt();
                    }
                }
            }
        }
        users.removeAll(pickedUpUsers); //After we have determined all picked up passengers, remove them from the list.

        if (passengers.isEmpty()) { //Checks if the elevator has no passengers
          if (direction == Direction.NONE && !users.isEmpty()) {
            // Find the closest user to the elevator
            User closestUser = users.get(0);
            int minDistance = Math.abs(currentFloor - closestUser.getStartFloor()); //Math stuff, not sure if working properly
            for (User user : users) {
              int distance = Math.abs(currentFloor - user.getStartFloor());
              if (distance < minDistance) {
                minDistance = distance;
                closestUser = user;
              }
            }
            direction = currentFloor < closestUser.getStartFloor() ? Direction.UP : Direction.DOWN;
          }
        }

        if (direction == Direction.UP) { //Elevator starts at floor 1 with the direction going up
        if (currentFloor == Building.getFloors()) {
          direction = Direction.DOWN; //If elevator is at the top floor, its direction flips to DOWN
        }
        else if (currentFloor < Building.getFloors()) { // Prevent elevator from going above the highest floor
                currentFloor++; 
            } 
          } else if (direction == Direction.DOWN) {
            if (currentFloor == 1) {
            direction = Direction.UP; //If the elevator is going down and reaches the first floor, it now starts going up
          }
            if (currentFloor > 1) { // Prevent elevator from going below floor 1
                currentFloor--; //If the elevator is headed down above floor 1, it goes down 1 floors
            }
        }

        try {
            Thread.sleep(100); //Delay to simulate the elevator moving up and down the shaft
        } catch (InterruptedException e) {
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }
       
    }

    public boolean hasUsersToPickup(List<User> users) {
      for (User user : users) {
        if (user.getDirection() == direction) {
          return true;
        }
      }
      return false;
    }

    public boolean isEmpty() {
      return this.getPassengerCount() == 0;
    }

    public void addUserToPickupQueue(User user) {
        pickupQueue.add(user);
    }
    

    public int getId() {
        return id;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public DisplayState getDisplayState() {
        return displayState;
    }

    public void setDisplayState(DisplayState displayState) {
        this.displayState = displayState;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public void addPassenger(User passenger) {
        passengers.add(passenger);
    }

    public void removePassenger(User passenger) {
        passengers.remove(passenger);
    }

    public int getPassengerCount() {
        return passengers.size();
    }

    public void setIdle(boolean isIdle) { this.isIdle = isIdle; }

    public boolean isMovingUp() { return isMovingUp; }

    public void setMovingUp(boolean isMovingUp) { this.isMovingUp = isMovingUp; }
    public boolean isIdle() { return isIdle; }
    public void addToUpQueue(int floor) { upQueue.add(floor); }
    public void addToDownQueue(int floor) { downQueue.add(floor); }
    public void moveToFloor(int floor) { currentFloor = floor;}
    public void processQueues() {
      if (upQueue.isEmpty() && downQueue.isEmpty()) {
        idle = true;
        return;
       }
    
    if (idle) {
        int nextFloor = (upQueue.isEmpty()) ? downQueue.poll() : upQueue.poll();
        movingUp = (nextFloor > currentFloor);
        idle = false;
        moveToFloor(nextFloor);
    } else if (movingUp && !upQueue.isEmpty()) {
        int nextFloor = upQueue.poll();
        moveToFloor(nextFloor);
    } else if (!movingUp && !downQueue.isEmpty()) {
        int nextFloor = downQueue.poll();
        moveToFloor(nextFloor);
    } else {
        movingUp = !movingUp;
    }
}
}

