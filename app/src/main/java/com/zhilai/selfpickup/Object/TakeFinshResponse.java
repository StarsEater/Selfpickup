package com.zhilai.selfpickup.Object;

public class TakeFinshResponse {
    String retCode,
           retMsg,
           cabinetRow,
            cabinetCol;

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
