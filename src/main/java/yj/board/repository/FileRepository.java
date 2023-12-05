package yj.board.repository;

import yj.board.domain.file.FileDto;
import yj.board.domain.file.FileWriteDto;

import java.util.ArrayList;

public interface FileRepository {

    ArrayList<FileDto> findFile(String atcNum);
    void insertFile(FileWriteDto file);
    void updateFile(FileDto file);
    void deleteFile(String atcNum);

}
