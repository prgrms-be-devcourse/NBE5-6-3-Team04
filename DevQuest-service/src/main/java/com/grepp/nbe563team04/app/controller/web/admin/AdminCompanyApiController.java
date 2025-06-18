package com.grepp.nbe563team04.app.controller.web.admin;

import com.grepp.nbe563team04.model.company.CompanyAliasRepository;
import com.grepp.nbe563team04.model.company.NormalizedCompanyRepository;
import com.grepp.nbe563team04.model.company.dto.CompanyAliasRequestDto;
import com.grepp.nbe563team04.model.company.entity.CompanyAlias;
import com.grepp.nbe563team04.model.company.entity.NormalizedCompany;
import com.grepp.nbe563team04.model.goalcompany.GoalCompanyRepository;
import com.grepp.nbe563team04.model.goalcompany.entity.GoalCompany;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/company")
public class AdminCompanyApiController {
    private final GoalCompanyRepository goalCompanyRepository;
    private final CompanyAliasRepository companyAliasRepository;
    private final NormalizedCompanyRepository normalizedCompanyRepository;

    // 기업 통계 데이터 API
    @GetMapping("/stats/data")
    public Map<String, Object> getCompanyStats() {
        Map<String, Long> companyStats = goalCompanyRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                GoalCompany::getCompanyName,
                Collectors.counting()
            ));
        List<Map.Entry<String, Long>> topCompanies = companyStats.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .toList();
        List<String> labels = topCompanies.stream().map(Map.Entry::getKey).toList();
        List<Long> data = topCompanies.stream().map(Map.Entry::getValue).toList();
        return Map.of("labels", labels, "data", data);
    }

    // 기업명 별칭 목록 조회
    @GetMapping("/stats/aliases")
    public List<CompanyAlias> getCompanyAliases() {
        return companyAliasRepository.findAll();
    }

    // 기업명 별칭 추가
    @PostMapping("/stats/aliases")
    public ResponseEntity<?> addCompanyAlias(@RequestBody CompanyAliasRequestDto dto) {
        try {
            NormalizedCompany company = normalizedCompanyRepository.findByNormalizedName(dto.getOriginalName())
                .orElseGet(() -> {
                    NormalizedCompany newCompany = new NormalizedCompany(dto.getOriginalName(), dto.getOriginalName());
                    return normalizedCompanyRepository.save(newCompany);
                });
            CompanyAlias alias = new CompanyAlias(dto.getAliasName(), company);
            CompanyAlias savedAlias = companyAliasRepository.save(alias);
            return ResponseEntity.ok(savedAlias);
        } catch (Exception e) {
            log.error("기업명 별칭 추가 실패", e);
            return ResponseEntity.badRequest().body("기업명 별칭 추가에 실패했습니다.");
        }
    }

    // 기업명 별칭 수정
    @PutMapping("/stats/aliases/{id}")
    public ResponseEntity<?> updateCompanyAlias(@PathVariable Long id, @RequestBody CompanyAliasRequestDto dto) {
        try {
            if (!companyAliasRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            NormalizedCompany company = normalizedCompanyRepository.findByNormalizedName(dto.getOriginalName())
                .orElseGet(() -> {
                    NormalizedCompany newCompany = new NormalizedCompany(dto.getOriginalName(), dto.getOriginalName());
                    return normalizedCompanyRepository.save(newCompany);
                });
            CompanyAlias alias = companyAliasRepository.findById(id).orElseThrow(() -> new RuntimeException("별칭이 존재하지 않습니다."));
            alias.setAliasName(dto.getAliasName());
            alias.setNormalizedCompany(company);
            CompanyAlias updatedAlias = companyAliasRepository.save(alias);
            return ResponseEntity.ok(updatedAlias);
        } catch (Exception e) {
            log.error("기업명 별칭 수정 실패", e);
            return ResponseEntity.badRequest().body("기업명 별칭 수정에 실패했습니다.");
        }
    }

    // 기업명 별칭 삭제
    @DeleteMapping("/stats/aliases/{id}")
    public ResponseEntity<?> deleteCompanyAlias(@PathVariable Long id) {
        try {
            if (!companyAliasRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            companyAliasRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("기업명 별칭 삭제 실패", e);
            return ResponseEntity.badRequest().body("기업명 별칭 삭제에 실패했습니다.");
        }
    }

    // 색상 수정 API
    @PutMapping("/colors/{id}")
    public ResponseEntity<?> updateCompanyColor(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String color = payload.get("color");
            NormalizedCompany company = normalizedCompanyRepository.findById(id).orElseThrow(() -> new RuntimeException("기업이 존재하지 않습니다."));
            company.setColor(color);
            NormalizedCompany updated = normalizedCompanyRepository.save(company);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("기업 색상 수정 실패", e);
            return ResponseEntity.badRequest().body("기업 색상 수정에 실패했습니다.");
        }
    }

    // 기업 삭제 API
    @DeleteMapping("/colors/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            if (!normalizedCompanyRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            normalizedCompanyRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("기업 삭제 실패", e);
            return ResponseEntity.badRequest().body("기업 삭제에 실패했습니다.");
        }
    }
}