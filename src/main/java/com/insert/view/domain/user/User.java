package com.insert.view.domain.user;

import com.insert.view.domain.user.authority.Authority;
import com.insert.view.domain.user.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import leehj050211.bsmOauth.dto.resource.BsmStudent;
import leehj050211.bsmOauth.dto.resource.BsmTeacher;
import leehj050211.bsmOauth.dto.resource.BsmUserResource;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String profile_image;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Min(2021)
    private Long enroll;

    @Min(1)
    @Max(3)
    private Short grade;

    @Min(1)
    @Max(4)
    private Short class_number;

    @Min(1)
    @Max(16)
    private Short student_number;

    public void setTeacherValue(BsmTeacher teacher) {
        this.name = teacher.getName();
        this.role = Role.TEACHER;
    }

    public void setStudentValue(BsmStudent student) {
        this.name = student.getName();
        this.role = Role.STUDENT;
        this.enroll = student.getEnrolledAt().longValue();
        this.grade = student.getGrade().shortValue();
        this.class_number = student.getClassNo().shortValue();
        this.student_number = student.getStudentNo().shortValue();
    }

    public void updateUserProfile(BsmUserResource resource) {
        this.id = resource.getUserCode();
        this.nickname = resource.getNickname();
        this.email = resource.getEmail();
        this.profile_image = resource.getProfileUrl();

        switch (resource.getRole()) {
            case STUDENT -> setStudentValue(resource.getStudent());
            case TEACHER -> setTeacherValue(resource.getTeacher());
        }
    }
}
