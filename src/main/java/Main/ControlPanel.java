package Main;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;

public class ControlPanel {
    static JButton toggleRunBtn;
    static JComboBox<String> algo;

    public ControlPanel(Map map) {
        algo = new JComboBox<>(new String[] { "Select an algorithm", "A*", "Dijkstra", "Breadth-first search"});
        algo.setVisible(true);

        toggleRunBtn = new JButton("Run");
        toggleRunBtn.setVisible(true);
        toggleRunBtn.setMargin(new Insets(0, 0, 0, 0));
        toggleRunBtn.setBackground(Color.white);
        toggleRunBtn.addActionListener(map);

        map.add(algo);
        map.add(toggleRunBtn);
    }

    public void renderMenu() {
        algo.setBounds(10,10, algo.getWidth(), algo.getHeight());
        toggleRunBtn.setBounds(220, 10, 48, 24);
    }
}
