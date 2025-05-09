package com.altay.finalproject.repository;

import com.altay.finalproject.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    // Standard JPA method queries

    List<Book> findByTitleContainingIgnoreCase(String title);

    long countByGenre(Book.Genre genre);

    //List<Task> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // JPQL queries
    @Query("SELECT b FROM Book b WHERE b.genre = :genre ORDER BY b.author DESC")
    List<Book> findBooksByGenre(@Param("genre") Book.Genre genre);


 /*
    // Native SQL queries
    @Query(value = """
            SELECT * FROM books
            WHERE status != 'COMPLETED'
            AND priority_value >= :minPriority
            ORDER BY priority_value DESC, created_at ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<Task> findPriorityTasksToComplete(
            @Param("minPriority") int minPriority,
            @Param("limit") int limit);

    @Query(value = """
            SELECT 
                status,
                COUNT(*) as task_count,
                MIN(created_at) as oldest_task,
                MAX(created_at) as newest_task
            FROM tasks
            GROUP BY status
            """, nativeQuery = true)
    List<Object[]> getTaskStatusStatistics();
    */

}
