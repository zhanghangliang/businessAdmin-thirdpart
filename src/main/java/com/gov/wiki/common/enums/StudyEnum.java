package com.gov.wiki.common.enums;

public class StudyEnum {
    public enum StudyType{
        politics(0,"政治资料"),
        history(1,"历史资料"),
        rule(2,"规则资料");

        private int key;
        private String value;
        private StudyType(int key, String value) {
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
            for (StudyEnum.StudyType sc : StudyEnum.StudyType.values()) {
                if (sc.key == ec) {
                    return sc.value;
                }
            }
            return null;
        }

        public static Integer getVal(String key) {
            for (StudyEnum.StudyType sc : StudyEnum.StudyType.values()) {
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
