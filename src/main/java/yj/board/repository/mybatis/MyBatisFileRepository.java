package yj.board.repository.mybatis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yj.board.domain.file.FileDto;
import yj.board.domain.file.FileWriteDto;
import yj.board.repository.FileRepository;
import yj.board.repository.mybatis.mapper.FileMapper;

import java.util.ArrayList;

@Repository
@RequiredArgsConstructor
public class MyBatisFileRepository implements FileRepository {

    private final FileMapper fileMapper;

    @Override
    public ArrayList<FileDto> findFile(String atcNum) {
        return fileMapper.findFile(atcNum);
    }

    @Override
    public void insertFile(FileWriteDto fileWriteDto) {
        fileMapper.insertFile(fileWriteDto);
    }

    @Override
    public void updateFile(FileDto file) {

    }

    @Override
    public void deleteFile(String atcNum) {
        fileMapper.deleteFile(atcNum);
    }
}
