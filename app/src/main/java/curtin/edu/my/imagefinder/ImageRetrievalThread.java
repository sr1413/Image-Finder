package curtin.edu.my.imagefinder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ImageRetrievalThread extends Thread
{
    private RemoteUtilities remoteUtilities;
    private SearchResponseViewModel searchViewModel;
    private ImageViewModel imageViewModel;
    private ErrorViewModel errorViewModel;
    private Activity uiActivity;
    public ImageRetrievalThread(Activity uiActivity, SearchResponseViewModel searchViewModel, ImageViewModel imageViewModel, ErrorViewModel errorViewModel)
    {
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
        this.searchViewModel = searchViewModel;
        this.imageViewModel = imageViewModel;
        this.errorViewModel = errorViewModel;
        this.uiActivity = uiActivity;
    }
    public void run()
    {
        String[] endpoint = getEndpoint(searchViewModel.getResponse());
        if(endpoint == null)
        {
            uiActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(uiActivity, "No image found", Toast.LENGTH_LONG).show();
                    errorViewModel.setErrorCode(errorViewModel.getErrorCode()+1);
                }
            });
        }
        else {

            for(String t_endpoint:endpoint)
            {
                System.out.println(t_endpoint);
                Bitmap image = getImageFromUrl(t_endpoint);
                imageViewModel.setImage(image);
            }
        }
        imageViewModel.setIsDone(true);
    }
    private String[] getEndpoint(String data)
    {
        String[] imageUrl = null;
        try
        {

            JSONObject jBase = new JSONObject(data);
            JSONArray jHits = jBase.getJSONArray("hits");
            if (jHits.length() > 0)
            {
                if(jHits.length() >= 15) imageUrl = new String[15];
                else imageUrl = new String[jHits.length()];
                for(int i = 0; i < imageUrl.length; i++)
                {
                    JSONObject jHitsItem = jHits.getJSONObject(i); //do a loop here
                    imageUrl[i]= jHitsItem.getString("largeImageURL");
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return  imageUrl;
    }
    private Bitmap getImageFromUrl(String imageURL)
    {
        Bitmap image = null;
        Uri.Builder url = Uri.parse(imageURL).buildUpon();
        String urlString = url.build().toString();
        HttpURLConnection connection = remoteUtilities.openConnection(urlString);
        if(connection!= null)
        {
            if(remoteUtilities.isConnectionOkay(connection) == true)
            {
                image = getBitMapFromConnection(connection);
                connection.disconnect();
            }
        }
        return image;
    }
    public Bitmap getBitMapFromConnection(HttpURLConnection connection)
    {
        Bitmap data = null;
        try {
            InputStream inputStream = connection.getInputStream();
            byte[] byteData = IOUtils.toByteArray(inputStream);
            data = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}
