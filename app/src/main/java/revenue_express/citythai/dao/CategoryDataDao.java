package revenue_express.citythai.dao;

import java.util.ArrayList;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class CategoryDataDao {

    String id ,menu_id, name, detail, seq_num, online, active, count;

    public CategoryDataDao(String id ,String menu_id,String name,String detail,String seq_num,String online,String active,String count) {
        this.id = id;
        this.menu_id = menu_id;
        this.name = name;
        this.detail = detail;
        this.seq_num = seq_num;
        this.online = online;
        this.active = active;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
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

    public String getSeq_num() {
        return seq_num;
    }

    public void setSeq_num(String seq_num) {
        this.seq_num = seq_num;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
