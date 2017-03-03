package cn.com.helloclyde.ygoService.vo;

/**
 * Created by HelloClyde on 2016/12/11.
 */
public class ResponseResult {
    private int code;
    private Object data;

    public ResponseResult(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Object data) {
        this.code = 0;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
