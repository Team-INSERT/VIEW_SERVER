package com.insert.view.domain.register;

import com.insert.view.domain.register.status.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Register {
    @Id
    private Long studentNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime checkedDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String description;
}
