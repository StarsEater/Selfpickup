package com.zhilai.selfpickup.Object;

import java.util.List;

public class cabinetInfo {

    /**
     * retCode :
     * retMsg :
     * ztgInfo : [{"ztgID":"10000","cabinetRow":"1","cabinetCol":"1","cabinetUsed":"0"},{"ztgID":"10000","cabinetRow":"1","cabinetCol":"2","cabinetUsed":"0"}]
     */

    private String retCode;
    private String retMsg;
    private List<ZtgInfoBean> ztgInfo;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public List<ZtgInfoBean> getZtgInfo() {
        return ztgInfo;
    }

    public void setZtgInfo(List<ZtgInfoBean> ztgInfo) {
        this.ztgInfo = ztgInfo;
    }

}
