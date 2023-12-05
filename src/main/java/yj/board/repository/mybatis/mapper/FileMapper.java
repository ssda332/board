package yj.board.repository.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import yj.board.domain.file.FileDto;
import yj.board.domain.file.FileWriteDto;

import java.util.ArrayList;

@Mapper
public interface FileMapper {

    ArrayList<FileDto> findFile(String atcNum);
    void insertFile(FileWriteDto fileWriteDto);
    void deleteFile(String atcNum);

}
