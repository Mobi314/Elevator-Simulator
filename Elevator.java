import java.util.*;

public class Elevator {
    private int id;
    private Direction direction;
    private DisplayState displayState;
    private int currentFloor;
    private List<User> passengers;
    private int DELAY_TIME_PER_FLOOR = 200;
    private int currentWeight;
    private int MAX_WEIGHT = 3000;// added variable


    public Elevator(int id) {
      this.id = id;
      this.direction = Direction.NONE;
      this.displayState = DisplayState.IDLE;
      this.currentFloor = 1;
      this.passengers = new ArrayList<>();
      this.currentWeight = 0;
    }
    
      public boolean hasExceededWeightLimit(User user) {
        return currentWeight + user.getWeight() > 3000;
    }

    public boolean hasRoomForUser(User user) {
      return !hasExceededWeightLimit(user);
    }
    public int getCurrentWeight() {
    int currentWeight = 0;
    for (User passenger : passengers) {
        currentWeight += passenger.getWeight();
    }
    return currentWeight;
}

    // Getters and setters remain the same

    // If elevator has no passengers, it should remain idle
    // Move elevator towards the destination floor of the passengers
    public void move(List<User> users) {
    if (direction == Direction.UP) {
        currentFloor++;
        if (currentFloor == Building.getFloors()) {
            direction = Direction.DOWN;
        }
    } else if (direction == Direction.DOWN) {
        currentFloor--;
        if (currentFloor == 1) {
            direction = Direction.UP;
        }
    }

    List<User> arrivedPassengers = new ArrayList<>();
    for (User passenger : passengers) {
        if (passenger.getDestinationFloor() == currentFloor) {
            arrivedPassengers.add(passenger);
        }
    }
    passengers.removeAll(arrivedPassengers);

    List<User> pickedUpUsers = new ArrayList<>();
    for (User user : users) {
        if (user.getStartFloor() == currentFloor && user.getDirection() == direction) {
            int newWeight = currentWeight + user.getWeight();
            if (newWeight <= MAX_WEIGHT) {
                pickedUpUsers.add(user);
                passengers.add(user);
                currentWeight = newWeight;
            }
        }
    }
    users.removeAll(pickedUpUsers);

    if (passengers.isEmpty()) {
        if (direction == Direction.NONE && !users.isEmpty()) {
            direction = users.get(0).getDirection();
        }
      } else {
        int maxFloor = passengers.stream().mapToInt(User::getDestinationFloor).max().getAsInt();
        int minFloor = passengers.stream().mapToInt(User::getDestinationFloor).min().getAsInt();
        if (currentFloor == maxFloor) {
          direction = Direction.DOWN;
        } else if (currentFloor == minFloor) {
          direction = Direction.UP;
        }
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

}


