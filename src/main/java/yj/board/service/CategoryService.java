package yj.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yj.board.domain.article.Category;
import yj.board.domain.article.dto.CategoryDto;
import yj.board.domain.article.dto.CategoryDtoJpa;
import yj.board.domain.article.dto.CategoryEditDto;
import yj.board.repository.CategoryRepository;
import yj.board.repository.MybatisCategoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    @Qualifier("myBatisCategoryRepositoryImpl")
    private final MybatisCategoryRepository mybatisCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDtoJpa> findTopCategories() {
        List<Category> topCategories = categoryRepository.findTopCategories();

        List<CategoryDtoJpa> result = topCategories.stream()
                .map(CategoryDtoJpa::from)
                .collect(Collectors.toList());

        result.forEach(parent -> {
            filterActiveCategories(parent);
        });

        return result;
    }

    public void filterActiveCategories(CategoryDtoJpa category) {
        List<CategoryDtoJpa> activeChildren = new ArrayList<>();
        for (CategoryDtoJpa child : category.getChild()) {
            if (child.getCtgActivated() == 0) {
                filterActiveCategories(child); // 재귀적으로 자식 처리
                activeChildren.add(child);
            }
        }
        category.setChild(activeChildren);
    }

    @Transactional
    public ArrayList<CategoryEditDto> saveCategory_jpa(List<CategoryEditDto> dtos) {
        ArrayList<CategoryEditDto> resultDtos = new ArrayList<>();
        Map<String, Category> tempIdToCategoryMap = new HashMap<>();

        for (CategoryEditDto dto : dtos) {
            switch (dto.getStatus()) {
                case "c":
                    String ctgPrtId = dto.getCtgPrtId();
                    if (tempIdToCategoryMap.containsKey(ctgPrtId)) {
                        // 실제 부모의 ID값 set
                        dto.setCtgPrtId(Long.toString(tempIdToCategoryMap.get(ctgPrtId).getCtgId()));
                    }

                    Category addCategory = addCategory(dto);
                    tempIdToCategoryMap.put(dto.getCtgId(), addCategory);
                    resultDtos.add(convertToDto(addCategory));

                    break;
                case "u":
                    Category updateCategory = updateCategory(dto);
                    resultDtos.add(convertToDto(updateCategory));
                    break;
                case "d":
                    Category deleteCategory = deleteCategory(dto);
                    resultDtos.add(convertToDto(deleteCategory));
                    break;
                default:
//                    throw new IllegalStateException("Unexpected value: " + dto.getStatus());

            }
        }

        log.debug("{} rows saved", resultDtos.size());
        return resultDtos;
    }

    private Category addCategory(CategoryEditDto dto) {
        Category parent = categoryRepository.getById(Long.parseLong(dto.getCtgPrtId()));

        Category category = Category.builder()
                .ctgTitle(dto.getCtgTitle())
                .ctgHierachy(dto.getCtgHierachy())
                .ctgSort(dto.getCtgSort())
                .ctgActivated(0L)
                .parents(parent)
                // 부모 카테고리 설정 로직이 필요할 경우 여기에 추가
                .build();
        // 저장 후 DTO 변환 로직 필요
        Category savedCategory = categoryRepository.save(category);
        return savedCategory;
    }

    private Category updateCategory(CategoryEditDto dto) {
        Category category = categoryRepository.findById(Long.parseLong(dto.getCtgId()))
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + dto.getCtgId()));

        category.setCtgTitle(dto.getCtgTitle());
        category.setCtgHierachy(dto.getCtgHierachy());
        category.setCtgSort(dto.getCtgSort());
        // 부모 카테고리 업데이트가 필요할 경우 여기에 로직 추가

        Category updatedCategory = categoryRepository.save(category);
        return updatedCategory;
    }

    private Category deleteCategory(CategoryEditDto dto) {
        Category category = categoryRepository.findById(Long.parseLong(dto.getCtgId()))
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + dto.getCtgId()));

        category.setCtgActivated(1L);
        Category deletedCategory = categoryRepository.save(category);
        return deletedCategory;
    }

    private CategoryEditDto convertToDto(Category category) {
        return CategoryEditDto.builder()
                .ctgId(Long.toString(category.getCtgId()))
                .ctgTitle(category.getCtgTitle())
                .ctgHierachy(category.getCtgHierachy())
                .ctgSort(category.getCtgSort())
                .status("EXISTING") // 처리 후 상태를 나타내는 값으로 설정
                .build();
    }

    @Transactional(readOnly = true)
    public ArrayList<CategoryDto> findCategory(int type) {

        if (type == 1) {
            return mybatisCategoryRepository.findAllCategories();
        } else if (type == 2) {
            return mybatisCategoryRepository.findCanWrite();
        } else {
            return mybatisCategoryRepository.findAllCategories();
        }

    }

    @Transactional(readOnly = true)
    public ArrayList<CategoryEditDto> findAll_edit() {
        return mybatisCategoryRepository.findAll_edit();
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
                long newCtgId = mybatisCategoryRepository.selectNewCtgId();
                mapId.put(fakeId, newCtgId);

                // 부모 카테고리가 만들어졌으면 부모 카테고리ID 업데이트
                if (mapId.containsKey(category.getCtgPrtId())) category.setCtgPrtId(mapId.get(category.getCtgPrtId()) + "");
                category.setCtgId(Long.toString(newCtgId));
                mybatisCategoryRepository.insertCategory(category);

            } else if (category.getStatus().equals("u")) {
                mybatisCategoryRepository.updateCategory(category);
            } else if (category.getStatus().equals("d")) {
                mybatisCategoryRepository.deleteCategory(category);
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
