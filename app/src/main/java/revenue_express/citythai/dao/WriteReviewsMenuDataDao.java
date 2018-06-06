package revenue_express.citythai.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class WriteReviewsMenuDataDao {

    String id,menu_name,url_menu_image;

    public WriteReviewsMenuDataDao(String id , String menu_name, String url_menu_image) {
        this.id = id;
        this.menu_name = menu_name;
        this.url_menu_image = url_menu_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getUrl_menu_image() {
        return url_menu_image;
    }

    public void setUrl_menu_image(String url_menu_image) {
        this.url_menu_image = url_menu_image;
    }
}
