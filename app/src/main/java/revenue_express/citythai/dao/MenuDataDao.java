package revenue_express.citythai.dao;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

public class MenuDataDao {
    
    String url_image;

    public MenuDataDao(String url_image) {
        this.url_image = url_image;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}
