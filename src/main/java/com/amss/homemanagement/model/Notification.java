package com.amss.homemanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    public Notification() {}

    public Notification(NotificationBuilder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.user = builder.user;
        this.task = builder.task;
    }

    public static class NotificationBuilder {

        private UUID id;
        private LocalDateTime date;
        private User user;
        private Task task;

        public NotificationBuilder() {}

        public NotificationBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public NotificationBuilder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public NotificationBuilder user(User user) {
            this.user = user;
            return this;
        }

        public NotificationBuilder task(Task task) {
            this.task = task;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
