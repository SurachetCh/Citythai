package revenue_express.citythai.dao;

import java.util.ArrayList;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class TimelineDataDao {

    String id_social,type_social ,title_social, description_social, score_social, created_date_social, writer_id, writer_name, writer_photo, video_url;
    private ArrayList<String> photo_img = new ArrayList<String>();

    public TimelineDataDao(String id_social, String type_social, String title_social, String description_social, String score_social, String created_date_social, String writer_id, String writer_name, String writer_photo, String video_url, ArrayList<String> photo_img) {
        this.id_social = id_social;
        this.type_social = type_social;
        this.title_social = title_social;
        this.description_social = description_social;
        this.score_social = score_social;
        this.created_date_social = created_date_social;
        this.writer_id = writer_id;
        this.writer_name = writer_name;
        this.writer_photo = writer_photo;
        this.video_url = video_url;
        this.photo_img = photo_img;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getId_social() {
        return id_social;
    }

    public void setId_social(String id_social) {
        this.id_social = id_social;
    }

    public String getType_social() {
        return type_social;
    }

    public void setType_social(String type_social) {
        this.type_social = type_social;
    }

    public String getTitle_social() {
        return title_social;
    }

    public void setTitle_social(String title_social) {
        this.title_social = title_social;
    }

    public String getDescription_social() {
        return description_social;
    }

    public void setDescription_social(String description_social) {
        this.description_social = description_social;
    }

    public String getScore_social() {
        return score_social;
    }

    public void setScore_social(String score_social) {
        this.score_social = score_social;
    }

    public String getCreated_date_social() {
        return created_date_social;
    }

    public void setCreated_date_social(String created_date_social) {
        this.created_date_social = created_date_social;
    }

    public String getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(String writer_id) {
        this.writer_id = writer_id;
    }

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String writer_name) {
        this.writer_name = writer_name;
    }

    public String getWriter_photo() {
        return writer_photo;
    }

    public void setWriter_photo(String writer_photo) {
        this.writer_photo = writer_photo;
    }

    public ArrayList<String> getPhoto_img() {
        return photo_img;
    }

    public void setPhoto_img(ArrayList<String> photo_img) {
        this.photo_img = photo_img;
    }
}
