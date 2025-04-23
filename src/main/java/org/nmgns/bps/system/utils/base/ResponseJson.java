package org.nmgns.bps.system.utils.base;


/**
 * 服务端返回给客户端的数据
 */
public class ResponseJson {


    private Boolean success;

    private String msg;

    private Long total;

    private Object data;

    private Object children;

    private String code;

    public ResponseJson() {
    }

    public ResponseJson(Boolean success, String msg, Long total, Object data, Object children) {
        this.success = success;
        this.msg = msg;
        this.total = total;
        this.data = data;
        this.children = children;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
