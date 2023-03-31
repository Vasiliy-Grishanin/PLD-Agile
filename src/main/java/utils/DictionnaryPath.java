package utils;

import Models.Delivery;
import Models.Intersection;
import Models.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DictionnaryPath {
    private DeliveryNode startNode;
    private DeliveryNode endNode;
    private Path path;

    public DictionnaryPath(DeliveryNode startNode, DeliveryNode endNode, Path path) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.path = path;
    }

    public Path getPath(DeliveryNode start, DeliveryNode end) {
        if (isNodesEqual(startNode, start) && isNodesEqual(endNode, end)) {
            return path;
        } else if (isNodesEqual(startNode, end) && isNodesEqual(endNode, start)) {
            List<Intersection> reversedPath = new ArrayList<>(path.getPath());
            Collections.reverse(reversedPath);
            return new Path(reversedPath, path.getLength());
        }
        return null;
    }

    private boolean isNodesEqual(DeliveryNode n1, DeliveryNode n2) {
        return n1 == n2 | n1.getDelivery().getAddress().getId() == n2.getDelivery().getAddress().getId();
    }
}
