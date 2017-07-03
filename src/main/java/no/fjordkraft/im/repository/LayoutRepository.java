package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.Layout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by miles on 6/27/2017.
 */
@Repository
public interface LayoutRepository extends JpaRepository<Layout, Long> {
}