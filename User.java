public class User {
    private int startFloor;
    private int destinationFloor;

    public User(int startFloor, int destinationFloor) {
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
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
}
