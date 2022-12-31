package org.tamm.nightuni.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tamm.nightuni.model.Lecture;
import org.tamm.nightuni.repository.LectureRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerServiceImpl implements CrawlerService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);

    private static final String NIGHT_UNIVERSITY_URL = "http://www.ylikool.ee/";
    private static final String LEFT_MENU_DIV_CSS_CLASS = "menuleft";
    private static final String CONTENT_DIV_CSS_CLASS = "content";
    private static final String URL_PART_OY = "/OY";
    private static final String TAG_NAME_A = "a";
    private static final String LINK_TEXT_MP_3 = "(mp3)";
    private static final String LINK_TEXT_RAM = "(ram)";
    private static final String ATTRIBUTE_HREF = "href";
    private static final String NUMERIC_PATTERN = "[^0-9]";

    private final LectureRepository lectureRepository;

    @Override
    public void crawl() {
        try {
            Document doc = Jsoup.connect(NIGHT_UNIVERSITY_URL).get();
            logger.debug(doc.toString());
            Element lecturesDiv = doc.body().select("div." + LEFT_MENU_DIV_CSS_CLASS).stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No element found with css class attribute: " + LEFT_MENU_DIV_CSS_CLASS));
            Elements links = lecturesDiv.getElementsByTag("a");
            List<Lecture> lectures = new ArrayList<>();
            for (Element link : links) {
                lectures.addAll(findLecturesByLink(link));
            }
            lectureRepository.saveAll(lectures);
            logger.info("Crawl finished, number of lectures={}", lectures.size());
        } catch (IOException e) {
            throw new RuntimeException("Could not connect to: " + NIGHT_UNIVERSITY_URL);
        }
    }

    private List<Lecture> findLecturesByLink(Element link) {
        List<Lecture> lectures = new ArrayList<>();
        String authorLinkHref = link.attr(ATTRIBUTE_HREF);
        String authorName = link.text();
        try {
            Document doc = Jsoup.connect(authorLinkHref).get();
            Element contentDiv = doc.body().select("div." + CONTENT_DIV_CSS_CLASS).stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No element found with CSS class attribute=" + CONTENT_DIV_CSS_CLASS));
            List<Element> lectureLinks = contentDiv.getElementsByTag(TAG_NAME_A).stream()
                    .filter(lectureLink -> !lectureLink.text().equals(LINK_TEXT_MP_3) && !lectureLink.text().equals(LINK_TEXT_RAM)).toList();
            lectures = lectureLinks.stream()
                    .map(lectureLink -> new Lecture(lectureLink.text(), authorName, lectureLink.attr(ATTRIBUTE_HREF),
                            getYearFromUrl(lectureLink.attr(ATTRIBUTE_HREF))))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.warn("Could not get contents of: {}", authorLinkHref);
        }
        return lectures;
    }

    private static Integer getYearFromUrl(String url) {
        logger.debug("Parsing URL={}", url);
        String[] urlParts = url.split(URL_PART_OY);
        Integer year = null;
        try {
            String onlyNumbers = RegExUtils.replaceAll(urlParts[1], NUMERIC_PATTERN, StringUtils.SPACE).trim();
            year = Integer.parseInt(onlyNumbers.substring(0, 4));
        } catch (NumberFormatException e) {
            logger.error("Failed to parse year from following URL={}", url);
        }
        return year;
    }

}
