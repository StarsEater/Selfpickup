package com.zhilai.selfpickup.Object;


public  class ZtgInfoBean {
        /**
         * ztgID : 10000
         * cabinetRow : 1
         * cabinetCol : 1
         * cabinetUsed : 0
         */

        private String ztgID;
        private String cabinetRow;
        private String cabinetCol;
        private String cabinetUsed;

        public Boolean getOpen() {
            return isOpen;
        }

        public void setOpen(Boolean open) {
            isOpen = open;
        }

        private Boolean isOpen;

        public ZtgInfoBean(int i , QueryMess q) {
            this.ztgID = String.valueOf(i);
            this.isOpen = q.isIs_opened();
        }
        public ZtgInfoBean(int i) {
            this.ztgID = String.valueOf(i);
            this.isOpen = true;
        }
        public String getZtgID() {
            return ztgID;
        }

        public void setZtgID(String ztgID) {
            this.ztgID = ztgID;
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

        public String getCabinetUsed() {
            return cabinetUsed;
        }

        public void setCabinetUsed(String cabinetUsed) {
            this.cabinetUsed = cabinetUsed;
        }
    }

