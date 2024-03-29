package com.hmpr.woofy.board.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "title")
    private String title;

    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registrationDate;

    @Column(name = "execution_date")
    private LocalDate meetingDate;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "image_id")
    private Long imageId;

}
