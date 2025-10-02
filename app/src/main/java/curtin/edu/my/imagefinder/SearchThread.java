package curtin.edu.my.imagefinder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class SearchThread extends Thread
{
    private HttpURLConnection connection;
    private RemoteUtilities remoteUtilities;
    private SearchResponseViewModel viewModel;
    private Activity uiActivity;
    private String searchValue;

    public SearchThread(String searchValue,Activity uiActivity, SearchResponseViewModel viewModel)
    {
        this.searchValue = searchValue;
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
        this.viewModel = viewModel;
        this.uiActivity = uiActivity;
    }
    public void run()
    {
        String endpoint = getSearchEndpoint();
        connection = remoteUtilities.openConnection(endpoint);

        if(connection != null)
        {
            System.out.println(remoteUtilities.isConnectionOkay(connection));
            if(remoteUtilities.isConnectionOkay(connection) == true)
            {
                System.out.println("PLEASE SEND HELP");
                String response = remoteUtilities.getResponseString(connection);
                connection.disconnect();
                try
                {
                    Thread.sleep(3000);
                } catch (Exception e)
                {
                }

                viewModel.setResponse(response);
            }
        }
        System.out.println("Thread end");
    }


    private String getSearchEndpoint()
    {
        String data = null;
        Uri.Builder url = Uri.parse("https://pixabay.com/api/").buildUpon();

        url.appendQueryParameter("key", "40231179-18cff1d2e3081d37b11bad549");
        url.appendQueryParameter("q", searchValue);

        url.appendQueryParameter("image_type", "vector");
        data = url.build().toString();
        return data;
    }

}