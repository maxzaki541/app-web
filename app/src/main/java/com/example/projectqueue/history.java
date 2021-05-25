package com.example.projectqueue;

public class history {
    private String queue_id,queue_date,queue_time,type_name;
    private String id,time,date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public history(String queue_id, String queue_date, String queue_time, String type_name, String id, String time, String date) {
        this.queue_id = queue_id;
        this.queue_date = queue_date;
        this.queue_time = queue_time;
        this.type_name = type_name;
        this.id = id;
        this.time = time;
        this.date = date;
    }

    public history() {

    }

    public history(String id,String queue_id, String queue_date, String queue_time,String type_name) {
        this.id = id;
        this.queue_id = queue_id;
        this.queue_date = queue_date;
        this.queue_time = queue_time;
        this.type_name = type_name;
    }

    public String getQueue_id() {
        return queue_id;
    }
    public String getID(){
        return this.id;
    }

    public void setQueue_id(String queue_id) {
        this.queue_id = queue_id;
    }

    public String getQueue_date() {
        return queue_date;
    }

    public void setQueue_date(String queue_date) {
        this.queue_date = queue_date;
    }

    public String getQueue_time() {
        return queue_time;
    }

    public void setQueue_time(String queue_time) {
        this.queue_time = queue_time;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
