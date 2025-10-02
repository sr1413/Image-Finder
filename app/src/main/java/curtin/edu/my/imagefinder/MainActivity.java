package curtin.edu.my.imagefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    public EditText searchBox;
    public Button searchButton;
    public Fragment recycler;
    public Button singleView;
    public Button doubleView;
    public ProgressBar progressBar;
    public int mode;

    SearchResponseViewModel searchResponseViewModel;
    ImageViewModel imageViewModel;
    ErrorViewModel errorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchResponseViewModel = new ViewModelProvider(this,
                (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory())
                .get(SearchResponseViewModel.class);
        imageViewModel = new ViewModelProvider(this,
                (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory())
                .get(ImageViewModel.class);
        errorViewModel = new ViewModelProvider(this,
                (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory())
                .get(ErrorViewModel.class);

        mode=1;
        searchBox = findViewById(R.id.SearchBox);
        searchButton = findViewById(R.id.SearchButton);
        singleView = findViewById(R.id.SingleView);
        doubleView = findViewById(R.id.DoubleView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        AppManager.get();


        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                //Search for images;
                String searchValues = searchBox.getText().toString();
                SearchThread searchThread = new SearchThread(searchValues, MainActivity.this, searchResponseViewModel);

                //reset tabledata so the old stuff gets deleted
                AppManager.setImage(new ArrayList<>());
                //progressBar.setVisibility(View.VISIBLE);
                searchThread.start();
            }
        });
        singleView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //set mode 1
                mode = 1;
                //and then reset the activity / recycler view;
                //does not reset if mode already 1
                if(imageViewModel.getIsDone())
                {
                    loadRecyclerFragment(mode);
                }
            }
        });

        doubleView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                mode = 2;
                if(imageViewModel.getIsDone())
                {
                    loadRecyclerFragment(mode);
                }
            }
        });




        searchResponseViewModel.response.observe(this, new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                Toast.makeText(MainActivity.this, "Connection found, Getting Images", Toast.LENGTH_LONG).show();
                ImageRetrievalThread imageRetrievalThread = new ImageRetrievalThread(MainActivity.this, searchResponseViewModel, imageViewModel, errorViewModel);
                imageRetrievalThread.start();
            }
        });

        imageViewModel.isDone.observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean aBoolean)
            {
                if(imageViewModel.getIsDone())
                {
                    loadRecyclerFragment(mode);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Search Complete", Toast.LENGTH_LONG).show();

                }
            }
        });

        imageViewModel.image.observe(this, new Observer<Bitmap>()
        {
            @Override
            public void onChanged(Bitmap bitmap)
            {
                TableData data = new TableData(imageViewModel.getImage());
                AppManager.addImage(data);
            }
        });
        errorViewModel.errorCode.observe(this, new Observer<Integer>()
        {
            @Override
            public void onChanged(Integer integer)
            {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void loadRecyclerFragment (int p_mode)
    {
        FragmentManager fm = getSupportFragmentManager();
        recycler = new RecyclerViewFragment(p_mode);

        if(recycler == null) {
            fm.beginTransaction().add(R.id.container_recycler, recycler, "imageDisplay").commit();
        } else {
            fm.beginTransaction().replace(R.id.container_recycler, recycler, "imageDisplay").commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}