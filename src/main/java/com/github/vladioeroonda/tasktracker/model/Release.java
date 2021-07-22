package com.github.vladioeroonda.tasktracker.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "release")
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String version;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "finish_time", nullable = false)
    private LocalDateTime finishTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "release", orphanRemoval = true)
    private List<Task> tasks;

}
