/* TODO
 * aggiungere una sorta di menu dove scegliere:
 * - velocità di riproduzione
 * - tipo di algoritmo:
 *    + a*
 *    + dijkstra
 *    + greedy best-first search
 *    + swarm
 *    + convergent swarm
 *    + bidirectional swarm
 *    + breadth-first search
 *    + depth-first search
 * zoommare sulla griglia
 */
package Main;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Map extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private JFrame window;
    private int size = 30;
    private char key = (char) 0;

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
        g.setColor(new Color(235, 219, 178));
        for (Node node : PathfinderUtils.barriers) {
            g.fillRect(node.getX() + 1, node.getY() + 1, size - 1, size - 1);
        }
    }

    // Drawing on the grid
    public void mapDrawing(MouseEvent e) {
        // Creating nodes
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (key == 's') {
                int posX = e.getX() - (e.getX() % size);
                int posY = e.getY() - (e.getY() % size);

                // TODO fa cagare
                // Checks if start node and end node are the same
                if (PathfinderUtils.endNode != null) {
                    if (PathfinderUtils.endNode.getX() == posX && PathfinderUtils.endNode.getY() == posY) {
                        JOptionPane.showMessageDialog(null, "End node and start node can't be the same node", "Same node error", JOptionPane.ERROR_MESSAGE);
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

                // TODO fa cagare
                // Checks if end node and start node are the same
                if (PathfinderUtils.startNode != null) {
                    if (PathfinderUtils.startNode.getX() == posX && PathfinderUtils.startNode.getY() == posY) {
                        JOptionPane.showMessageDialog(null, "End node and start node can't be the same node!", "SAME NODE ERROR", JOptionPane.ERROR_MESSAGE);
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

                // TODO controlla se si sta creando una barriera su start o end


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
                int nodeID = PathfinderUtils.locate(posX, posY);

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
        // TODO far partire/fermare l'algoritmo con il tasto invio
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
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}