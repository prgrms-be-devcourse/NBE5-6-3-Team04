package com.grepp.nbe563team04.app.controller.web.admin;

import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.member.MemberService;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.goalcompany.GoalCompanyRepository;
import com.grepp.nbe563team04.model.company.CompanyNormalizationService;
import com.grepp.nbe563team04.model.company.CompanyAliasRepository;
import com.grepp.nbe563team04.model.company.entity.CompanyAlias;
import com.grepp.nbe563team04.model.company.dto.CompanyAliasRequestDto;
import com.grepp.nbe563team04.model.company.entity.NormalizedCompany;
import com.grepp.nbe563team04.model.company.NormalizedCompanyRepository;
import com.grepp.nbe563team04.model.goalcompany.GoalCompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("admin/company")
public class AdminCompanyController {

    private final MemberService memberService;
    private final GoalCompanyRepository goalCompanyRepository;
    private final CompanyNormalizationService companyNormalizationService;
    private final CompanyAliasRepository companyAliasRepository;
    private final NormalizedCompanyRepository normalizedCompanyRepository;
    private final GoalCompanyService goalCompanyService;

    // 기업 통계 데이터 API
    @GetMapping("/stats/data")
    @ResponseBody
    public Map<String, Object> getCompanyStats() {
        // 기업별 지원자 수 통계
        Map<String, Long> companyStats = goalCompanyRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                company -> company.getCompanyName(),
                Collectors.counting()
            ));

        // 상위 10개 기업만 추출
        List<Map.Entry<String, Long>> topCompanies = companyStats.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .collect(Collectors.toList());

        // 차트 데이터 포맷팅
        List<String> labels = topCompanies.stream()
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        List<Long> data = topCompanies.stream()
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());

        return Map.of(
            "labels", labels,
            "data", data
        );
    }

    // 기업명 별칭 관리 페이지
    @GetMapping("/stats/aliases")
    public String showCompanyAliasesPage(
        @AuthenticationPrincipal Principal principal,
        Model model,
        CsrfToken csrfToken) {

        // 로그인한 관리자 정보
        Member admin = memberService.findByEmail(principal.getUsername());
        model.addAttribute("nickname", principal.getMember().getNickname());

        // 모든 기업명 별칭 목록 조회
        List<CompanyAlias> aliases = companyAliasRepository.findAll();
        model.addAttribute("aliases", aliases);

        return "admin/company-aliases";
    }

    // 기업명 별칭 추가
    @PostMapping("/stats/aliases")
    @ResponseBody
    public ResponseEntity<?> addCompanyAlias(@RequestBody CompanyAliasRequestDto dto) {
        try {
            // originalName으로 NormalizedCompany 조회, 없으면 생성
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

    // 기업명 별칭 수정 (DTO 기반)
    @PutMapping("/stats/aliases/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCompanyAlias(@PathVariable Long id, @RequestBody CompanyAliasRequestDto dto) {
        try {
            if (!companyAliasRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            // originalName으로 NormalizedCompany 조회, 없으면 생성
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
    @ResponseBody
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

    // 기업 관리 페이지 (5개만 리스팅)
    @GetMapping("/colors")
    public String showCompanyColorsPage(@AuthenticationPrincipal Principal principal, Model model, CsrfToken csrfToken) {
        Member admin = memberService.findByEmail(principal.getUsername());
        model.addAttribute("nickname", principal.getMember().getNickname());
        List<NormalizedCompany> companies = normalizedCompanyRepository.findAll().stream().limit(5).collect(Collectors.toList());
        model.addAttribute("companies", companies);
        return "admin/company-colors";
    }

    // 색상 수정 API
    @PutMapping("/colors/{id}")
    @ResponseBody
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
    @ResponseBody
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

    // 기존 DB에 저장된 GoalCompany의 companyName을 정규화된 값으로 일괄 업데이트하는 API
    @PostMapping("/normalize-all")
    @ResponseBody
    public ResponseEntity<String> normalizeAllGoalCompanyNames() {
        // GoalCompanyService의 일괄 정규화 메서드 호출
        goalCompanyService.normalizeAllGoalCompanyNames();
        return ResponseEntity.ok("모든 GoalCompany 기업명 정규화 완료");
    }
}
