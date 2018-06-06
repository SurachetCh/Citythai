package revenue_express.citythai.dao;

/**
 * Created by chaleamsuk on 12/25/2016.
 */

public class RecommendDataDao {

    String bssi_file,bssi_title;

    public RecommendDataDao(String bssi_file, String bssi_title) {
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
