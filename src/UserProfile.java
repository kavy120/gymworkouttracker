import java.io.Serializable;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String unitPreference;

    public UserProfile(String userName, String unitPreference) {
        this.userName = userName;
        this.unitPreference = unitPreference;
    }

    public void setUnitPreference(String pref) {
        if (pref == null || pref.trim().isEmpty()) return;
        this.unitPreference = pref.trim();
    }

    public String getUnitPreference() {
        return unitPreference;
    }
}
