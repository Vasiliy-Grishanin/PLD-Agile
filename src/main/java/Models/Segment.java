package Models;

import java.awt.geom.Line2D;

public class Segment {
    private long destination;
    private long origin;
    private double length;
    private String name;
    private Line2D.Double line;

    public Segment(long destination, long origin, double length, String name, Line2D.Double line){
        this.destination = destination;
        this.origin = origin;
        this.length = length;
        this.name = name;
        this.line = line;
    }

    public long getDestination(){
        return destination;
    }

    public long getOrigin(){
        return origin;
    }

    public double getLength(){
        return length;
    }

    public String getName(){
        return name;
    }

    public Line2D.Double getLine(){ return line;}
}
