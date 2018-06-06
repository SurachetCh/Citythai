package revenue_express.citythai.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class TimelineDao {
    Boolean success;
    List<TimelineDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<TimelineDataDao> getData() {
        return data;
    }

    public void setData(List<TimelineDataDao> data) {
        this.data = data;
    }
}
