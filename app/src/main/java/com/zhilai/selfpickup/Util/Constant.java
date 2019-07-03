package com.zhilai.selfpickup.Util;

/**
 * 常量
 */
public class Constant {
    // request参数
//    public  static String [] boxId={
//            "A01", "A05", "A09", "A13",
//            "A02", "A06", "A10", "A14",
//            "A03", "A07", "A11", "A15",
//            "A04", "A08", "A12", "A16"};
//}
    public static boolean superF = true;
    public static String ztgID = "10000";
    public static int cabinetTotalRow = 4;
    public static int cabinetTotalCol= 4;
    public static int cabinetTotalNum = 16;
    public static void setData(String ztgID,int cabinetTotalRow,int cabinetTotalCol,int cabinetTotalNum){
        Constant.ztgID = ztgID;
        Constant.cabinetTotalRow = cabinetTotalRow;
        Constant.cabinetTotalCol = cabinetTotalCol;
        Constant.cabinetTotalNum = cabinetTotalNum;
    }
    public static String [] getBoxId(int m, int n,String suffix){
        String [] boxId = new String[m*n];
        int p = 0;
        for(int i = 1; i <= m; i++ )
            for(int j = 0; j < n; j++){
                int t = i + m*j;
                String s = t < 10 ? "0" + t:String.valueOf(t);
                s = suffix + s;
                boxId[p++] = s;
            }

        return boxId;

    }



}