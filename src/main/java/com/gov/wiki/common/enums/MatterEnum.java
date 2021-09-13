package com.gov.wiki.common.enums;

public class MatterEnum {
    public enum Attribute{
        Matter(0,"事项"),
        BasicMatters(1,"基础事项"),
        Situation(2,"情形");

        private int key;
        private String value;
        private Attribute(int key, String value) {
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
            for (MatterEnum.Attribute sc : MatterEnum.Attribute.values()) {
                if (sc.key == ec) {
                    return sc.value;
                }
            }
            return null;
        }

        public static Integer getVal(String key) {
            for (MatterEnum.Attribute sc : MatterEnum.Attribute.values()) {
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
    public enum Territory{
        LICENCE(0,"成都"),
        APPROVAL(1,"绵阳"),
        PROVE(2,"雅安"),
        FORM(3,"眉山"),
        OTHER(4,"宜宾");

        private int key;
        private String value;
        private Territory(int key, String value) {
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
            for (MatterEnum.Territory sc : MatterEnum.Territory.values()) {
                if (sc.key == ec) {
                    return sc.value;
                }
            }
            return null;
        }

        public static Integer getVal(String key) {
            for (MatterEnum.Territory sc : MatterEnum.Territory.values()) {
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
    public enum MatterType{
        Enterprise(0,"企业开办","开学校、餐饮店、网吧、代理记账公司"),
        Investment(1,"投资项目审批","开酒厂、粮油加工厂"),
        EntryAndExit(2,"出入境办理","办护照、港澳通行证"),
        RealEstate(3,"不动产登记","抵押房屋、买卖房屋");

        private int key;
        private String value;
        private String describe;
        private MatterType(int key, String value,String describe) {
            this.key = key;
            this.value = value;
            this.describe=describe;
        }

        /**
         * @Title: getVal
         * @Description: 通过key值获取value值
         * @param ec
         * @return String 返回类型
         * @throws
         */
        public static String getVal(int ec) {
            for (MatterEnum.MatterType sc : MatterEnum.MatterType.values()) {
                if (sc.key == ec) {
                    return sc.value;
                }
            }
            return null;
        }

        public static Integer getVal(String key) {
            for (MatterEnum.MatterType sc : MatterEnum.MatterType.values()) {
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

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
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
