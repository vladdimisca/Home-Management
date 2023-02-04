package com.amss.homemanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    @Column(name = "content")
    private String content;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Like> likes = new ArrayList<>();
}
