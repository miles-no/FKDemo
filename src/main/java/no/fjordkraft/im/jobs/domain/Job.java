package no.fjordkraft.im.jobs.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "im_job")
public class Job {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "obid")
    Long obid;

    @Column(name = "name")
	String name;

    @Column(name = "jobclass")
    String jobClass;

    @Column(name = "manualallowed")
    Boolean manualAllowed;

    @Column(name = "editallowed")
    Boolean editAllowed;

    @Column(name = "schedule")
    String schedule;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    JobStatus status;

    @Transient
    JobResult jobResult;


    public Job() {
	}

    public Job(Long obid, String name, String jobClass, Boolean manualAllowed,
               Boolean editAllowed, String schedule, JobStatus status) {
        this.obid = obid;
        this.name = name;
        this.jobClass = jobClass;
        this.manualAllowed = manualAllowed;
        this.editAllowed = editAllowed;
        this.schedule = schedule;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public Long getObid() {
        return obid;
    }

    public void setObid(Long obid) {
        this.obid = obid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Boolean getManualAllowed() {
        return manualAllowed;
    }

    public void setManualAllowed(Boolean manualAllowed) {
        this.manualAllowed = manualAllowed;
    }

    public Boolean getEditAllowed() {
        return editAllowed;
    }

    public void setEditAllowed(Boolean editAllowed) {
        this.editAllowed = editAllowed;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public JobStatus getStatus() {
        return status;
    }
    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public JobResult getJobResult() {
        return jobResult;
    }

    public void setJobResult(JobResult jobResult) {
        this.jobResult = jobResult;
    }

    @Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Job[");
		b.append("id=");
		b.append(id);
		b.append(", obid=");
		b.append(obid);
        b.append(", name=");
        b.append(name);
        b.append(", jobClass=");
        b.append(jobClass);
        b.append(", manualAllowed=");
        b.append(manualAllowed);
        b.append(", editAllowed=");
        b.append(editAllowed);
        b.append(", schedule=");
        b.append(schedule);
        b.append(", status=");
        b.append(status);
		b.append("]");
		return b.toString();
	}
}
