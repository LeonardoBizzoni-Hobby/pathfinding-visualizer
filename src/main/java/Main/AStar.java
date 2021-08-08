package Main;

public class AStar{
    private Map map;
    private int x, y;

    public AStar(Map map) {
        this.map = map;
        PathfinderUtils.path.clear();
    }

    public void start() {
        searchPath(PathfinderUtils.startNode);
    }
    
    public void searchPath(Node parent) {
        for (int i = 0; i < 4; i++) {
            x = (int) Math.round(parent.getX() + (-map.getNodeSize()* Math.cos((Math.PI / 2) * i)));
            y = (int) Math.round(parent.getY() + (-map.getNodeSize() * Math.sin((Math.PI / 2) * i)));

            calculateOpenNode(x, y, parent);
        }

        if((parent = getNextBestNode()) == null)
            return;

        PathfinderUtils.closedNodes.add(parent);
        PathfinderUtils.openNodes.remove(parent);

        if (!map.isFinished())
            searchPath(parent);
    }

    public void calculateOpenNode(int nextX, int nextY, Node parent) {
        if (nextX >= map.getWidth() || nextY >= map.getHeight() || nextX < 0 || nextY < 0)
            return;
        if (PathfinderUtils.locateBarrier(nextX, nextY) != -1)
            return;
        if (nextX == PathfinderUtils.startNode.getX() && nextY == PathfinderUtils.startNode.getY())
            return;
        if (nextX == PathfinderUtils.endNode.getX() && nextY == PathfinderUtils.endNode.getY()) {
            PathfinderUtils.endNode.setParentNode(parent);

            PathfinderUtils.drawPath();
            map.setFinished(true);
            map.setRunning(false);
            map.repaint();
            return;
        }

        Node openNode = new Node(nextX, nextY);
        openNode.setParentNode(parent);

        // Tests if a copy of the node has already been added
        for (Node node : PathfinderUtils.closedNodes) {
            if(openNode.getX() == node.getX() && openNode.getY() == node.getY())
                return;
        }

        // Tests if a copy of the node has already been added
        for (Node node : PathfinderUtils.openNodes) {
            if(openNode.getX() == node.getX() && openNode.getY() == node.getY())
                return;
        }

        int gx = openNode.getX() - parent.getX();
        int gy = openNode.getY() - parent.getY();
        int g = parent.getG();

        if (gx != 0 && gy != 0) {
            g += (int) (Math.sqrt(2 * (Math.pow(map.getNodeSize(), 2))));
        } else {
            g += map.getNodeSize();
        }
        openNode.setG(g);

        // Calculating H Cost
        int hx = Math.abs(PathfinderUtils.endNode.getX() - openNode.getX());
        int hy = Math.abs(PathfinderUtils.endNode.getY() - openNode.getY());
        int h = hx + hy;
        openNode.setH(h);

        // Calculating F Cost
        int f = g + h;
        openNode.setF(f);

        PathfinderUtils.openNodes.add(openNode);
    }

    public Node getNextBestNode() {
        if (PathfinderUtils.openNodes.size() > 0) {
            PathfinderUtils.sort();
            return PathfinderUtils.openNodes.get(0);
        }

        map.setFinished(true);
        map.repaint();
        return null;
    }
}
