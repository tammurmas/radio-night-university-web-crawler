package org.tamm.nightuni.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Lecture")
public class Lecture {

    public Lecture(String title, String author, String url, int year) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.year = year;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "url")
    private String url;

    @Column(name = "\"year\"")
    private Integer year;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private Date createdDate = new Date();

}
