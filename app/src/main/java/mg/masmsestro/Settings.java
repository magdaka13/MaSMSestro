package mg.masmsestro;

/**
 * Created by magda on 2017-03-19.
 */

public class Settings {
  private Integer id_setting;
  private String name;
  private String value;

    public Integer getId_setting() {
        return id_setting;
    }

    public void setId_setting(Integer id_setting) {
        this.id_setting = id_setting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
