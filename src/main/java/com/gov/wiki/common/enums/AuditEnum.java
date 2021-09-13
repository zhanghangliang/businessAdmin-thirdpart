package com.gov.wiki.common.enums;

public class AuditEnum {
    public enum AuditState{
        PENDING(0,"待审"),
        REFUES(1,"拒绝"),
        ADOPT(2,"通过");

        private int key;
        private String value;
        private AuditState(int key, String value) {
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
            for (AuditState sc : AuditState.values()) {
                if (sc.key == ec) {
                    return sc.value;
                }
            }
            return null;
        }

        public static Integer getVal(String key) {
            for (AuditState sc : AuditState.values()) {
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
    public enum Audittype{
        MATERIAL(0,"资料"),
        MATTER(1,"事项"),
        SUBJECT(2,"主题");

        private int key;
        private String value;
        private Audittype(int key, String value) {
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
            for (Audittype sc : Audittype.values()) {
                if (sc.key == ec) {
                    return sc.value;
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
