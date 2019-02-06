package br.com.beblue.utils;

public class NotifyResponse {

    private Integer status;
    private Integer result;
    private Object object;

    public NotifyResponse() {
        this.status = null;
        this.result = null;
        this.object = null;
    }

    public NotifyResponse(Integer status, Integer result, Object object) {
        this.status = status;
        this.result = result;
        this.object = object;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
