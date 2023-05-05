public class User {
    private int startFloor;
    private int destinationFloor;
    private int weight;

    public User(int startFloor, int destinationFloor, int weight) {
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
        this.weight = weight;
        
        if (startFloor > Building.getFloors() || startFloor < 1) {
            throw new IllegalArgumentException("Invalid start floor");
        }
        if (destinationFloor > Building.getFloors() || destinationFloor < 1) {
            throw new IllegalArgumentException("Invalid destination floor");
        }
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public Direction getDirection() {
      return startFloor < destinationFloor ? Direction.UP : Direction.DOWN;
    }


    public int getWeight() {
        return weight;
    }
}
