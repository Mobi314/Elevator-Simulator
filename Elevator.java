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
    private User assignedUser;

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

   
    public void move(List<User> users) { //Large method to determine how the elevator moves, and drops off/picks up passengers
      List<User> arrivedPassengers = new ArrayList<>(); //Creates a new list for all passengers that have arrived on the current floor
      for (User passenger : passengers) {
        if (passenger.getDestinationFloor() == currentFloor) { //For every passenger on the elevator, check if their destination floor matches the current floor
          System.out.println("A Passenger has arrived at floor " + currentFloor + " from elevator " + this.getId());
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
            System.out.println("Elevator " + this.getId() + " has picked up a passenger on floor " + currentFloor);
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

    if (direction == Direction.UP) {
      if (currentFloor == Building.getFloors() || shouldChangeDirection(users)) {
            direction = Direction.DOWN;
    } else if (currentFloor < Building.getFloors()) {
        currentFloor++;
    }
  } else if (direction == Direction.DOWN) {
    if (currentFloor == 1 || shouldChangeDirection(users)) {
      direction = Direction.UP;
    }
    if (currentFloor > 1) {
      currentFloor--;
    }

  }
    

      try {
        Thread.sleep(100); //Delay to simulate the elevator moving up and down the shaft
      } catch (InterruptedException e) {
        e.printStackTrace();
        Thread.currentThread().interrupt();
      }

    }
    
    private boolean shouldChangeDirection(List<User> users) {
    boolean hasUsersToServicingDirection = passengers.stream()
            .anyMatch(user -> user.getDirection() == direction);

    if (!hasUsersToServicingDirection) {
        boolean hasUsersWaitingInDirection = users.stream()
                .anyMatch(user -> user.getDirection() == direction);
        return !hasUsersWaitingInDirection;
    }

    return false;
}

   public boolean hasUsersToPickup(List<User> users, Queue<Integer> upQueue, Queue<Integer> downQueue) {
    for (User user : users) {
        if (user.getStartFloor() == currentFloor) {
            if (direction == Direction.UP && user.getDirection() == Direction.UP) {
                return true; // User to pick up going in the same direction (UP)
              } else if (direction == Direction.DOWN && user.getDirection() == Direction.DOWN) {
                return true; // User to pick up going in the same direction (DOWN)
              }
              else {
            }
        }
    }

    return false; // No users to pick up
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
    public Queue<Integer> getUpQueue() {
    return upQueue;
}

public Queue<Integer> getDownQueue() {
    return downQueue;
}
     public void processQueue() {
        // Check if the elevator has an assigned user
        if (assignedUser != null) {
            int destinationFloor = assignedUser.getDestinationFloor();

            if (destinationFloor > currentFloor) {
                direction = Direction.UP;
                currentFloor++;
            } else if (destinationFloor < currentFloor) {
                direction = Direction.DOWN;
                currentFloor--;
            } else {
                // Reached the assigned user's destination floor
                // Remove the user from the elevator and reset its assigned elevator
                passengers.remove(assignedUser);
                assignedUser.setAssignedElevator(null);
                assignedUser = null;
            }
        } else {
            // No assigned user, continue with regular elevator logic
            // ...
        }
    }


}

