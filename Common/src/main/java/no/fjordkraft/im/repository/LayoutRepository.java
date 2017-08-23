package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.Layout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 6/27/2017.
 */
@Repository
public interface LayoutRepository extends JpaRepository<Layout, Long> {
}