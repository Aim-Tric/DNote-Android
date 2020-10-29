package top.devonte.note.bean;

public class ResultBean<T> {

    private T body;
    private Integer statusCodeValue;
    private String statusCode;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Integer getStatusCodeValue() {
        return statusCodeValue;
    }

    public void setStatusCodeValue(Integer statusCodeValue) {
        this.statusCodeValue = statusCodeValue;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
