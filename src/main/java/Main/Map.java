/* TODO
 * aggiungere una sorta di menu dove scegliere:
 * - velocità di riproduzione
 * - tipo di algoritmo:
 *    + a*
 *    + dijkstra
 *    + breadth-first search
 * zoommare sulla griglia
 */
package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class Map extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    private JFrame window;
    private char key = (char) 0;
    private ControlPanel menu = new ControlPanel(this);

    static int width;
    static int height;
    boolean isFinished = false;

    int size = 20;

    public Map() {
        this.setBackground(new Color(40, 40, 40));

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        addMouseMotionListener(this);

        // Setting up the window
        window = new JFrame();
        window.setContentPane(this);
        window.setTitle("Pathfinding Algorithm Visualizer");
        window.getContentPane().setPreferredSize(new Dimension(1920, 1080));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);

        width = this.getWidth();
        height = this.getHeight();

        this.revalidate();
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws the grid
        g.setColor(new Color(50, 48, 47));
        for (int i = 0; i < this.getWidth(); i += size) {
            for (int j = 0; j < this.getHeight(); j += size) {
                g.drawRect(i, j, size, size);
            }
        }

        // Draws start node
        if (PathfinderUtils.startNode != null) {
            g.setColor(new Color(117, 110, 202));
            g.fillRect(PathfinderUtils.startNode.getX() + 1, PathfinderUtils.startNode.getY() + 1, size - 1, size - 1);
        }

        // Draws end node
        if (PathfinderUtils.endNode != null) {
            g.setColor(new Color(204, 36, 29));
            g.fillRect(PathfinderUtils.endNode.getX() + 1, PathfinderUtils.endNode.getY() + 1, size - 1, size - 1);
        }

        // Draws barrier nodes
        g.setColor(Color.white);
        for (Node node : PathfinderUtils.barriers) {
            g.fillRect(node.getX() + 1, node.getY() + 1, size - 1, size - 1);
        }

        menu.renderMenu();

        // Draws open nodes
        g.setColor(new Color(69, 133, 136));
        for (Node node : PathfinderUtils.openNodes) {
            g.fillRect(node.getX() + 1, node.getY() + 1, size - 1, size - 1);
        }

        // Draws closed nodes
        g.setColor(new Color(77, 77, 77));
        for (Node node : PathfinderUtils.closedNodes) {
            g.fillRect(node.getX() + 1, node.getY() + 1, size - 1, size - 1);
        }

        // if path is found start drawing the shortest path
        if (isFinished) {
            g.setColor(new Color(250, 189, 47));
            for (Node node : PathfinderUtils.path) {
                g.fillRect(node.getX() + 1, node.getY() + 1, size - 1, size - 1);
            }
        }
    }

    // Drawing on the grid
    public void mapDrawing(MouseEvent e) {
        // Creating nodes
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (key == 's') {
                int posX = e.getX() - (e.getX() % size);
                int posY = e.getY() - (e.getY() % size);

                // Checks if start node and end node are the same
                if (PathfinderUtils.startNode != null && PathfinderUtils.endNode != null) {
                    if (PathfinderUtils.isSameNode(PathfinderUtils.startNode, PathfinderUtils.endNode)) {
                        JOptionPane.showMessageDialog(null, "End node and start node can't be the same node",
                                "Same node error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (PathfinderUtils.startNode == null) {
                    PathfinderUtils.startNode = new Node(posX, posY);
                } else {
                    PathfinderUtils.startNode.setX(posX);
                    PathfinderUtils.startNode.setY(posY);
                }

                repaint();
            }

            else if (key == 'e') {
                int posX = e.getX() - (e.getX() % size);
                int posY = e.getY() - (e.getY() % size);

                // Checks if end node and start node are the same
                if (PathfinderUtils.startNode != null && PathfinderUtils.endNode != null) {
                    if (PathfinderUtils.isSameNode(PathfinderUtils.startNode, PathfinderUtils.endNode)) {
                        JOptionPane.showMessageDialog(null, "End node and start node can't be the same node!",
                                "SAME NODE ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (PathfinderUtils.endNode == null) {
                    PathfinderUtils.endNode = new Node(posX, posY);
                } else {
                    PathfinderUtils.endNode.setX(posX);
                    PathfinderUtils.endNode.setY(posY);
                }

                repaint();
            }

            else {
                int posX = e.getX() - (e.getX() % size);
                int posY = e.getY() - (e.getY() % size);
                Node barrierNode = new Node(posX, posY);

                if (PathfinderUtils.startNode != null)
                    if (PathfinderUtils.isSameNode(barrierNode, PathfinderUtils.startNode))
                        return;
                if (PathfinderUtils.endNode != null)
                    if (PathfinderUtils.isSameNode(barrierNode, PathfinderUtils.endNode))
                        return;

                PathfinderUtils.barriers.add(barrierNode);

                repaint();
            }
        }

        // Deleting nodes
        else if (SwingUtilities.isRightMouseButton(e)) {
            int posX = e.getX() - (e.getX() % size);
            int posY = e.getY() - (e.getY() % size);

            if (key == 's' && PathfinderUtils.startNode != null) {
                if (PathfinderUtils.startNode.getX() == posX && PathfinderUtils.startNode.getY() == posY) {
                    PathfinderUtils.startNode = null;
                    repaint();
                }
            }

            else if (key == 'e' && PathfinderUtils.endNode != null) {
                if (PathfinderUtils.endNode.getX() == posX && PathfinderUtils.endNode.getY() == posY) {
                    PathfinderUtils.endNode = null;
                    repaint();
                }
            }

            else {
                int nodeID = PathfinderUtils.locateBarrier(posX, posY);

                if (nodeID != -1) {
                    PathfinderUtils.remove(nodeID);
                }
                repaint();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyChar();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        key = (char) 0;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mapDrawing(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mapDrawing(e);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (ControlPanel.toggleRunBtn.getText().equals("Run")) {

            if (PathfinderUtils.startNode == null || PathfinderUtils.endNode == null)
                return;

            switch (ControlPanel.algo.getItemAt(ControlPanel.algo.getSelectedIndex())) {
            case ("A*"):
                new AStar(this).start();
                break;

            case ("Dijkstra"):
                System.out.println("Dijkstra selected");
                // AStar.start();
                break;

            case ("Breadth-first search"):
                System.out.println("Breadth-first search selected");
                // AStar.start();
                break;

            default:
                JOptionPane.showMessageDialog(null, "You must select an algorithm before starting the pathfinder",
                        "Algorithm not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        else if (ControlPanel.toggleRunBtn.getText().equals("Clear")) {
            PathfinderUtils.barriers.clear();
            PathfinderUtils.openNodes.clear();
            PathfinderUtils.closedNodes.clear();
            PathfinderUtils.startNode = null;
            PathfinderUtils.endNode = null;

            isFinished = false;

            this.repaint();
            ControlPanel.toggleRunBtn.setText("Run");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
