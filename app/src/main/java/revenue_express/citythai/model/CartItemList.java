package revenue_express.citythai.model;

import org.json.JSONArray;

import java.util.ArrayList;

import io.realm.RealmObject;

/**
 * Created by ChetPC on 12/14/2016.
 */
public class CartItemList extends RealmObject {
//    option_id, option_name, option_detail, option_type, option_counter, option_default,jsonArray

    String id_menu,name_menu,size_menu,size_name,price,qty,instruction_menu,option_menu,option_name,option_price,price_total;


    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getName_menu() {
        return name_menu;
    }

    public void setName_menu(String name_menu) {
        this.name_menu = name_menu;
    }

    public String getSize_menu() {
        return size_menu;
    }

    public void setSize_menu(String size_menu) {
        this.size_menu = size_menu;
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

    public String getOption_menu() {
        return option_menu;
    }

    public void setOption_menu(String option_menu) {
        this.option_menu = option_menu;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public String getOption_price() {
        return option_price;
    }

    public void setOption_price(String option_price) {
        this.option_price = option_price;
    }

    public String getPrice_total() {
        return price_total;
    }

    public void setPrice_total(String price_total) {
        this.price_total = price_total;
    }
}
