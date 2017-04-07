package mg.masmsestro;

/**
 * Created by magda on 2017-04-07.
 */

class Conversation {

    private Integer conv_id;
    private String recipient_list;
    private String snippet;
    private Integer thread_id;
    private long date;
    private Integer read;
    private Integer seen;

    public Integer getConv_id() {
        return conv_id;
    }

    public void setConv_id(Integer conv_id) {
        this.conv_id = conv_id;
    }

    public String getRecipient_list() {
        return recipient_list;
    }

    public void setRecipient_list(String recipient_list) {
        this.recipient_list = recipient_list;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Integer getThread_id() {
        return thread_id;
    }

    public void setThread_id(Integer thread_id) {
        this.thread_id = thread_id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public Integer getSeen() {
        return seen;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }
}
