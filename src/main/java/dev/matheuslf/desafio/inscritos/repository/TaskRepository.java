package dev.matheuslf.desafio.inscritos.repository;

import dev.matheuslf.desafio.inscritos.model.Task;
import dev.matheuslf.desafio.inscritos.model.enums.Priority;
import dev.matheuslf.desafio.inscritos.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
        SELECT t FROM Task t
            WHERE (:status IS NULL OR t.status = :status)
              AND (:priority IS NULL OR t.priority = :priority)
              AND (:projectId IS NULL OR t.project.id = :projectId)
    """)
    Page<Task> findByFilters(Pageable pageable,
                             @Param("status") Status status,
                             @Param("priority") Priority priority,
                             @Param("projectId") Long projectId
    );

}
