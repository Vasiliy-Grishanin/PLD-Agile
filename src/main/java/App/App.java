package App;
import Controllers.HomeController;
import Views.HomeView;

/**
 * Hello world!
 *
 */
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
