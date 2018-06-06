package revenue_express.citythai.model;

/**
 * Created by surachet on 1/12/2017.
 */

public class OrderListDAO {
    private String id_menu, name_menu, price_menu, size_menu,size_name,option_menu,option_name,option_price,instruction_menu, qty;

    public OrderListDAO(String id_menu, String name_menu, String price_menu, String size_menu,String size_name,String option_menu,String option_name,String option_price,String instruction_menu, String qty) {
        this.id_menu = id_menu;
        this.name_menu = name_menu;
        this.price_menu = price_menu;
        this.size_menu = size_menu;
        this.size_name = size_name;
        this.option_menu = option_menu;
        this.option_name = option_name;
        this.option_price = option_price;
        this.instruction_menu = instruction_menu;
        this.qty = qty;
    }

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

    public String getPrice_menu() {
        return price_menu;
    }

    public void setPrice_menu(String price_menu) {
        this.price_menu = price_menu;
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

    public String getOption_menu() {
        return option_menu;
    }

    public void setOption_menu(String option_menu) {
        this.option_menu = option_menu;
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

    public String getInstruction_menu() {
        return instruction_menu;
    }

    public void setInstruction_menu(String instruction_menu) {
        this.instruction_menu = instruction_menu;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
