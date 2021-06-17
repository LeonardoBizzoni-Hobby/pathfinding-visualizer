package Main;

import java.util.ArrayList;
import java.util.List;

public class PathfinderUtils {
    static List<Node> barriers = new ArrayList<>();
    static Node startNode, endNode;

    static int locate(int x, int y) {
        for (int i = 0; i < barriers.size(); i++) {
            if(barriers.get(i).getX() == x && barriers.get(i).getY() == y)
                return i;
        }

        return -1;
    }

    static void remove(int id) {
        barriers.remove(id);
    }
}
