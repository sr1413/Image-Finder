package curtin.edu.my.imagefinder;

import android.graphics.Bitmap;

public class TableData {
    private Bitmap image;

    public TableData(Bitmap image) {
        this.image = image;
    }

    public TableData() {}

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
