package no.fjordkraft.im.jobs.domain;

import java.sql.Timestamp;

public class JobHost {

    private long id;
    private String name;
    private Timestamp heartbeatTime;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeartbeatTime(Timestamp heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }
}
