import java.util.*;

public class ElevatorScheduler {
    private Building building;
    private List<User> pendingUsers;

    public ElevatorScheduler(Building building) {
        this.building = building;
        this.pendingUsers = new ArrayList<>();
    }

    public void addUser(User user) {
        pendingUsers.add(user);
    }

    public Elevator findBestElevatorForUser(User user) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : building.getElevatorList()) {
            int distance = Math.abs(elevator.getCurrentFloor() - user.getStartFloor());
            if (elevator.hasRoomForUser(user) && (bestElevator == null || distance < minDistance)) {
                bestElevator = elevator;
                minDistance = distance;
            }
        }
        return bestElevator;
    }

    public void processUserRequests() {
        while (!pendingUsers.isEmpty() || elevatorsAreActive()) {
            List<User> servedUsers = new ArrayList<>();
            for (User user : pendingUsers) {
                Elevator bestElevator = findBestElevatorForUser(user);
                if (bestElevator != null) {
                    bestElevator.addUserToPickupQueue(user);
                    servedUsers.add(user);
                }
            }
            pendingUsers.removeAll(servedUsers);
            for (Elevator elevator : building.getElevatorList()) {
                elevator.processQueues();
            }
        }
    }

    private boolean elevatorsAreActive() {
        for (Elevator elevator : building.getElevatorList()) {
            if (!elevator.isIdle()) {
                return true;
            }
        }
        return false;
    }
}
