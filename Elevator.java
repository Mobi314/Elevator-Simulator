import java.util.*;

public class Elevator {
    private int id;
    private Direction direction;
    private DisplayState displayState;
    private int currentFloor;
    private List<User> passengers;

    public Elevator(int id) {
        this.id = id;
        this.direction = Direction.NONE;
        this.displayState = DisplayState.IDLE;
        this.currentFloor = 1;
        this.passengers = new ArrayList<>();
    }

    // Getters and setters remain the same

    public void move(List<User> users) {
        if (direction == Direction.UP) {
            currentFloor++;
        } else if (direction == Direction.DOWN) {
            currentFloor--;
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
                pickedUpUsers.add(user);
                passengers.add(user);
            }
        }

        users.removeAll(pickedUpUsers);

        if (passengers.isEmpty()) {
            direction = Direction.NONE;
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
