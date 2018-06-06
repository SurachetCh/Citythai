package revenue_express.citythai.model;

import io.realm.RealmObject;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class CartItemData extends RealmObject {
//    option_id, option_name, option_detail, option_type, option_counter, option_default,jsonArray

    String id_menu,size_menu,size_name,price,qty,instruction_menu,option_id,option_item_id,option_item_name,option_item_price;

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getSize_menu() {
        return size_menu;
    }

    public void setSize_menu(String size_menu) {
        this.size_menu = size_menu;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getInstruction_menu() {
        return instruction_menu;
    }

    public void setInstruction_menu(String instruction_menu) {
        this.instruction_menu = instruction_menu;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_item_id() {
        return option_item_id;
    }

    public void setOption_item_id(String option_item_id) {
        this.option_item_id = option_item_id;
    }

    public String getOption_item_name() {
        return option_item_name;
    }

    public void setOption_item_name(String option_item_name) {
        this.option_item_name = option_item_name;
    }

    public String getOption_item_price() {
        return option_item_price;
    }

    public void setOption_item_price(String option_item_price) {
        this.option_item_price = option_item_price;
    }
}
