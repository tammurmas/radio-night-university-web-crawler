package org.tamm.nightuni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tamm.nightuni.model.Lecture;
import org.tamm.nightuni.repository.LectureRepository;
import org.tamm.nightuni.service.CrawlerService;

import java.util.List;

@Controller
public class LectureController {

    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private LectureRepository lectureRepository;

    @GetMapping("/lectures")
    public String lectures(Model model) {
        model.addAttribute("lectures", lectureRepository.findAll());
        return "lectures";
    }

    /*@RequestMapping("/crawl")
    public String index(Model model) {
        crawlerService.crawl();
        return "Crawling finished";
        return "index";
    }*/

}
