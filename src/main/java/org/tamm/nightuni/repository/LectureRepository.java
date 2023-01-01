package org.tamm.nightuni.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tamm.nightuni.model.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    
    List<Lecture> findAllByOrderByYearDesc();
}
