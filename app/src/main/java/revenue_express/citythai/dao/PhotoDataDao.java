package revenue_express.citythai.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class PhotoDataDao {

    String urlPhoto;

    public PhotoDataDao(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
