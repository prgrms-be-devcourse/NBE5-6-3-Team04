const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute(
    'content');
const csrfHeader = document.querySelector(
    'meta[name="_csrf_header"]').getAttribute('content');

function getCsrfHeaders() {
  return {
    [csrfHeader]: csrfToken
  };
}

// 이벤트 모음

// 목표 완료 이벤트
document.querySelectorAll('.goal-complete-btn').forEach(button => {
  button.addEventListener('click', handleGoalCompleteClick);
});

// 목표 완료 이벤트 핸들러
function handleGoalCompleteClick(event) {
  const goalId = event.currentTarget.getAttribute('data-id');

  if (confirm('정말 목표를 완료하시겠습니까?')) {
    goalComplete(goalId);
  }
}

// 목표 완료 함수
function goalComplete(goalId) {
  fetch(`/goals/${goalId}/complete`, {
    method: 'POST',
    headers: {...getCsrfHeaders()}
  })
      .then(response => response.json())
      .then(data => {
        if (data.leveledUp) {
          // 모달 띄우기
          // document.getElementById('levelUpModal').style.display = 'flex';
          // document.getElementById('levelUpText').innerText = `축하합니다! ${data.newLevelName}로 레벨업 했어요!`;
          showLevelUpModal(data.newLevelName, data.newLevelId);
          setTimeout(() => {
            document.getElementById('levelUpModal').style.display = 'none';
            location.reload();
          }, 4000);
        } else {
          alert('목표 완료! 경험치 +10');
          location.reload();
        }
      })
      .catch(error => {
        console.error('에러 발생:', error);
        alert('요청 중 오류가 발생했습니다.');
      });
}


const toggleBtn = document.getElementById('toggleCompletedBtn');
const completedSection = document.getElementById('completedGoalsSection');

toggleBtn.addEventListener('click', () => {
  const isVisible = completedSection.style.display === 'block';
  completedSection.style.display = isVisible ? 'none' : 'block';
  toggleBtn.textContent = isVisible ? '완료된 목표 보기' : '완료된 목표 숨기기';
});

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("goal-form");
  const todoForm = document.getElementById("todo-form");

  // goal 생성 이벤트
  document.querySelector(".goal-create-btn").addEventListener("click", () => {
    form.reset();
    form.removeAttribute("data-id");
    document.querySelector("#goalModal h2").textContent = "목표 추가";
    document.querySelector(
        "#goalModal button[type='submit']").textContent = "추가";
    form.onsubmit = function (e) {
      e.preventDefault();
      createGoal();
    };
    openModal("goalModal");
  });

  // goal 수정 이벤트
  document.querySelectorAll(".update-btn").forEach(btn => {
    btn.addEventListener("click", async () => {
      const goalId = btn.dataset.id;

      await fillGoalForm(goalId);

      form.setAttribute("data-id", goalId);
      document.querySelector("#goalModal h2").textContent = "목표 수정";
      document.querySelector(
          "#goalModal button[type='submit']").textContent = "수정";

      form.onsubmit = function (e) {
        e.preventDefault();
        updateGoal(e);
      };
      openModal("goalModal");
    });
  });

  // goal 삭제 이벤트
  document.querySelectorAll(".delete-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const goalId = btn.dataset.id;
      deleteGoal(goalId);
    });
  });

  // 목표 별 투두 생성 이벤트
  document.querySelectorAll(".add-todo-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const goalId = btn.dataset.goalId;
      const form = document.getElementById("todo-form");
      form.reset();
      form.goalId.value = goalId;

      document.querySelector("#todoModal h2").textContent = "todo 생성";
      document.querySelector(
          "#todoModal button[type='submit']").textContent = "추가";

      form.onsubmit = function (e) {
        e.preventDefault();
        createTodo();
      };

      openModal("todoModal");
    });
  });

  // 목표별 투두 수정 이벤트
  document.querySelectorAll(".update-todo-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const todoId = btn.dataset.id;
      fetch(`/todos/${todoId}/select`)
      .then(res => res.json())
      .then(data => {
        const form = document.getElementById("todo-form");
        form.reset();
        form.goalId.value = data.goalId; // 필요 시 포함
        form.title.value = data.content;
        form.startDate.value = data.startDate ?? "";
        form.endDate.value = data.endDate ?? "";
        form.isDone.value = data.isDone ? "true" : "false";

        document.querySelector("#todoModal h2").textContent = "todo 수정";
        document.querySelector(
            "#todoModal button[type='submit']").textContent = "수정";

        openModal("todoModal");

        form.onsubmit = function (e) {
          e.preventDefault();
          updateTodo(todoId);
        };
      })
      .catch(err => {
        console.error("투두 불러오기 실패", err);
        alert("수정할 데이터를 불러오지 못했습니다.");
      });
    });
  });

  // 목표별 투두 삭제 이벤트
  document.querySelectorAll(".delete-todo-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const todoId = btn.dataset.id;
      deleteTodo(todoId);
    });
  });

  // 투두 완료 toggle 이벤트
  document.querySelectorAll(".todo-checkbox").forEach(checkbox => {
    checkbox.addEventListener("change", () => {
      const todoId = checkbox.dataset.id;
      const isDone = checkbox.checked;
      toggleTodoStatus(todoId, isDone); // → 함수 분리!
    });
  });

});

// goal 생성 함수
function createGoal() {
  const form = document.getElementById("goal-form");
  const companyId = form.dataset.companyId; // 필요시 상위에서 설정
  const data = {
    companyId: companyId,
    title: form.title.value,
    startDate: form.startDate.value,
    endDate: form.endDate.value
  };

  fetch(`/goals/${companyId}/create`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...getCsrfHeaders()
    },
    body: JSON.stringify(data)
  }).then(res => {
    if (res.ok) {
      alert("등록 완료!");
      closeModal("goalModal");
      location.reload();
    } else {
      alert("등록 실패");
    }
  }).catch(err => {
    console.error(err);
    alert("에러 발생");
  });
}

// goal 수정 함수
function updateGoal(e) {
  const form = document.getElementById("goal-form");
  const goalId = form.dataset.id;

  const data = {
    title: form.title.value,
    startDate: form.startDate.value,
    endDate: form.endDate.value,
    isDone: form.isDone.value === "true"
  };

  fetch(`/goals/${goalId}/update`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      ...getCsrfHeaders()
    },
    body: JSON.stringify(data)
  }).then(res => {
    if (res.ok) {
      alert("수정 완료!");
      closeModal("goalModal");
      location.reload();
    } else {
      alert("수정 실패");
    }
  }).catch(err => {
    console.error(err);
    alert("수정 중 오류 발생");
  });
}

// goal 삭제 함수
function deleteGoal(goalId) {
  if (confirm("정말 삭제하시겠습니까?")) {
    fetch(`/goals/${goalId}/delete`, {
      method: 'DELETE',
      headers: {
        ...getCsrfHeaders()
      }
    }).then(res => {
      if (res.ok) {
        alert("삭제 완료!");
        location.reload();
      } else {
        alert("삭제 실패");
      }
    }).catch(err => {
      console.error(err);
      alert("에러 발생");
    });
  }
}

//goal 불러오기 함수
function fillGoalForm(goalId) {
  fetch(`/goals/${goalId}/select`)
  .then(res => res.json())
  .then(data => {
    const form = document.getElementById("goal-form");
    form.title.value = data.title ?? '';        // null-safe 처리 (에러 방지)
    form.startDate.value = data.startDate ?? '';
    form.endDate.value = data.endDate ?? '';
    form.isDone.value = data.isDone ? "true" : "false";
  })
  .catch(err => {
    console.error("목표 불러오기 실패", err);
    alert("수정할 데이터를 불러오지 못했습니다.");
  });
}

// 함수 모음

// 투두 생성 함수
function createTodo() {

  const form = document.getElementById("todo-form");
  const goalId = form.goalId.value;
  const content = form.title.value;

  const data = {
    goalId: goalId,
    content: content
  };

  fetch(`/todos/${goalId}/create`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...getCsrfHeaders()
    },
    body: JSON.stringify(data)
  })
  .then(res => {
    if (res.ok) {
      alert("투두 추가 완료!");
      closeModal("todoModal");
      location.reload();
    } else {
      alert("추가 실패");
    }
  })
  .catch(err => {
    console.error("에러 발생", err);
    alert("에러 발생");
  });
}

// 투두 수정 함수
function updateTodo(todoId) {
  const form = document.getElementById("todo-form");
  const content = form.title.value;

  const data = {
    content: form.title.value,
    startDate: form.startDate.value,
    endDate: form.endDate.value,
    isDone: form.isDone.value === "true"
  };

  fetch(`/todos/${todoId}/update`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      ...getCsrfHeaders()
    },
    body: JSON.stringify(data)
  })
  .then(res => {
    if (res.ok) {
      alert("수정 완료!");
      closeModal("todoModal");
      location.reload();
    } else {
      alert("수정 실패");
    }
  })
  .catch(err => {
    console.error("수정 중 오류 발생", err);
    alert("에러 발생");
  });
}

// 투두 삭제 함수
function deleteTodo(todoId) {
  if (confirm("정말 삭제하시겠습니까?")) {
    fetch(`/todos/${todoId}/delete`, {
      method: 'DELETE',
      headers: {
        ...getCsrfHeaders()
      }
    })
    .then(res => {
      if (res.ok) {
        alert("삭제 완료!");
        location.reload();
      } else {
        alert("삭제 실패");
      }
    })
    .catch(err => {
      console.error("삭제 중 오류 발생", err);
      alert("에러 발생");
    });
  }
}

// 투두 완료 toggle 함수
function toggleTodoStatus(todoId) {
  fetch(`/todos/${todoId}/toggle-check`, {
    method: 'POST',
    headers: {
      ...getCsrfHeaders()
    }
  })
      .then(res => {
        if (!res.ok) throw new Error("업적 확인 실패");
        return res.json();
      })
      .then(data => {
        if (data.achievementName) {
          const baseUrl = `${window.location.origin}${window.location.pathname}`;
          const redirectUrl = `${baseUrl}?achievementName=${encodeURIComponent(data.achievementName)}`;
          window.location.href = redirectUrl;
        } else {
          // 업적이 없는 경우는 단순 리로드
          location.reload();
        }
      })
      .catch(err => {
        console.error("체크박스 상태 변경 실패", err);
        alert("상태 변경 중 오류 발생");
      });
}

// goal modal 열기 함수
function openModal(goalModal) {
  document.getElementById(goalModal).style.display = "flex";
}

// goal modal 닫기 함수
function closeModal(goalModal) {
  document.getElementById(goalModal).style.display = "none";
}

function openTodoModal(goalId) {
  const form = document.getElementById("todo-form");
  form.reset();
  form.goalId.value = goalId;
  openModal("todoModal");
}

function closeTodoModal() {
  const form = document.getElementById("todo-form");
  form.reset();

  closeModal("todoModal");
}

//Gemini 답장 메시지

function sendAiMessage(message) {
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute(
      'content');
  const csrfHeader = document.querySelector(
      'meta[name="_csrf_header"]').getAttribute(
      'content');

  fetch("/api/ai/feedback", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      [csrfHeader]: csrfToken
    },
    body: JSON.stringify({prompt: message})
  })
  .then(res => res.json())
  .then(data => {
    document.getElementById("aiFeedBack").textContent = data.reply;
  })
  .catch(err => {
    console.error("AI 응답 실패", err);
    alert("AI 응답 중 오류발생");
  });
}


// 드롭다운(수정, 삭제)
document.addEventListener('DOMContentLoaded', () => {
  const toggles = document.querySelectorAll('.dropdown-toggle');

  toggles.forEach((toggle) => {
    toggle.addEventListener('click', (e) => {
      e.stopPropagation(); // 다른 곳 클릭 막기
      // 모든 드롭다운 닫기
      document.querySelectorAll('.dropdown-menu').forEach(menu => {
        if (menu !== toggle.nextElementSibling) {
          menu.style.display = 'none';
        }
      });
      // 현재 것만 토글
      const menu = toggle.nextElementSibling;
      menu.style.display = (menu.style.display === 'block') ? 'none' : 'block';

      const items = menu.querySelectorAll('li');
      items.forEach(item => {
        item.addEventListener('click', (e) => {
          e.stopPropagation(); // 수정, 삭제 눌러도 상세 진입 안 됨
        });
      });
    });
  });

  document.querySelectorAll('.dropdown-toggle, .dropdown-menu').forEach(el => {
    el.addEventListener('click', e => {
      e.stopPropagation();
    });
  });

  // 페이지 어디든 클릭하면 메뉴 닫힘
  document.addEventListener('click', () => {
    document.querySelectorAll('.dropdown-menu').forEach(menu => {
      menu.style.display = 'none';
    });
  });
});