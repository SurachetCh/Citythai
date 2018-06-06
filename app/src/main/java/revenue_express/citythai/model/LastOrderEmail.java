package revenue_express.citythai.model;

import io.realm.RealmObject;

/**
 * Created by chaleamsuk on 1/19/2017.
 */

public class LastOrderEmail extends RealmObject {


    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}