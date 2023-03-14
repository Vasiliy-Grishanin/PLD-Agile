package Models;

public class Intersection {
    private long id;
    private double latitude;
    private double longitude;
    private double X;
    private double Y;

    public Intersection(long id, double latitude, double longitude, double X, double Y){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.X = X;
        this.Y =  Y;
    }

    public long getId(){
        return id;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getX(){ return X;}

    public double getY(){ return Y;}
}
