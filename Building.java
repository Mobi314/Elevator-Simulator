import java.util.*;

public class Building { //Creates a new building with a number of floors and elevators
    private static int floors;
    private int elevators;
    private List<Elevator> elevatorList;

    public Building(int floors, int elevators) {
        this.floors = floors;
        this.elevators = elevators;
        this.elevatorList = new ArrayList<>();
        for (int i = 0; i < elevators; i++) { //Makes a list of elevators
            elevatorList.add(new Elevator(i + 1));
        }
    }

    public static int getFloors() {
      return floors;
    }

    public int getElevators() {
        return elevators;
    }

    public List<Elevator> getElevatorList() {
        return elevatorList;
    }
}
