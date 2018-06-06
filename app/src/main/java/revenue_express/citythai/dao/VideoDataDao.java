package revenue_express.citythai.dao;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class VideoDataDao {

    String url_video,name_video;

    public VideoDataDao(String url_video, String name_video) {
        this.url_video = url_video;
        this.name_video = name_video;
    }

    public String getUrl_video() {
        return url_video;
    }

    public void setUrl_video(String url_video) {
        this.url_video = url_video;
    }

    public String getName_video() {
        return name_video;
    }

    public void setName_video(String name_video) {
        this.name_video = name_video;
    }
}
