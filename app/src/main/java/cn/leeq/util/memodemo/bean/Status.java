package cn.leeq.util.memodemo.bean;

/**
 * Created by LeeQ
 * Date : 2016-8-8
 * Name : MemoDemo
 * Use :
 */
public class Status {

    /**
     * awb :
     * create_date : 2016-06-02 11:44:37.0
     * creator_id : 10
     * creator_name : 老唐
     * creator_type : 1
     * express_id : 201
     * id : 519
     * status : 1
     * status_name : 提交订单
     */

    private String awb;
    private String create_date;
    private int creator_id;
    private String creator_name;
    private int creator_type;
    private int express_id;
    private int id;
    private int status;
    private String status_name;

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public int getCreator_type() {
        return creator_type;
    }

    public void setCreator_type(int creator_type) {
        this.creator_type = creator_type;
    }

    public int getExpress_id() {
        return express_id;
    }

    public void setExpress_id(int express_id) {
        this.express_id = express_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }
}
