package com.gov.wiki.common.enums;

public class OperationEnum {
    public enum OperationStatus{
        save(0, "新增"),
        submit(1, "提交预审"),
        auditing(2, "预审中"),
        pass(3, "预审通过"),
        refuse(4, "预审拒绝");

        private int key;
        private String value;
        OperationStatus(int key, String value) {
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
            for (OperationEnum.OperationStatus sc : OperationEnum.OperationStatus.values()) {
                if (sc.key == ec) {
                    return sc.value;
                }
            }
            return null;
        }

        public static Integer getVal(String key) {
            for (OperationEnum.OperationStatus sc : OperationEnum.OperationStatus.values()) {
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
