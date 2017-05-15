package mg.masmsestro;

/**
 * Created by magda on 2017-03-19.
 */

public class Rule {

    private Integer id_rule;
    private String rule_name;
    private String rule_number;
    private String rule_keyword;
    private Integer folder_id;

    public Integer getId_rule() {
        return id_rule;
    }

    public void setId_rule(Integer id_rule) {
        this.id_rule = id_rule;
    }

    public String getRule_number() {
        return rule_number;
    }

    public String getRule_name() {
        return rule_name;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    public void setRule_number(String rule_number) {
        this.rule_number = rule_number;
    }

    public String getRule_keyword() {
        return rule_keyword;
    }

    public void setRule_keyword(String rule_keyword) {
        this.rule_keyword = rule_keyword;
    }

    public Integer getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Integer folder_id) {
        this.folder_id = folder_id;
    }
}
