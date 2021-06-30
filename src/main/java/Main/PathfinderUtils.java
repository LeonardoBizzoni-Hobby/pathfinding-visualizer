package Main;

import java.util.ArrayList;
import java.util.List;

public class PathfinderUtils {
    static List<Node> barriers = new ArrayList<>();
    static List<Node> openNodes = new ArrayList<>();
    static List<Node> closedNodes = new ArrayList<>();
    static List<Node> path = new ArrayList<>();
    static Node startNode, endNode;

    // Returns index of barrier if the coordinates are of a barrier node
    static int locateBarrier(int x, int y) {
        for (int i = 0; i < barriers.size(); i++) {
            if (barriers.get(i).getX() == x && barriers.get(i).getY() == y)
                return i;
        }

        return -1;
    }

    // Removes barrier node
    static void remove(int id) {
        barriers.remove(id);
    }

    static boolean isSameNode(Node a, Node b) {
        if (a.getX() == b.getX() && a.getY() == b.getY())
            return true;
        return false;
    }

    static void sort() {
        Node tmp;

        for (int i = 0; i < openNodes.size(); i++) {
            for (int j = 0; j < openNodes.size(); j++) {
                if (openNodes.get(i).getF() < openNodes.get(j).getF()) {
                    tmp = openNodes.get(j);
                    openNodes.set(j, openNodes.get(i));
                    openNodes.set(i, tmp);
                }
            }
        }
    }

    static void drawPath() {
        if (path.size() == 0) {
            Node node = endNode.getParentNode();

            while (!isSameNode(node, startNode)) {
                path.add(node);

                for (int i = 0; i < closedNodes.size(); i++) {
                    Node current = closedNodes.get(i);

                    if (isSameNode(current, node)) {
                        node = current.getParentNode();
                        break;
                    }
                }
            }
        }
    }
}
