package com.example.board18.board.repository;

import com.example.board18.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 데이터 베이스와의 연동
@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>{

    //findBy[Title]Containing  title에서 포함된 요소를 가진것만 찿기
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
    //findBy[Tag]Containing  tag에서 포함된 요소를 가진것만 찿기
    Page<Board> findByTagContaining(String searchTag, Pageable pageable);

    Page<Board> findByTitleAndTagContaining(String searchKeyword, String searchTag, Pageable pageable);
}
