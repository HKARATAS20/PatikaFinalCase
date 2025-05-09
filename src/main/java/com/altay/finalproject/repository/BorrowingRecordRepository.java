package com.altay.finalproject.repository;

import com.altay.finalproject.model.entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, UUID> {
    List<BorrowingRecord> findByUser_Id(UUID userId);
    List<BorrowingRecord> findByBook_Id(UUID bookId);
}