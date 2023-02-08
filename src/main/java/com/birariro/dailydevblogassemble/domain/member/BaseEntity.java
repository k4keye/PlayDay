package com.birariro.dailydevblogassemble.domain.member;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(name = "create_at",updatable = false)
    private LocalDateTime createAt;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    public BaseEntity setState(State state) {
        this.state = state;
        return this;
    }
}