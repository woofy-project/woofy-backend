package com.hmpr.woofy.board.repository;

import com.hmpr.woofy.board.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
