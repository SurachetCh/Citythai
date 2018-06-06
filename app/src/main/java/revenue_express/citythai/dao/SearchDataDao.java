package revenue_express.citythai.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class SearchDataDao {
    String bssi_file,bssi_title;

    public SearchDataDao(String bssi_file, String bssi_title) {
        this.bssi_file = bssi_file;
        this.bssi_title = bssi_title;
    }

    public String getBssi_file() {
        return bssi_file;
    }

    public void setBssi_file(String bssi_file) {
        this.bssi_file = bssi_file;
    }

    public String getBssi_title() {
        return bssi_title;
    }

    public void setBssi_title(String bssi_title) {
        this.bssi_title = bssi_title;
    }
}
