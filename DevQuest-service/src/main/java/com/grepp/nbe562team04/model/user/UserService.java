package com.grepp.nbe562team04.model.user;

import com.grepp.nbe562team04.model.achievement.AchievementService;
import com.grepp.nbe562team04.model.user.entity.UsersAchieve;
import com.grepp.nbe562team04.model.auth.code.Role;
import com.grepp.nbe562team04.model.auth.domain.Principal;
import com.grepp.nbe562team04.model.interest.InterestRepository;
import com.grepp.nbe562team04.model.interest.entity.Interest;
import com.grepp.nbe562team04.model.level.LevelRepository;
import com.grepp.nbe562team04.model.level.entity.Level;
import com.grepp.nbe562team04.model.user.dto.UserDto;
import com.grepp.nbe562team04.model.user.entity.User;

import com.grepp.nbe562team04.model.user.entity.UserInterest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final LevelRepository levelRepository;
    private final InterestRepository interestRepository;
    private final UserInterestRepository userInterestRepository;
    private final AchievementService achievementService;
    private final UsersAchieveRepository usersAchieveRepository;

    @Transactional
    public Long signup(UserDto dto, Role role) {
        User user = mapper.map(dto, User.class);
        Level defaultLevel = levelRepository.findFirstByOrderByLevelIdAsc()
                .orElseThrow(() -> new IllegalStateException("기본 레벨이 존재하지 않습니다."));

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setLevel(defaultLevel);
        user.setExp(0);
        user.setCreatedAt(LocalDate.now());
        user.setDeletedAt(null);

        User savedUser = userRepository.save(user);
        return savedUser.getUserId();
    }

    public User findByEmail(String email) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email));

        Level currentLevel = levelRepository.findTopByXpLessThanEqualOrderByXpDesc(user.getExp())
                .orElseThrow(() -> new IllegalStateException("레벨 데이터가 없습니다."));
        user.setLevel(currentLevel);
        return user;
    }

    public List<UsersAchieve> findAchieveByUserId(Long userId){

        return usersAchieveRepository.findTop3ByUser_UserIdOrderByAchievedAtDesc(userId);
    }
    public List<UsersAchieve> findAllAchieveByUserId(Long userId){

        return usersAchieveRepository.findByUser_UserIdOrderByAchievedAtDesc(userId);
    }

    @Transactional
    public void updateUser(String email, UserDto dto, MultipartFile file) throws IOException {
        log.info(">> 수정 요청 진입: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        user.setComment(dto.getComment());
        user.setNickname(dto.getNickname());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String hashed = passwordEncoder.encode(dto.getPassword());
            user.setPassword(hashed);
        }


        if (file != null && !file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/profile/";
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filepath = Paths.get(uploadDir, filename);
            Files.createDirectories(filepath.getParent());
            Files.write(filepath, file.getBytes());
            user.setUserImage(filename);
        }
        userRepository.save(user);
    }

    @Transactional
    public Map<String, List<UserDto>> findUsersGroupedByStatus() {
        List<UserDto> users = Optional.of(userRepository.findAll())
                .orElse(Collections.emptyList()).stream()
                .map(UserDto::new)
                .toList();

        List<UserDto> activeUsers = users.stream()
                .filter(user -> user.getDeletedAt() == null && !user.getRole().name().equals("ROLE_ADMIN"))
                .toList();

        List<UserDto> deletedUsers = users.stream()
                .filter(user -> user.getDeletedAt() != null)
                .toList();

        List<UserDto> adminUsers = users.stream()
                .filter(user -> user.getRole().name().equals("ROLE_ADMIN") && user.getDeletedAt() == null)
                .toList();

        Map<String, List<UserDto>> result = new HashMap<>();
        result.put("activeUsers", activeUsers);
        result.put("deletedUsers", deletedUsers);
        result.put("adminUsers", adminUsers);

        return result;
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("탈퇴했거나 존재하지 않는 사용자입니다."));

        return new Principal(user);
    }

    @Transactional
    public void softDeleteUser(String email) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
        user.setDeletedAt(LocalDate.now()); // 또는 LocalDateTime.now()
        userRepository.save(user);
    }


    public void receiveInterest(Long userId, Long roleId, List<Long> skillIds) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Interest role = interestRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직무입니다."));

        userInterestRepository.save(new UserInterest(user, role));

        for (Long skillId : skillIds) {
            Interest skill = interestRepository.findById(skillId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기술 ID입니다: " + skillId));

            userInterestRepository.save(new UserInterest(user, skill));
        }
    }

    public Boolean isDuplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void updateXpAndLevel(User user, int gainedXp) {
        user.addXp(gainedXp);

        List<Level> levels = levelRepository.findAll();
        levels.sort(Comparator.comparingInt(Level::getXp));

        for (Level level : levels) {
            if (user.getExp() >= level.getXp()) {
                user.setLevel(level);
            }
        }

        userRepository.save(user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails updatedUser = new Principal(user); // 새 User로 Principal 재생성
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
