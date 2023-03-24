package App;
import Controllers.HomeController;
import Controllers.MapController;
import Models.Intersection;
import Models.Segment;
import Views.HomeView;
import Views.MapView;

import java.util.ArrayList;


public class App 
{
    public static void main( String[] args )
    {
        // créer une instance de Home
        HomeController homeController = new HomeController();
        HomeView homeView = new HomeView(homeController);
        homeView.setVisible(true);

    }
}
