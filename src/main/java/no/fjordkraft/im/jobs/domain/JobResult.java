package no.fjordkraft.im.jobs.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "jobresult")
public class JobResult {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "JOBRESULTSEQ")
    @SequenceGenerator(name = "JOBRESULTSEQ", sequenceName = "jobresult_id_seq")
    private Long id;

    @Column(name = "jobid")
    String jobId;

    @Column(name = "message")
    String message;

    @Column(name = "totalitemsprocessed")
    Long totalItemsProcessed;

    @Column(name = "totalitemserror")
    Long totalItemsError;

    @Column(name = "starttime")
    Timestamp startTime;

    @Column(name = "endtime")
    Timestamp endTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    JobResultStatus status;

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTotalItemsProcessed() {
        return totalItemsProcessed;
    }

    public void setTotalItemsProcessed(Long totalItemsProcessed) {
        this.totalItemsProcessed = totalItemsProcessed;
    }

    public Long getTotalItemsError() {
        return totalItemsError;
    }

    public void setTotalItemsError(Long totalItemsError) {
        this.totalItemsError = totalItemsError;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }


    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public JobResultStatus getStatus() {
        return status;
    }

    public void setStatus(JobResultStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "JobResult[" +
                "id=" + id +
                "jobid=" + jobId +
                ", message=" + message +
                ", totalItemsProcessed=" + totalItemsProcessed +
                ", totalItemsError=" + totalItemsError +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ']';
    }
}
