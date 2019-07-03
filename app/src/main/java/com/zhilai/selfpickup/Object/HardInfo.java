package com.zhilai.selfpickup.Object;

public class HardInfo {
    public String getZtgID() {
        return ztgID;
    }

    public void setZtgID(String ztgID) {
        this.ztgID = ztgID;
    }

    public int getCabinetTotalRow() {
        return cabinetTotalRow;
    }

    public void setCabinetTotalRow(int cabinetTotalRow) {
        this.cabinetTotalRow = cabinetTotalRow;
    }

    public int getCabinetTotalCol() {
        return cabinetTotalCol;
    }

    public void setCabinetTotalCol(int cabinetTotalCol) {
        this.cabinetTotalCol = cabinetTotalCol;
    }

    public int getCabinetTotalNum() {
        return cabinetTotalNum;
    }

    public void setCabinetTotalNum(int cabinetTotalNum) {
        this.cabinetTotalNum = cabinetTotalNum;
    }
    public String toString(){
        return "配置文件数据:\n"+
                "柜子ID:"+ztgID+" "+
                "总行数"+cabinetTotalRow+" "+
                "总列数"+cabinetTotalCol+" "+
                "总数"+cabinetTotalNum;

    }
    private String ztgID;
      private int cabinetTotalRow;
      private int cabinetTotalCol;
      private int cabinetTotalNum;
}
