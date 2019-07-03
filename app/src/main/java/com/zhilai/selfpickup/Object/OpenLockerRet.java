package com.zhilai.selfpickup.Object;

public class OpenLockerRet {

    /**
     * result_code : 0
     * error_msg : Success.
     */

    private int result_code;
    private String error_msg;
    public OpenLockerRet(){
        result_code = 1;
        error_msg = "未知错误";
    }

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
}
