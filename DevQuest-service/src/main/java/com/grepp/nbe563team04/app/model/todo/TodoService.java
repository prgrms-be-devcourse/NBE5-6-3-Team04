package com.grepp.nbe563team04.app.model.todo;

import com.grepp.nbe563team04.app.model.goal.entity.Goal;
import com.grepp.nbe563team04.app.model.goal.GoalRepository;
import com.grepp.nbe563team04.app.model.member.entity.Member;
import com.grepp.nbe563team04.app.model.problem.ProblemRepository;
import com.grepp.nbe563team04.app.model.problem.entity.Problem;
import com.grepp.nbe563team04.app.model.todo.dto.TodoRequestDto;
import com.grepp.nbe563team04.app.model.todo.dto.TodoResponseDto;
import com.grepp.nbe563team04.app.model.todo.entity.Todo;
import com.grepp.nbe563team04.app.model.userProblemSolve.UserProblemSolveRepository;
import com.grepp.nbe563team04.app.model.userProblemSolve.entity.UserProblemId;
import com.grepp.nbe563team04.app.model.userProblemSolve.entity.UserProblemSolve;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final GoalRepository goalRepository;
    private final ProblemRepository problemRepository;
    private final UserProblemSolveRepository userProblemSolveRepository;

    // 투두 생성
    @Transactional
    public void create(TodoRequestDto dto) {
        Goal goal = goalRepository.findById(dto.getGoalId()).orElseThrow(() -> new RuntimeException("해당 목표가 존재하지 않습니다."));


        Todo todo = Todo.builder()
                .goal(goal)
                .content(dto.getContent())
                .url(dto.getUrl())
                .startDate(dto.getStartDate() != null ? dto.getStartDate() : LocalDate.now())
                .endDate(dto.getEndDate())
                .isDone(dto.getIsDone() != null ? dto.getIsDone() : false)
                .sourceType("USER")
                .build();

        todoRepository.save(todo);
    }


    // 특정 목표(goalId)에 속한 투두 목록 조회
    @Transactional
    public List<TodoResponseDto> getTodosByGoal(Long goalId) {
        List<Todo> todos = todoRepository.findByGoalGoalId(goalId);

        List<TodoResponseDto> dtos = new ArrayList<>();

        for(Todo todo :  todos) {
            TodoResponseDto dto = TodoResponseDto.builder()
                    .todoId(todo.getTodoId())
                    .content(todo.getContent())
                    .url(todo.getUrl())
                    .startDate(todo.getStartDate())
                    .endDate(todo.getEndDate())
                    .isDone(todo.getIsDone())
                    .goalId(todo.getGoal().getGoalId())
                    .build();
            dtos.add(dto);
        }

        return dtos;
    }

    // 투두 수정
    @Transactional
    public void update(Long todoId, TodoRequestDto dto) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("해당 투두가 존재하지 않습니다."));

        todo.setUrl(dto.getUrl());
        todo.setContent(dto.getContent());
        todo.setStartDate(dto.getStartDate());
        todo.setEndDate(dto.getEndDate());
        todo.setIsDone(dto.getIsDone());

    }

    // 투두 삭제
    @Transactional
    public void delete(Long todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new RuntimeException("해당 투두가 존재하지 않습니다.");
        }
        todoRepository.deleteById(todoId);
    }

    // 투두 조회
    public TodoResponseDto getById(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("투두를 찾을 수 없습니다."));

        TodoResponseDto dto = TodoResponseDto.builder()
                .todoId(todo.getTodoId())
                .content(todo.getContent())
                .url(todo.getUrl())
                .startDate(todo.getStartDate())
                .endDate(todo.getEndDate())
                .isDone(todo.getIsDone())
                .goalId(todo.getGoal().getGoalId())
                .build();

        return dto;
    }

    @Transactional
    public void toggleStatus(Long todoId, Boolean isDone) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("해당 투두 없음"));
        todo.setIsDone(isDone);
        if (isDone) {
            todo.setEndDate(LocalDate.now());
        } else {
            todo.setEndDate(null);
        }
        todoRepository.save(todo);
    }

    @Transactional
    public void toggleCheck(Long todoId, Member member) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 투두가 존재하지 않습니다."));

        // 권한 체크
        if (!todo.getGoal().getCompany().getMember().getUserId().equals(member.getUserId())) {
            throw new SecurityException("해당 사용자의 투두가 아닙니다.");
        }

        // 체크 상태 토글
        todo.setIsDone(!todo.getIsDone());
        todoRepository.save(todo);

        // 문제 추천 모달에서 생성한 문제라면 문제 완료후 user_problem_solve에 값 저장하기
        if (todo.getIsDone() && "PROBLEM_RECOMMEND".equals(todo.getSourceType()) && todo.getProblem() != null) {

            Long userId = member.getUserId();
            Long problemId = todo.getProblem().getId();
            UserProblemId id = new UserProblemId(userId, problemId); // 복합키

            if (userProblemSolveRepository.existsById(id)) {
                UserProblemSolve existing = userProblemSolveRepository.findById(id).get();
                existing.setSolveCount(existing.getSolveCount() + 1);
                userProblemSolveRepository.save(existing);
            } else {
                userProblemSolveRepository.save(new UserProblemSolve(id, member, todo.getProblem(), 1));
            }
        }
    }


    @Transactional
    public void createFromProblems(Long goalId, List<Long> problemIds) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("해당 목표가 존재하지 않습니다."));

        List<Problem> problems = problemRepository.findAllById(problemIds);

        for (Problem problem : problems) {
            Todo todo = Todo.builder()
                    .goal(goal)
                    .problem(problem)
                    .content(problem.getTitle())
                    .url(problem.getUrl())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(0)) // 추천 문제로직을 통해 만들어지는 투두는 0일로 만들어짐
                    .isDone(false)
                    .sourceType("PROBLEM_RECOMMEND")
                    .build();

            todoRepository.save(todo);
        }
    }









}
