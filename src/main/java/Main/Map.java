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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Map extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    private JFrame window;
    private int size = 30;
    private char key = (char) 0;

    private Node startNode, endNode;
    private List<Node> barriers = new ArrayList<>();

    public Map() {
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        addMouseMotionListener(this);

        // Settings up the window
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
        g.setColor(Color.lightGray);
        for (int i = 0; i < this.getWidth(); i += size) {
            for (int j = 0; j < this.getHeight(); j += size) {
                g.drawRect(i, j, size, size);
            }
        }

        // Draws start node
        if (startNode != null) {
            g.setColor(new Color(117, 110, 202));
            g.fillRect(startNode.getX() + 1, startNode.getY() + 1, size - 1, size - 1);
        }

        // Draws end node
        if (endNode != null) {
            g.setColor(new Color(204, 36, 29));
            g.fillRect(endNode.getX() + 1, endNode.getY() + 1, size - 1, size - 1);
        }

        // Draws barrier nodes
        g.setColor(new Color(40, 40, 40));
        for (Node node : barriers) {
            g.fillRect(node.getX() + 1, node.getY() + 1, size - 1, size - 1);
        }
    }

    // Drawing on the grid
    public void mapDrawing(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (key == 's') {
                int posX = e.getX() % size;
                int posY = e.getY() % size;

                if (startNode == null) {
                    startNode = new Node(e.getX() - posX, e.getY() - posY);
                } else {
                    startNode.setX(e.getX() - posX);
                    startNode.setY(e.getY() - posY);
                }

                repaint();
            }

            else if (key == 'e') {
                int posX = e.getX() % size;
                int posY = e.getY() % size;

                if (endNode == null) {
                    endNode = new Node(e.getX() - posX, e.getY() - posY);
                } else {
                    endNode.setX(e.getX() - posX);
                    endNode.setY(e.getY() - posY);
                }

                repaint();
            }

            else {
                int posX = e.getX() % size;
                int posY = e.getY() % size;

                barriers.add(new Node(e.getX() - posX, e.getY() - posY));

                repaint();
            }
        }
        // TODO cancellare le barriere col tasto destro del mouse
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
