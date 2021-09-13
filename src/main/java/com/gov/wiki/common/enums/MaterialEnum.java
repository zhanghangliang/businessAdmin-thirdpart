package com.gov.wiki.common.enums;

public class  MaterialEnum {
    public enum MaterialType{
        LICENCE(0,"证照"),
        APPROVAL(1,"批文"),
        PROVE(2,"证明"),
        FORM(3,"表单"),
        OTHER(4,"其他");

        private int key;
        private String value;
        private MaterialType(int key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @Title: getVal
         * @Description: 通过key值获取value值
         * @param ec
         * @return String 返回类型
         * @throws
         */
        public static String getVal(int ec) {
            for (MaterialType sc : MaterialType.values()) {
                if (sc.key == ec) {
                    return sc.value;
                }
            }
            return null;
        }

        public static Integer getVal(String key) {
            for (MaterialType sc : MaterialType.values()) {
                if (sc.value.equals(key)) {
                    return sc.key;
                }
            }
            return null;
        }
        
        public static MaterialType getEnumByKey(int key) {
        	 for (MaterialType sc : MaterialType.values()) {
                 if (sc.key == key) {
                     return sc;
                 }
             }
             return null;
        }

        /**
         * @return the key
         */
        public int getKey() {
            return key;
        }

        /**
         * @param key the key to set
         */
        public void setKey(int key) {
            this.key = key;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }
    }

    public enum OperationStatus{
        BUILD(0,"新建"),
        MODIFY(1,"修改"),
        DELETE(2,"删除");

        private int key;
        private String value;
        private OperationStatus(int key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @Title: getVal
         * @Description: 通过key值获取value值
         * @param ec
         * @return String 返回类型
         * @throws
         */
        public static String getVal(int ec) {
            for (OperationStatus sc : OperationStatus.values()) {
                if (sc.key == ec) {
                    return sc.value;
                }
            }
            return null;
        }

        public static Integer getVal(String key) {
            for (OperationStatus sc : OperationStatus.values()) {
                if (sc.value.equals(key)) {
                    return sc.key;
                }
            }
            return null;
        }

        /**
         * @return the key
         */
        public int getKey() {
            return key;
        }

        /**
         * @param key the key to set
         */
        public void setKey(int key) {
            this.key = key;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }
    }
}
