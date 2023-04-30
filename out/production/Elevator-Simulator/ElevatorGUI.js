import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;


public class ElevatorGUI extends JFrame {
  private JTextArea outputArea;
  private JButton startButton;
  private JPanel controlPanel;
  private int floors, elevators, users, destinationFloor, startFloor;
  private Building building;
  private List<User> userList;


  public ElevatorGUI() {
    super("Elevator Simulator");


    // Create components
    outputArea = new JTextArea(20, 50);
    outputArea.setEditable(false);


    startButton = new JButton("Start Simulation");
    startButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startSimulation();
      }
    });


    controlPanel = new JPanel();
    controlPanel.add(startButton);


    // Add components to window
    Container c = getContentPane();
    c.setLayout(new BorderLayout());
    c.add(outputArea, BorderLayout.CENTER);
    c.add(controlPanel, BorderLayout.SOUTH);


    // Set window properties
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }


  private void startSimulation() {
    // Get input values from user
    floors = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of floors:"));
    elevators = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of elevators:"));
    users = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of users:"));

    // Disable the start button while the simulation is running
    startButton.setEnabled(false);

    // Start the simulation in a background thread
    new SwingWorker<Void, String>() {
      @Override
      protected Void doInBackground() throws Exception {
        // Generate users and create building object
        userList = ElevatorSimulator.generateUsers(users, floors);
        building = new Building(floors, elevators);

        // Run simulation
        boolean allUsersServiced = false;
        while (!allUsersServiced) {
          boolean hasUsersToPickup = false;

          for (Elevator elevator : building.getElevatorList()) {
            if (elevator.getDirection() == Direction.NONE && !userList.isEmpty()) {
              elevator.setDirection(ElevatorSimulator.closestUserDirection(elevator, userList));
            }

            elevator.move(userList);
            publish(ElevatorSimulator.displayFrame(elevator, userList));

            hasUsersToPickup |= elevator.hasUsersToPickup(userList);
          }

          allUsersServiced = userList.isEmpty() && !hasUsersToPickup && building.getElevatorList().stream()
              .allMatch(elevator -> elevator.getPassengers().isEmpty());
        }

        return null;
      }

      @Override
      protected void process(List<String> chunks) {
        // Append each frame to the output area
        for (String frame : chunks) {
          outputArea.append(frame);
        }
      }

      @Override
      protected void done() {
        // Re-enable the start button when the simulation is done
        startButton.setEnabled(true);
        outputArea.append("All users have been serviced.");
      }
    }.execute();
  }

  public static void main(String[] args) {
    new ElevatorGUI();
  }
}


