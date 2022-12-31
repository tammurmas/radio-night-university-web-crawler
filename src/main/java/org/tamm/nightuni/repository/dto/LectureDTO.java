package org.tamm.nightuni.repository.dto;

import lombok.Getter;
import lombok.Setter;
import org.tamm.nightuni.model.Lecture;

@Getter
@Setter
public class LectureDTO {

    private Long id;
    private String title;
    private String author;
    private String url;
    private Integer year;

    public static LectureDTO ofLecture(Lecture lecture) {
        LectureDTO lectureDTO = new LectureDTO();
        lectureDTO.setId(lecture.getId());
        lectureDTO.setTitle(lecture.getTitle());
        lectureDTO.setAuthor(lecture.getAuthor());
        lectureDTO.setUrl(lecture.getUrl());
        lectureDTO.setYear(lecture.getYear());

        return lectureDTO;
    }

}
