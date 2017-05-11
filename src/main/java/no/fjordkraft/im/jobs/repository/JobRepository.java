package no.fjordkraft.im.jobs.repository;


import no.fjordkraft.im.jobs.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByJobClass(String jobClass);

    Job findByObid(long obId);
}
