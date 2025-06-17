package com.grepp.nbe563team04.model.member;

import com.grepp.nbe563team04.infra.util.file.FileDto;
import com.grepp.nbe563team04.infra.util.file.FileUtil;
import com.grepp.nbe563team04.model.achievement.AchievementService;
import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.member.entity.MemberImage;
import com.grepp.nbe563team04.model.member.entity.MembersAchieve;
import com.grepp.nbe563team04.model.auth.code.Role;
import com.grepp.nbe563team04.model.auth.domain.Principal;
import com.grepp.nbe563team04.model.interest.InterestRepository;
import com.grepp.nbe563team04.model.interest.entity.Interest;
import com.grepp.nbe563team04.model.level.LevelRepository;
import com.grepp.nbe563team04.model.level.entity.Level;
import com.grepp.nbe563team04.model.member.dto.MemberDto;

import com.grepp.nbe563team04.model.member.entity.MemberInterest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final LevelRepository levelRepository;
    private final InterestRepository interestRepository;
    private final MemberInterestRepository memberInterestRepository;
    private final AchievementService achievementService;
    private final MembersAchieveRepository membersAchieveRepository;
    private final FileUtil fileUtil;

    // 활성 사용자 수 조회
    public long countActiveUsers() {
        return memberRepository.countByDeletedAtIsNull();
    }

    @Transactional
    public Long signup(MemberDto dto, Role role) {
        Member member = mapper.map(dto, Member.class);
        Level defaultLevel = levelRepository.findFirstByOrderByLevelIdAsc()
                .orElseThrow(() -> new IllegalStateException("기본 레벨이 존재하지 않습니다."));

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        member.setPassword(encodedPassword);
        member.setRole(role);
        member.setLevel(defaultLevel);
        member.setExp(0);
        member.setCreatedAt(LocalDate.now());
        member.setDeletedAt(null);

        Member savedMember = memberRepository.save(member);

        MemberImage defaultImage = new MemberImage();
        defaultImage.setMember(savedMember);
        defaultImage.setOriginFileName("default.png");
        defaultImage.setRenameFileName("default.png");
        defaultImage.setSavePath("/");
        defaultImage.setCreatedAt(LocalDateTime.now());
        defaultImage.setActivated(true);

        memberImageRepository.save(defaultImage);

        return savedMember.getUserId();
    }

    public Member findByEmail(String email) {

        Member member = memberRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email));

        Level currentLevel = levelRepository.findTopByXpLessThanEqualOrderByXpDesc(member.getExp())
                .orElseThrow(() -> new IllegalStateException("레벨 데이터가 없습니다."));
        member.setLevel(currentLevel);
        return member;
    }

    public List<MembersAchieve> findAchieveByUserId(Long userId){

        return membersAchieveRepository.findTop3ByMember_UserIdOrderByAchievedAtDesc(userId);
    }
    public List<MembersAchieve> findAllAchieveByUserId(Long userId){

        return membersAchieveRepository.findByMember_UserIdOrderByAchievedAtDesc(userId);
    }

    @Transactional
    public void updateUser(String email, MemberDto dto, List<MultipartFile> file) throws IOException {
        log.info(">> 수정 요청 진입: " + email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        member.setComment(dto.getComment());
        member.setNickname(dto.getNickname());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String hashed = passwordEncoder.encode(dto.getPassword());
            member.setPassword(hashed);
        }
        // 파일 처리
        if (file != null && !file.isEmpty()) {

            // 새 이미지 저장
            List<FileDto> fileDtos = fileUtil.upload(file, "profile");
            if (!fileDtos.isEmpty()) {
                // 기존 이미지 비활성화 (선택)
                List<MemberImage> oldImages = memberImageRepository.findByMemberAndActivatedTrue(member);
                for (MemberImage old : oldImages) {
                    old.setActivated(false);
                }
                memberImageRepository.saveAll(oldImages);

                FileDto fileDto = fileDtos.getFirst();

                MemberImage newImage = new MemberImage();
                newImage.setMember(member);
                newImage.setOriginFileName(fileDto.getOriginFileName());
                newImage.setRenameFileName(fileDto.getRenameFileName());
                newImage.setSavePath(fileDto.getSavePath());
                newImage.setCreatedAt(LocalDateTime.now());
                newImage.setActivated(true);

                if (!member.isProfile()){
                    member.setProfile(true);
                }

                memberImageRepository.save(newImage);
            }
        }

        memberRepository.save(member);
    }

    @Transactional
    public Map<String, List<MemberDto>> findMembersGroupedByStatus() {
        List<MemberDto> users = Optional.of(memberRepository.findAll())
            .orElse(Collections.emptyList()).stream()
            .map(MemberDto::new)
            .toList();

        List<MemberDto> activeUsers = users.stream()
            .filter(user -> user.getDeletedAt() == null && !user.getRole().name().equals("ROLE_ADMIN"))
            .toList();

        List<MemberDto> deletedUsers = users.stream()
            .filter(user -> user.getDeletedAt() != null)
            .toList();

        List<MemberDto> adminUsers = users.stream()
            .filter(user -> user.getRole().name().equals("ROLE_ADMIN") && user.getDeletedAt() == null)
            .toList();

        Map<String, List<MemberDto>> result = new HashMap<>();
        result.put("activeUsers", activeUsers);
        result.put("deletedUsers", deletedUsers);
        result.put("adminUsers", adminUsers);

        return result;
    }

    public boolean checkPassword(Member member, String rawPassword) {
        return passwordEncoder.matches(rawPassword, member.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("탈퇴했거나 존재하지 않는 사용자입니다."));

        return new Principal(member);
    }

    @Transactional
    public void softDeleteUser(String email) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
        member.setDeletedAt(LocalDate.now()); // 또는 LocalDateTime.now()
        memberRepository.save(member);
    }


    public void receiveInterest(Long userId, Long roleId, List<Long> skillIds) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Interest role = interestRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직무입니다."));

        memberInterestRepository.save(new MemberInterest(member, role));

        for (Long skillId : skillIds) {
            Interest skill = interestRepository.findById(skillId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기술 ID입니다: " + skillId));

            memberInterestRepository.save(new MemberInterest(member, skill));
        }
    }

    public Boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public void updateXpAndLevel(Member member, int gainedXp) {
        member.addXp(gainedXp);

        List<Level> levels = levelRepository.findAll();
        levels.sort(Comparator.comparingInt(Level::getXp));

        for (Level level : levels) {
            if (member.getExp() >= level.getXp()) {
                member.setLevel(level);
            }
        }

        memberRepository.save(member);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails updatedUser = new Principal(member); // 새 User로 Principal 재생성
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
