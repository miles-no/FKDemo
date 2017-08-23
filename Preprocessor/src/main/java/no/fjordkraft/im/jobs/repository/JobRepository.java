package no.fjordkraft.im.jobs.repository;


import no.fjordkraft.im.jobs.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByJobClass(String jobClass);

    Job findByObid(long obId);
}
