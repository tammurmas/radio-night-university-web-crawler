package org.tamm.nightuni.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.tamm.nightuni.repository.LectureRepository;
import org.tamm.nightuni.repository.dto.LectureDTO;
import org.tamm.nightuni.service.CrawlerService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LectureController {

    private final CrawlerService crawlerService;
    private final LectureRepository lectureRepository;

    @GetMapping("/lectures")
    public String lectures(Model model) {
        List<LectureDTO> lectures = lectureRepository.findAllByOrderByYearDesc().stream()
                .map(LectureDTO::ofLecture)
                .toList();
        model.addAttribute("lectures", lectures);
        return "lectures";
    }

    @PostMapping("/crawl")
    public String index() {
        crawlerService.crawl();
        return "index";
    }

}
