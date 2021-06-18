package Main;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class ControlPanel {
    private Map map;

    private JButton toggleRunBtn;
    private JComboBox<String> algo;

    public ControlPanel(Map map) {
        this.map = map;

        algo = new JComboBox<>(new String[] { "Select an algorithm", "A*", "Dijkstra", "Breadth-first search"});
        algo.setVisible(true);

        toggleRunBtn = new JButton("Run");
        toggleRunBtn.setVisible(true);
        toggleRunBtn.setMargin(new Insets(0, 0, 0, 0));
        toggleRunBtn.setBackground(Color.white);
        toggleRunBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (toggleRunBtn.getText().equals("Run")) {
                    switch (algo.getItemAt(algo.getSelectedIndex())) {

                    case ("A*"):
                        System.out.println("A* selected");
                        // AStar.start();
                        break;

                    case ("Dijkstra"):
                        System.out.println("Dijkstra selected");
                        // AStar.start();
                        break;

                    case ("Greedy best-first search"):
                        System.out.println("Greedy best-first search selected");
                        // AStar.start();
                        break;

                    case ("Breadth-first search"):
                        System.out.println("Breadth-first search selected");
                        // AStar.start();
                        break;

                    default:
                        JOptionPane.showMessageDialog(null,
                                "You must select an algorithm before starting the pathfinder", "Algorithm not selected",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    toggleRunBtn.setText("Stop");
                }

                else {
                    toggleRunBtn.setText("Run");
                }
            }
        });

        map.add(algo);
        map.add(toggleRunBtn);
    }

    public void renderMenu() {
        algo.setBounds(10,10, algo.getWidth(), algo.getHeight());
        toggleRunBtn.setBounds(220, 10, 48, 24);
    }
}
