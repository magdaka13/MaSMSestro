package mg.masmsestro;

/**
 * Created by magda on 2017-03-19.
 */

public class SMS {

    private Integer sms_id;
    private String tel_no;
    private String content;

    public Integer getSms_id() {
        return sms_id;
    }

    public void setSms_id(Integer sms_id) {
        this.sms_id = sms_id;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
