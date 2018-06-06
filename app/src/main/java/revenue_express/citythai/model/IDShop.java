package revenue_express.citythai.model;

import io.realm.RealmObject;

/**
 * Created by surachet on 1/12/2017.
 */

public class IDShop extends RealmObject {
    private int idShop;

    public int getIdShop() {
        return idShop;
    }

    public void setIdShop(int idShop) {
        this.idShop = idShop;
    }
}
