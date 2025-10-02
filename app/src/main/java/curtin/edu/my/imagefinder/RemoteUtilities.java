package curtin.edu.my.imagefinder;

import android.app.Activity;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RemoteUtilities
{
    public static RemoteUtilities remoteUtilities = null;
    private Activity uiActivity;
    public RemoteUtilities(Activity uiActivity)
    {
        this.uiActivity = uiActivity;
    }

    public void setUiActivity(Activity uiActivity)
    {
        this.uiActivity = uiActivity;
    }

    public static RemoteUtilities getInstance(Activity uiActivity)
    {
        if(remoteUtilities == null)
        {
            remoteUtilities = new RemoteUtilities(uiActivity);
        }
        remoteUtilities.setUiActivity(uiActivity);
        return remoteUtilities;
    }

    public HttpURLConnection openConnection(String urlString)
    {
        HttpURLConnection conn = null;
        try{
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        if(conn == null)
        {
            uiActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(uiActivity, "Check Internet", Toast.LENGTH_LONG).show();
                }

            });
        }
        return conn;
    }

    public Boolean isConnectionOkay(HttpURLConnection conn)
    {
        Boolean val = false;
        try
        {
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                val = true;
            }
        }catch (IOException e)
        {
            e.printStackTrace();
            uiActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(uiActivity,"Problem with API endpoint", Toast.LENGTH_LONG).show();
                }
            });
        }
        return val;
    }

    public String getResponseString(HttpURLConnection conn)
    {
        String data = null;
        try
        {
            InputStream inputStream = conn.getInputStream();
            byte[] byteData = IOUtils.toByteArray(inputStream);
            data = new String(byteData, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}