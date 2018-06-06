package revenue_express.citythai.model;

import io.realm.RealmObject;

/**
 * Created by chaleamsuk on 1/19/2017.
 */

public class OptionAddExtraList extends RealmObject {

    private String option_id,item_id,price;

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}