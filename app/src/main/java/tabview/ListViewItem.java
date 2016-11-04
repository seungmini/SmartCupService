package tabview;

/**
 * Created by Lageder on 2016-11-03.
 */

public class ListViewItem {
    private String phone_num;
    private String user_name;

    public void setPhone_num(String num) {
        phone_num = num;
    }

    public void setUser_name(String name) {
        user_name = name;
    }

    public String getPhone_num() {
        return this.phone_num;
    }

    public String getUser_name() {
        return this.user_name;
    }
}
