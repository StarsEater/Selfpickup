package com.zhilai.selfpickup.Object;

public class DistributionResponse {

    /**
     * retCode :
     * retMsg :
     * cabinetRow :
     * cabinetCol :
     */

    private String retCode;
    private String retMsg;
    private String cabinetRow;
    private String cabinetCol;

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

    public int getCabinetRow() {
        return Integer.parseInt(cabinetRow);
    }

    public void setCabinetRow(String cabinetRow) {
        this.cabinetRow = cabinetRow;
    }

    public int getCabinetCol() {
        return Integer.parseInt(cabinetCol);
    }

    public void setCabinetCol(String cabinetCol) {
        this.cabinetCol = cabinetCol;
    }
}
