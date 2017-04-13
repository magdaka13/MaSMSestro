package mg.masmsestro;

import java.util.Date;

/**
 * Created by magda on 2017-03-19.
 */

public class SMS {

    private Integer sms_id;
    private String tel_no;
    private String content;
    private long date_sent;
    private long date_received;
    private String read;
    private String seen;
    private String person;
    private int thread_id;
    private int type;

    public long getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(long date_sent) {
        this.date_sent = date_sent;
    }

    public long getDate_received() {
        return date_received;
    }

    public void setDate_received(long date_received) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

