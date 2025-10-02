package curtin.edu.my.imagefinder;

import java.util.ArrayList;

public class AppManager
{
    private static AppManager instance = null;

    public static AppManager get()
    {
        if (instance == null)
        {
            instance = new AppManager();
        }
        return instance;
    }
    public static ArrayList<TableData> image = new ArrayList<>();

    public static void setImage(ArrayList<TableData> image)
    {
        AppManager.image = image;
    }

    public static ArrayList<TableData> getImage()
    {
        return image;
    }

    public static void addImage(TableData image)
    {
        AppManager.image.add(image);
    }
}
