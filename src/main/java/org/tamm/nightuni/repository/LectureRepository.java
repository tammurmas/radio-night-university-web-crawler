package org.tamm.nightuni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tamm.nightuni.model.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
