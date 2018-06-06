package revenue_express.citythai.dao;

import java.util.ArrayList;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class ReviewsDataDao {

    String id ,title, description, score, created_date, writer_id, writer_name, writer_photo;
    private ArrayList<String> photo_img = new ArrayList<String>();
    private ArrayList<String> photo_caption = new ArrayList<String>();

    public ReviewsDataDao(String id, String title, String description, String score, String created_date, String writer_id, String writer_name, String writer_photo, ArrayList<String> photo_img, ArrayList<String> photo_caption) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.score = score;
        this.created_date = created_date;
        this.writer_id = writer_id;
        this.writer_name = writer_name;
        this.writer_photo = writer_photo;
        this.photo_img = photo_img;
        this.photo_caption = photo_caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
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

    public ArrayList<String> getPhoto_caption() {
        return photo_caption;
    }

    public void setPhoto_caption(ArrayList<String> photo_caption) {
        this.photo_caption = photo_caption;
    }
}
