import java.util.*;

public class ElevatorSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of floors: ");
        int floors = scanner.nextInt();

        System.out.print("Enter the number of elevators: ");
        int elevators = scanner.nextInt();

        System.out.print("Enter the number of users: ");
        int users = scanner.nextInt();

        Building building = new Building(floors, elevators);
        List<User> userList = generateUsers(users, floors);

        while (!userList.isEmpty()) {
            for (Elevator elevator : building.getElevatorList()) {
                if (elevator.getDirection() == Direction.NONE && !userList.isEmpty()) {
                    elevator.setDirection(closestUserDirection(elevator, userList));
                }

                elevator.move(userList);
                displayFrame(elevator, userList);
            }
        }

        System.out.println("All users have been serviced.");
    }

    public static List<User> generateUsers(int users, int floors) {
        List<User> userList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < users; i++) {
            int startFloor = random.nextInt(floors) + 1;
            int destinationFloor;
            do {
                destinationFloor = random.nextInt(floors) + 1;
            } while (destinationFloor == startFloor);
            userList.add(new User(startFloor, destinationFloor));
        }
        return userList;
    }

    public static Direction closestUserDirection(Elevator elevator, List<User> userList) {
        int minDistance = Integer.MAX_VALUE;
        Direction closestDirection = Direction.NONE;

        for (User user : userList) {
            int distance = Math.abs(elevator.getCurrentFloor() - user.getStartFloor());
            if (distance < minDistance) {
                minDistance = distance;
                closestDirection = user.getDirection();
            }
        }

        return closestDirection;
    }

    public static void displayFrame(Elevator elevator, List<User> userList) {
        System.out.println("Elevator ID: " + elevator.getId());
        System.out.println("Direction: " + elevator.getDirection());
        System.out.println("DisplayState: " + elevator.getDisplayState());
        System.out.println("CurrentFloor: " + elevator.getCurrentFloor());
        System.out.println("Users in elevator: " + elevator.getPassengerCount());
        System.out.println("Users waiting: " + userList.size());
        System.out.println("--------------------");
    }
}
