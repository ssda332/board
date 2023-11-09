package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.board.domain.board.dto.CategoryDto;
import yj.board.domain.board.dto.CategoryEditDto;
import yj.board.repository.CategoryRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    @Qualifier("myBatisCategoryRepository")
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ArrayList<CategoryDto> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ArrayList<CategoryEditDto> findAll_edit() {
        return categoryRepository.findAll_edit();
    }

    @Transactional
    public ArrayList<CategoryEditDto> saveCategory(List<CategoryEditDto> data) {

        categorySort(data);
        HashMap<String, Long> mapId = new HashMap<>();

        for (CategoryEditDto category : data) {
            if (category.getStatus().equals("c")) {
                // create
                // 0. 정렬 status, ctgId로
                // 1. 시퀀스 값 select
                // 2. new_xx와 같은 id,prtid값 update
                // 3. insert table
                String fakeId = category.getCtgId();
                long newCtgId = categoryRepository.selectNewCtgId();
                mapId.put(fakeId, newCtgId);

                // 부모 카테고리가 만들어졌으면 부모 카테고리ID 업데이트
                if (mapId.containsKey(category.getCtgPrtId())) category.setCtgPrtId(mapId.get(category.getCtgPrtId()) + "");
                category.setCtgId(Long.toString(newCtgId));
                categoryRepository.insertCategory(category);

            } else if (category.getStatus().equals("u")) {
                categoryRepository.updateCategory(category);
            } else if (category.getStatus().equals("d")) {
                categoryRepository.deleteCategory(category);
            }
        }

        return findAll_edit();
    }

    // 정해진 순서에 맞게 카테고리 변경사항을 적용하기 위한 정렬
    private static void categorySort(List<CategoryEditDto> data) {
        Collections.sort(data, (o1, o2) -> {
            // 비교 대상인 두 객체의 status 값을 비교
            int statusComparison = getStatusPriority(o1.getStatus()) - getStatusPriority(o2.getStatus());

            if (statusComparison != 0) {
                return statusComparison; // status가 "c"인 경우 우선 정렬
            }

            // status가 "c"인 경우, ctgId에서 숫자 부분을 추출하여 비교
            int o1Number = extractNumber(o1.getCtgId());
            int o2Number = extractNumber(o2.getCtgId());

            return Integer.compare(o1Number, o2Number);
        });
    }

    // "status" 우선순위를 지정하는 메서드
    private static int getStatusPriority(String status) {
        if (status.equals("c")) {
            return 1; // "c"인 경우 가장 우선순위
        } else if (status.equals("")) {
            return 3; // ""(빈 문자열)인 경우 가장 후순위
        } else {
            return 2; // 그 외의 경우 중간 우선순위
        }
    }

    // 정규표현식을 사용하여 문자열에서 숫자 부분을 추출
    private static int extractNumber(String text) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String numberStr = matcher.group();
            return Integer.parseInt(numberStr);
        }

        return 0; // 숫자가 없는 경우 0으로 간주
    }





}
