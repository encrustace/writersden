package in.encrust.writersden;

import android.net.Uri;

public class ImageModel {
    private Uri image;

    public ImageModel() {
    }

    public ImageModel(Uri image) {
        this.image = image;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
