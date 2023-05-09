public class User { //Creates an individual passenger with three values
    private int startFloor;
    private int destinationFloor;
    private int weight;

    public User(int startFloor, int destinationFloor, int weight) {
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
        this.weight = weight;
    }

    public int getStartFloor() { //starting floor of user
        return startFloor;
    }

    public int getDestinationFloor() { //desintation floor
        return destinationFloor;
    }

    public Direction getDirection() { //Determins which direction the user needs to head to get to their floor
      return startFloor < destinationFloor ? Direction.UP : Direction.DOWN;
    }


    public int getWeight() { //user weight
        return weight;
    }
}
