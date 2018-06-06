package revenue_express.citythai.model;

import io.realm.RealmObject;

/**
 * Created by surachet on 1/12/2017.
 */

public class ThemeVersion extends RealmObject {
    private String theme_version;

    public String getTheme_version() {
        return theme_version;
    }

    public void setTheme_version(String theme_version) {
        this.theme_version = theme_version;
    }
}
