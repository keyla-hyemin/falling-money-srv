package com.kkp.demo.repository;

import com.kkp.demo.dto.FallingMoneyHistory;
import org.springframework.data.repository.CrudRepository;

public interface FallingMoneyHistoryRepository extends CrudRepository<FallingMoneyHistory, String> {
}
