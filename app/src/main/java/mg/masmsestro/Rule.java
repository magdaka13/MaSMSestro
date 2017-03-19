package mg.masmsestro;

/**
 * Created by magda on 2017-03-19.
 */

public class Rule {

    private Integer id_rule;
    private String rule;
    private Integer folder_id;

    public Integer getId_rule() {
        return id_rule;
    }

    public void setId_rule(Integer id_rule) {
        this.id_rule = id_rule;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }
}
