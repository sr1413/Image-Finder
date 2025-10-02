package curtin.edu.my.imagefinder;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImageViewModel extends ViewModel
{
    public MutableLiveData<Bitmap> image;
    public MutableLiveData<Boolean> isDone;
    public ImageViewModel()
    {
        image = new MutableLiveData<>();
        isDone = new MutableLiveData<>();
    }
    public Bitmap getImage()
    {
        return image.getValue();
    }
    public void setImage(Bitmap bitmap)
    {
        image.postValue(bitmap);
    }

    public Boolean getIsDone()
    {
        return isDone.getValue();
    }
    public void setIsDone(Boolean done)
    {
        isDone.postValue(done);
    }



}
