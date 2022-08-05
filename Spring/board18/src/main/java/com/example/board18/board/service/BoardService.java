package com.example.board18.board.service;

import com.example.board18.board.entity.Board;
import com.example.board18.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BoardService {

    // BoardRepository를 읽어서 넣어줌
    @Autowired
    private BoardRepository boardRepository;

    // 현재 시간 생성
    private String timeNow(){
        Date now = new Date();
        SimpleDateFormat dayf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        return dayf.format(now).toString();
    }

    // 데이터 베이스에 board를 넣어줌
    // 글 작성
    public void boardWrite(Board board, MultipartFile multipartFile) throws Exception{

        // 파일 저장 경로
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        // 랜덤 UUID를 생성 하여 UUID_본래 파일 이름 으로 파일 이름 생성
        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + multipartFile.getOriginalFilename();

        // 파일이 비어있지 않을때, 파일에 파일 명, 파일 경로 저장
        if(multipartFile.getOriginalFilename() != ""){
            File saveFile = new File(projectPath, fileName);

            // 전달할 파일 저장
            multipartFile.transferTo(saveFile);

            // 데이터 베이스에 전달하기 위해 파일명과 파일 경로를 엔티티에 저장
            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
        }

        board.setDay(timeNow());
        boardRepository.save(board);
    }

    // 데이터 베이스에 board를 읽어옴
    // 계시글 가져오기
    public Page<Board> boardList(Pageable pageable) {

        return  boardRepository.findAll(pageable);
    }

    public  Page<Board> boardSearchTag(String searchTag, Pageable pageable){
        return  boardRepository.findByTagContaining(searchTag, pageable);
    }

    public Page<Board> boardSearchTitle(String searchKeyword, Pageable pageable){

        return  boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    public Page<Board> boardSearchTitleTag(String searchKeyword, String searchTag, Pageable pageable){
        return boardRepository.findByTitleAndTagContaining(searchKeyword, searchTag, pageable);
    }

    // 특정 계시글 가져오기
    public  Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    // 특정 계시글 삭제

    public void boardDelete(Integer id) {
        File file = new File(boardRepository.findById(id).get().getFilepath());

        file.delete();

        boardRepository.deleteById(id);
    }

}
