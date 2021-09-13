package com.gov.wiki.common.exception;

public class BeanChangException extends RuntimeException {

    public BeanChangException(String string) {
        // TODO Auto-generated constructor stub
        super(string);
    }

    public BeanChangException(Throwable e) {
        // TODO Auto-generated constructor stub
        super(e);
    }

    public BeanChangException(String string, Throwable ex) {
        super(string, ex);
    }

    /**
     *
     */
    private static final long serialVersionUID = -4691479760145954711L;

}

