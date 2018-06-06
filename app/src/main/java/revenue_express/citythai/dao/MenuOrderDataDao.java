package revenue_express.citythai.dao;

/**
 * Created by surachet on 1/5/2017.
 */

public class MenuOrderDataDao {
    int id,multisize,online;
    double price;
    String name,detail,img_thumb,img_full;

    public MenuOrderDataDao(Integer id, String name, String detail, Integer multisize, Integer online, Double price,String img_thumb,String img_full) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.multisize = multisize;
        this.online = online;
        this.price = price;
        this.img_thumb = img_thumb;
        this.img_full = img_full;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMultisize() {
        return multisize;
    }

    public void setMultisize(int multisize) {
        this.multisize = multisize;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImg_thumb() {
        return img_thumb;
    }

    public void setImg_thumb(String img_thumb) {
        this.img_thumb = img_thumb;
    }

    public String getImg_full() {
        return img_full;
    }

    public void setImg_full(String img_full) {
        this.img_full = img_full;
    }
}
