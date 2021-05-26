package com.ecnu.plantyclock.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ChenLu on 2021/5/21
 * Use for plant save
 */
public class Plant  extends DataSupport {
    private int id;
    private int num;
    private int progress;//根据成长情况取值，0-20为plant1，21-40为plant2，41-60为plant3，61-80为plant4，81-100为plant5

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}