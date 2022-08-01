package com.example.board18.controller;

import com.example.board18.entity.Board;
import com.example.board18.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import  org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


// 페이지 이동과 변수의 전달
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    //"/board/Write" 사이트로 들어갔을때 boardWrite html 문서 이동
    @GetMapping("/board/Write")
    public String boardWriteForm() {

        return "boardWrite";
    }

    //"board/WritePro" 사이트로 이동시 board 에 title, content 전송
    @PostMapping("board/WritePro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{

        if(board.getTitle() == ""){
            board.setTitle("제목 없음");
        }
        // board Service에 board,file 전달
        boardService.boardWrite(board, file);

        // 글 작성 시에 메세지, 이동할 페이지 전달
        model.addAttribute("message", "글 작성이 완료 되었습니다.");
        model.addAttribute("searchUrl", "/board/List");

        return "message";
    }

    //"board/WritePro" 사이트로 들어갔을때 boardList html 문서 이동
    //데이터 베이스에서 게시글에 대한 정보를 받아와서 넘겨준다.
    //Pageable로 페이징 처리
    //검색에 따라 페이징 처리 추가 설정
    @GetMapping("/board/List")
    public String boardList(Model model,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        String searchKeyword, String searchTag) {

        Page<Board> list = null;

        try {
            if(searchKeyword.isBlank()) {
                searchKeyword = null;
            }
        }
        catch (Exception e){

        }

        if (searchKeyword == null && searchTag == null){
            list = boardService.boardList(pageable);
        }
        else if(searchKeyword == null){
            list = boardService.boardSearchTag(searchTag, pageable);
        }
        else if(searchTag == null){
            list = boardService.boardSearchTitle(searchKeyword, pageable);
        }
        else {
            list = boardService.boardSearchTitleTag(searchKeyword, searchTag, pageable);
        }

        // 현재 페이지와 페이지의 범위 지정
        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        // 페이지, 계시글 정보 전달
        model.addAttribute( "list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardList";
    }

    // 특정 계시글 호출
    @GetMapping("/board/View") // localhost:8080/board/View?id=1
    public String boardView(Model model, Integer id) {

        model.addAttribute("board", boardService.boardView(id));
        return "boardView";
    }

    // 게시글 삭제
    @GetMapping("/board/delete")
    public String boardDelate (Integer id) {

        boardService.boardDelete(id);

        return "redirect:/board/List";
    }

    // 게시글 수정 페이지 이동
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("board", boardService.boardView(id));

        return "boardModify";
    }

    // 게시글 수정 적용
    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception {

        // 현재 board를 받아와서 저장
        Board boardTemp = boardService.boardView(id);

        if(board.getTitle() == ""){
            board.setTitle("제목 없음");
        }

        // 새로 받은 board에 덮어 쓰기
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.boardWrite(boardTemp, file);

        model.addAttribute("message", "글 수정이 완료 되었습니다.");
        model.addAttribute("searchUrl", "/board/List");

        return "message";
    }

}
