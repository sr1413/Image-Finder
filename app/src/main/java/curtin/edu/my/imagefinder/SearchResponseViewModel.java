package curtin.edu.my.imagefinder;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchResponseViewModel extends ViewModel
{
    public MutableLiveData<String> response;
    public SearchResponseViewModel() {response = new MutableLiveData<>();}

    public String getResponse()
    {
        return response.getValue();
    }

    public void setResponse(String response)
    {
        this.response.postValue(response);
    }

}
