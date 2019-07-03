package com.zhilai.selfpickup.Object;

import java.util.List;

public class AdminInfo {

    /**
     * admin : [{"adminName":"张三","adminTel":""},{"adminName":"张三","adminTel":""}]
     * ztgLocation : 北京邮电大学
     */

    private String ztgLocation;
    private List<AdminBean> admin;

    public String getZtgLocation() {
        return ztgLocation;
    }

    public void setZtgLocation(String ztgLocation) {
        this.ztgLocation = ztgLocation;
    }

    public List<AdminBean> getAdmin() {
        return admin;
    }

    public void setAdmin(List<AdminBean> admin) {
        this.admin = admin;
    }

    public static class AdminBean {
        /**
         * adminName : 张三
         * adminTel :
         */

        private String adminName;
        private String adminTel;

        public String getAdminName() {
            return adminName;
        }

        public void setAdminName(String adminName) {
            this.adminName = adminName;
        }

        public String getAdminTel() {
            return adminTel;
        }

        public void setAdminTel(String adminTel) {
            this.adminTel = adminTel;
        }
    }
}
