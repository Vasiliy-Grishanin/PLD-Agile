package Models;

import java.util.List;

public class Path {
    List<Intersection> path;
    double length;

    public Path(List<Intersection> path, double length) {
        this.path = path;
        this.length = length;
    }

    public List<Intersection> getPath() {
        return path;
    }

    public void setPath(List<Intersection> path) {
        this.path = path;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
