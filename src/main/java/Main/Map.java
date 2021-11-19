package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class Map extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private JFrame window;
    private char key = (char) 0;

    private boolean isFinished = false;
    private boolean running = false;

    private int size = 20;
    int speed = 2;

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

        // Draws external nodes
        g.setColor(new Color(69, 133, 136));
        for (int i = 0; i < PathfinderUtils.openNodes.size(); i++) {
            g.fillRect(PathfinderUtils.openNodes.get(i).getX() + 1, PathfinderUtils.openNodes.get(i).getY() + 1,
                    size - 1, size - 1);
        }

        // Draws internal nodes
        g.setColor(new Color(77, 77, 77));
        for (int i = 0; i < PathfinderUtils.closedNodes.size(); i++) {
            g.fillRect(PathfinderUtils.closedNodes.get(i).getX() + 1, PathfinderUtils.closedNodes.get(i).getY() + 1,
                    size - 1, size - 1);
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
        Node node = new Node(e.getX() - (e.getX() % size), e.getY() - (e.getY() % size));

        if (SwingUtilities.isLeftMouseButton(e)) {
            // Start node
            if (key == 's') {
                // If both start and end node don't exist just create the start node
                if (PathfinderUtils.startNode == null && PathfinderUtils.endNode == null)
                    // Set the node as the start node
                    PathfinderUtils.startNode = node;

                // If the end node exists check if the node is the same else create the start
                // node
                else if (PathfinderUtils.endNode != null && PathfinderUtils.startNode == null) {
                    if (PathfinderUtils.isSameNode(node, PathfinderUtils.endNode)) {
                        // Send an error message saying that start and end node are the same
                        JOptionPane.showMessageDialog(null, "End node and start node can't be the same node",
                                "Same node error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Set the node as the start node
                    PathfinderUtils.startNode = node;

                }

                // If they both exist check if same node else move che start node
                else {
                    if (PathfinderUtils.endNode != null && PathfinderUtils.isSameNode(node, PathfinderUtils.endNode)) {
                        JOptionPane.showMessageDialog(null, "End node and start node can't be the same node",
                                "Same node error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Change start node coordinates without creating a new node
                    PathfinderUtils.startNode.setX(node.getX());
                    PathfinderUtils.startNode.setY(node.getY());
                }
            }

            // End node
            else if (key == 'e') {
                // If both start and end node don't exist just create the end node
                if (PathfinderUtils.startNode == null && PathfinderUtils.endNode == null)
                    // Set the node as the end node
                    PathfinderUtils.endNode = node;

                // If the start node exists check if the node is the same else create the end node
                else if (PathfinderUtils.startNode != null && PathfinderUtils.endNode == null) {
                    if (PathfinderUtils.isSameNode(node, PathfinderUtils.startNode)) {
                        // Send an error message saying that start and end node are the same
                        JOptionPane.showMessageDialog(null, "End node and start node can't be the same node",
                                "Same node error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Set the node as the end node
                    PathfinderUtils.endNode = node;

                }
                // If they both exist check if same node else move the end node
                else {
                    if (PathfinderUtils.startNode !=null && PathfinderUtils.isSameNode(node, PathfinderUtils.startNode)) {
                        JOptionPane.showMessageDialog(null, "End node and start node can't be the same node",
                                "Same node error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Change end node coordinates without creating a new node
                    PathfinderUtils.endNode.setX(node.getX());
                    PathfinderUtils.endNode.setY(node.getY());
                }
            }

            // Barriers
            else {
                // If the start/end node exists check if the node is the same
                // If it is return
                if (PathfinderUtils.startNode != null && PathfinderUtils.isSameNode(node, PathfinderUtils.startNode))
                    return;
                if (PathfinderUtils.endNode != null && PathfinderUtils.isSameNode(node, PathfinderUtils.endNode))
                    return;

                PathfinderUtils.barriers.add(node);
            }

            // Update the UI with barrier/start/end node
            repaint();
        }

        // Deleting nodes
        else if (SwingUtilities.isRightMouseButton(e)) {
            int posX = e.getX() - (e.getX() % size);
            int posY = e.getY() - (e.getY() % size);

            if (key == 's' && PathfinderUtils.startNode != null) {
                if (PathfinderUtils.startNode.getX() == posX && PathfinderUtils.startNode.getY() == posY) {
                    PathfinderUtils.startNode = null;
                }
            }

            else if (key == 'e' && PathfinderUtils.endNode != null) {
                if (PathfinderUtils.endNode.getX() == posX && PathfinderUtils.endNode.getY() == posY) {
                    PathfinderUtils.endNode = null;
                }
            }

            else {
                int nodeID = PathfinderUtils.locateBarrier(posX, posY);

                if (nodeID != -1) {
                    PathfinderUtils.remove(nodeID);
                }
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyChar();

        if (key == KeyEvent.VK_SPACE) {
            if (running == false && isFinished == false) {
                if (PathfinderUtils.startNode == null || PathfinderUtils.endNode == null) {
                    JOptionPane.showMessageDialog(null, "Missing start or end node", "Missing node",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                running = true;

                // AStar needs to extend runnable because otherwise map.repaint() will wait to update the UI
                new Thread(new AStar(this)).start();
            } else if (running == false && isFinished == true) {
                PathfinderUtils.barriers.clear();
                PathfinderUtils.openNodes.clear();
                PathfinderUtils.closedNodes.clear();
                PathfinderUtils.startNode = null;
                PathfinderUtils.endNode = null;

                isFinished = false;
                running = false;

                this.repaint();
            }
        }
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getNodeSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
