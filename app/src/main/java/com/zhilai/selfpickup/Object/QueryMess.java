package com.zhilai.selfpickup.Object;

//"result_code":0,
// "error_msg":"Succes
//  "is_opened":true
public class QueryMess {

    /**
     * result_code : 0
     * error_msg : Success.
     * is_opened : true
     */

    private int result_code;
    private String error_msg;
    private boolean is_opened;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public boolean isIs_opened() {
        return is_opened;
    }

    public void setIs_opened(boolean is_opened) {
        this.is_opened = is_opened;
    }
}
