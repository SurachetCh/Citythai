package revenue_express.citythai.dao;

import java.util.List;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class VideoDao {
    Boolean success;
    List<VideoDataDao> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<VideoDataDao> getData() {
        return data;
    }

    public void setData(List<VideoDataDao> data) {
        this.data = data;
    }
}
