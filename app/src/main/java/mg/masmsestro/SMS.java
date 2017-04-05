package mg.masmsestro;

import java.util.Date;

/**
 * Created by magda on 2017-03-19.
 */

public class SMS {

    private Integer sms_id;
    private String tel_no;
    private String content;
    private Date date_sent;
    private Date date_received;
    private String read;
    private String seen;
    private String person;
    private int thread_id;

    public Date getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(Date date_sent) {
        this.date_sent = date_sent;
    }

    public Date getDate_received() {
        return date_received;
    }

    public void setDate_received(Date date_received) {
        this.date_received = date_received;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

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
