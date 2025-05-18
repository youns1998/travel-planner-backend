package com.travelplanner.backend.repository;

import com.travelplanner.backend.domain.Schedule;
import com.travelplanner.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByUserOrderByOrderIndex(User user);

    void deleteByUser(User user);
}
