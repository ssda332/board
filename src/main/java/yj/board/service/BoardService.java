package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import yj.board.repository.BoardRepository;
import yj.board.repository.CategoryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    @Qualifier("myBatisBoardRepository")
    private final BoardRepository boardRepository;



}
