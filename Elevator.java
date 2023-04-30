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
  
    List<User> arrivedPassengers = new ArrayList<>();
    for (User passenger : passengers) {
      if (passenger.getDestinationFloor() == currentFloor) {
        System.out.println("A Passenger has arrived at floor " + currentFloor);
        arrivedPassengers.add(passenger);
      }
        try {
            Thread.sleep(500); // 2 second delay for each passenger picked up or dropped off
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    passengers.removeAll(arrivedPassengers);

    List<User> pickedUpUsers = new ArrayList<>();
    int newWeight = getCurrentWeight();
    for (User user : users) {
        if (user.getStartFloor() == currentFloor && user.getDirection() == direction) {
          if (newWeight + user.getWeight() <= MAX_WEIGHT) {
              System.out.println("Elevator has picked up a passenger on floor " + currentFloor);
                pickedUpUsers.add(user);
                passengers.add(user);
                newWeight += user.getWeight();
                try {
                Thread.sleep(500 * pickedUpUsers.size()); // 2 second delay for each passenger picked up or dropped off
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
          System.out.println("Elevator has reached the highest floor and is now going down.");
          direction = Direction.DOWN;
        } else if (currentFloor == minFloor) {
          System.out.println("Elevator has reached the lowest floor and is now going up.");
          direction = Direction.UP;
        }
      }
        if (direction == Direction.UP) {
        currentFloor++;
        if (currentFloor == Building.getFloors()) {
            direction = Direction.DOWN;
            for (int i = currentFloor - 1; i >= 1; i--) {
                for (User user : users) {
                    if (user.getStartFloor() == i && user.getDestinationFloor() < i) {
                        return;
                    }
                }
            }
        }
    } else if (direction == Direction.DOWN) {
        currentFloor--;
        if (currentFloor == 1) {
            direction = Direction.UP;
            for (int i = currentFloor + 1; i <= Building.getFloors(); i++) {
                for (User user : users) {
                    if (user.getStartFloor() == i && user.getDestinationFloor() > i) {
                        return;
                    }
                }
            }
        }
    }

    



      try {
        Thread.sleep(200);
    } catch (InterruptedException e) {
        e.printStackTrace();
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

