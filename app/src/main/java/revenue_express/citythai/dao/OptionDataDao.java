package revenue_express.citythai.dao;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class OptionDataDao {
//    option_id, option_name, option_detail, option_type, option_counter, option_default,jsonArray

    String option_name,option_detail,option_default,option_id,option_type,option_counter;
//    private ArrayList<String> photo_img = new ArrayList<String>();
    JSONArray jsonArray = new JSONArray();

    public OptionDataDao(String option_id, String option_name,String option_detail,String option_type,String option_counter,String option_default,JSONArray jsonArray) {
        this.option_id = option_id;
        this.option_name = option_name;
        this.option_detail = option_detail;
        this.option_type = option_type;
        this.option_counter = option_counter;
        this.option_default = option_default;
        this.jsonArray = jsonArray;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_default() {
        return option_default;
    }

    public void setOption_default(String option_default) {
        this.option_default = option_default;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public String getOption_detail() {
        return option_detail;
    }

    public void setOption_detail(String option_detail) {
        this.option_detail = option_detail;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setOption_type(String option_type) {
        this.option_type = option_type;
    }

    public String getOption_counter() {
        return option_counter;
    }

    public void setOption_counter(String option_counter) {
        this.option_counter = option_counter;
    }
}
