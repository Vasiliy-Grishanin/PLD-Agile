package Controllers;

public class HomeController {
    private String mapPath;

    public HomeController(){
    };

    public String getMapPath(){ return mapPath;}
    public void setMapPath(String mapPath){ this.mapPath = mapPath; }
}
