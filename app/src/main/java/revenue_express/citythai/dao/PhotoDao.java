package revenue_express.citythai.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class PhotoDao {
    Boolean success;
    List<PhotoDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<PhotoDataDao> getData() {
        return data;
    }

    public void setData(List<PhotoDataDao> data) {
        this.data = data;
    }
}
