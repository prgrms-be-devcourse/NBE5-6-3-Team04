package com.grepp.nbe563team04.model.company;

import com.grepp.nbe563team04.model.company.entity.CompanyAlias;
import com.grepp.nbe563team04.model.company.entity.NormalizedCompany;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyNormalizationService {

    private final NormalizedCompanyRepository normalizedCompanyRepository;
    private final CompanyAliasRepository companyAliasRepository;

    // 기업명 정규화를 위한 매핑 규칙
    private static final Map<String, String> COMPANY_MAPPINGS = new HashMap<>();
    static {
        // 주요 기업 매핑 규칙
        COMPANY_MAPPINGS.put("toss", "토스");
        COMPANY_MAPPINGS.put("toss corp", "토스");
        COMPANY_MAPPINGS.put("toss corporation", "토스");
        COMPANY_MAPPINGS.put("toss 주식회사", "토스");
        COMPANY_MAPPINGS.put("naver", "네이버");
        COMPANY_MAPPINGS.put("kakao", "카카오");
        COMPANY_MAPPINGS.put("baemin", "배달의민족");
        COMPANY_MAPPINGS.put("배민", "배달의민족");
        COMPANY_MAPPINGS.put("danggeun", "당근");
        // 추가 기업 매핑 규칙은 여기에 계속 추가
    }

    /**
     * 입력된 기업명을 정규화하여 표준 기업명을 반환합니다.
     * GoalCompany와는 독립적으로 동작하며, 단순히 기업명 정규화만 담당합니다.
     *
     * @param inputCompanyName 사용자가 입력한 기업명
     * @return 정규화된 기업명 (표준 기업명)
     */
    public String normalizeCompanyName(String inputCompanyName) {
        if (inputCompanyName == null || inputCompanyName.trim().isEmpty()) {
            throw new IllegalArgumentException("기업명은 비어있을 수 없습니다.");
        }

        // 1. 입력값 정규화 (소문자 변환, 공백 제거)
        String normalizedInput = normalizeInput(inputCompanyName);

        // 2. DB에서 정규화된 기업명 검색
        Optional<NormalizedCompany> existingCompany = normalizedCompanyRepository.findByNormalizedName(normalizedInput);
        if (existingCompany.isPresent()) {
            return existingCompany.get().getStandardName();
        }

        // 3. 별칭으로 검색
        Optional<CompanyAlias> existingAlias = companyAliasRepository.findByAliasName(normalizedInput);
        if (existingAlias.isPresent()) {
            return existingAlias.get().getNormalizedCompany().getStandardName();
        }

        // 4. 매핑 규칙에서 검색
        String standardName = COMPANY_MAPPINGS.get(normalizedInput);
        if (standardName != null) {
            // 새로운 정규화된 기업명과 별칭 저장
            saveNewNormalizedCompany(normalizedInput, standardName, inputCompanyName);
            return standardName;
        }

        // 5. 새로운 기업명으로 처리
        saveNewNormalizedCompany(normalizedInput, inputCompanyName, inputCompanyName);
        return inputCompanyName;
    }

    /**
     * 입력값을 정규화합니다.
     * - 소문자 변환
     * - 공백 제거
     * - 특수문자 제거
     */
    private String normalizeInput(String input) {
        return input.toLowerCase()
            .trim()
            .replaceAll("\\s+", "")  // 모든 공백 제거
            .replaceAll("[^a-z0-9가-힣]", ""); // 영문, 숫자, 한글만 남기고 제거
    }

    /**
     * 새로운 정규화된 기업명과 별칭을 저장합니다.
     */
    @Transactional
    protected void saveNewNormalizedCompany(String normalizedName, String standardName, String originalInput) {
        NormalizedCompany company = new NormalizedCompany(normalizedName, standardName);
        normalizedCompanyRepository.save(company);

        // 원본 입력값을 별칭으로 저장
        if (!originalInput.equals(normalizedName)) {
            CompanyAlias alias = new CompanyAlias(originalInput, company);
            companyAliasRepository.save(alias);
        }
    }

    /**
     * 기업명 매핑 규칙을 추가합니다.
     * 새로운 기업이 추가될 때 이 메서드를 통해 매핑 규칙을 추가할 수 있습니다.
     */
    @Transactional
    public void addCompanyMapping(String normalizedName, String standardName) {
        COMPANY_MAPPINGS.put(normalizedName, standardName);
    }
} 