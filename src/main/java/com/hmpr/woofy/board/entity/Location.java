package com.hmpr.woofy.board.entity;

import com.hmpr.woofy.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @OneToOne
    @JoinColumn(name="board_id")
    private Board board;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "detail")
    private String detail;

}
