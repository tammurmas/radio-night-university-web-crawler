package org.tamm.nightuni.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tamm.nightuni.model.Lecture;
import org.tamm.nightuni.repository.LectureRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CrawlerServiceImpl implements CrawlerService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    private static final String NIGHT_UNIVERSITY_URL = "http://www.ylikool.ee/";
    private static final String LEFT_MENU_DIV_CSS_CLASS = "menuleft";
    private static final String CONTENT_DIV_CSS_CLASS = "content";

    @Autowired
    private LectureRepository lectureRepository;

    @Override
    public void crawl() {
        try {
            Document doc = Jsoup.connect(NIGHT_UNIVERSITY_URL).get();
            logger.info(doc.toString());
            Element lecturesDiv = doc.body().select("div."+ LEFT_MENU_DIV_CSS_CLASS).stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No element found with css class attribute: " + LEFT_MENU_DIV_CSS_CLASS));
            Elements links = lecturesDiv.getElementsByTag("a");
            for (Element link : links) {
                getLecturesByLink(link);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cound not connect to: " + NIGHT_UNIVERSITY_URL);
        }
    }

    private void getLecturesByLink(Element link) {
        String authorLinkHref = link.attr("href");
        String authorName = link.text();

        try {
            Document doc = Jsoup.connect(authorLinkHref).get();
            Element contentDiv = doc.body().select("div."+ CONTENT_DIV_CSS_CLASS).stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No element found with css class attribute: " + CONTENT_DIV_CSS_CLASS));
            List<Element> lectureLinks = contentDiv.getElementsByTag("a").stream()
                    .filter(lectureLink -> !lectureLink.text().equals("(mp3)") && !lectureLink.text().equals("(ram)"))
                    .collect(Collectors.toList());
            List<Lecture> lectures = lectureLinks.stream()
                    .map(lectureLink -> new Lecture(lectureLink.text(), authorName, lectureLink.attr("href")))
                    .collect(Collectors.toList());
            lectureRepository.saveAll(lectures);
            log.info("Saved {} new lectures", lectures.size());
        } catch (IOException e) {
            logger.warn("Could not get contents of: {}", authorLinkHref);
        }
    }
}
