package com.zhilai.selfpickup.Object;

import com.zhilai.selfpickup.Util.Constant;

public class TakeResponse {
    String retCode;
    String retMsg;
    String orderID;
    String cabinetRow;
    String cabinetCol;
    public int getCabinetId(){
        return (Integer.parseInt(cabinetRow)-1)* Constant.cabinetTotalCol
                +(Integer.parseInt(cabinetCol));
    }
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

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCabinetRow() {
        return cabinetRow;
    }

    public void setCabinetRow(String cabinetRow) {
        this.cabinetRow = cabinetRow;
    }

    public String getCabinetCol() {
        return cabinetCol;
    }

    public void setCabinetCol(String cabinetCol) {
        this.cabinetCol = cabinetCol;
    }



}
