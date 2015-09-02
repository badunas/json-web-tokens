package org.badun.jwtdemo.web.model;

import java.util.Date;

/**
 * Created by Artsiom Badun.
 */
public class HeartBeatModel implements Model {
    private String status = "ALIVE";
    private Date date = new Date();

    public HeartBeatModel() {
    }

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }
}
