package it.unito.prog.models;

import java.io.Serializable;

public class Feedback implements Serializable {

    private int id;

    private String msg;

    private final Object result;

    public Feedback(int id, String msg) {
        this.id = id;
        this.msg = msg;
        result = null;
    }

    public Feedback(int id, String msg, Object result) {
        this.id = id;
        this.msg = msg;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public Object getResult() {
        return result;
    }

    public void setAll(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
