package revenue_express.citythai.dao;

import java.util.List;

/**
 * Created by chaleamsuk on 12/26/2016.
 */

public class MenuDao {
    Boolean success;
    List<MenuDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<MenuDataDao> getData() {
        return data;
    }

    public void setData(List<MenuDataDao> data) {
        this.data = data;
    }
}
