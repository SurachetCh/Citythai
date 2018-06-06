package revenue_express.citythai.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class PointHistoryDao {
    Boolean success;
    List<PointHistoryDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<PointHistoryDataDao> getData() {
        return data;
    }

    public void setData(List<PointHistoryDataDao> data) {
        this.data = data;
    }
}
