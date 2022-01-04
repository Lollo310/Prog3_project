package it.unito.prog.models;

import java.io.Serializable;

public class Feedback implements Serializable {

    private int id;

    private String msg;

    public Feedback(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    private int getId() {
        return this.id;
    }

    private String getMsg() {
        return this.msg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
