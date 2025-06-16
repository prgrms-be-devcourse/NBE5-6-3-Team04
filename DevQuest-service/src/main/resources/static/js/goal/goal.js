

//  modal 닫기 함수 => 삭제 예정
function closeModal(Modal) {
  document.getElementById(Modal).style.display = "none";
}

// modal 관련 이벤트 리스너 모음

// '목표 생성' modal 열기 이벤트 리스너
document.addEventListener("DOMContentLoaded", function () {
  document.querySelector(".goal-create-btn").addEventListener("click", () => {
    const form = document.getElementById("goal-form");
    form.reset();
    form.removeAttribute("data-id");
    document.querySelector("#goalModal h2").textContent = "목표 추가";
    document.querySelector("#goalModal button[type='submit']").textContent = "추가";
    form.onsubmit = function (e) {
     e.preventDefault(); // 폼 제출시 페이지가 새로고침 되는것을 방지
     createGoal(); // 추가 버튼 눌렀을때 실행되는 함수
    };
    document.getElementById("goalModal").style.display = "flex";
  });
});

// '목표 생성' modal 닫기 이벤트 리스너
document.addEventListener("DOMContentLoaded", function () {
  const goalClose = document.getElementById("goalModalClose");
  goalClose.addEventListener("click", function () {
    const form = document.getElementById("goal-form");
    form.reset();
    document.getElementById("goalModal").style.display = "none";
  });
});

// '목표 수정' modal 열기 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".update-btn").forEach(btn => {
    btn.addEventListener("click", async () => {
      const goalId = btn.dataset.id;

      const form = document.getElementById("goal-form");

      await fillGoalForm(goalId);

      form.setAttribute("data-id", goalId);
      document.querySelector("#goalModal h2").textContent = "목표 수정";
      document.querySelector("#goalModal button[type='submit']").textContent = "수정";

      form.onsubmit = function (e) {
        e.preventDefault();
        updateGoal(goalId);
      };
      document.getElementById("goalModal").style.display = "flex";
    });
  });
});

//목표 불러오기  함수
function fillGoalForm(goalId) {
  fetch(`/goals/${goalId}/select`)
      .then(res => res.json())
      .then(data => {
        const form = document.getElementById("goal-form");
        form.title.value = data.title;        // null-safe 처리 (에러 방지)
        form.startDate.value = data.startDate ?? '';
        form.endDate.value = data.endDate ?? '';
        form.isDone.value = data.isDone ? "true" : "false";
        form.categoryName.value = data.categoryName;
      })
      .catch(err => {
        console.error("목표 불러오기 실패", err);
        alert("수정할 데이터를 불러오지 못했습니다.");
      });
}


// '투두 추가' modal 열기 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".add-todo-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const form = document.getElementById("todo-form");
      form.reset();
      form.goalId.value = btn.dataset.goalId;

      document.querySelector("#todoModal h2").textContent = "todo 생성";
      document.querySelector("#todoModal button[type='submit']").textContent = "추가";

      form.onsubmit = (e) => {
        e.preventDefault();
        createTodo();
      };

      document.getElementById("todoModal").style.display = "flex";
    });
  });
});

// '투두 추가' modal 닫기 이벤트 리스너
document.addEventListener("DOMContentLoaded", function () {
  const todoClose = document.getElementById("todoModalClose");
  todoClose.addEventListener("click", function () {
    const form = document.getElementById("todo-form");
    form.reset();
    document.getElementById("todoModal").style.display = "none";
  });
});


// '투두 수정' modal 열기 이벤트 리스너
document.querySelectorAll(".update-todo-btn").forEach(btn => {
  btn.addEventListener("click", async () => {
    const todoId = btn.dataset.id;

    try {
      await fillTodoForm(todoId);

      document.querySelector("#todoModal h2").textContent = "todo 수정";
      document.querySelector("#todoModal button[type='submit']").textContent = "수정";
      document.getElementById("todoModal").style.display = "flex";

      const form = document.getElementById("todo-form");
      form.onsubmit = (e) => {
        e.preventDefault();
        updateTodo(todoId);
      };

    } catch (err) {
      console.error("투두 불러오기 실패", err);
      alert("수정할 데이터를 불러오지 못했습니다.");
    }
  });
});


// 투두 불러오기 함수
function fillTodoForm(todoId) {
  return fetch(`/todos/${todoId}/select`)
      .then(res => res.json())
      .then(data => {
        const form = document.getElementById("todo-form");
        form.reset();
        form.goalId.value = data.goalId;
        form.title.value = data.content;
        form.url.value = data.url;
        form.startDate.value = data.startDate ?? "";
        form.endDate.value = data.endDate ?? "";
        form.isDone.value = data.isDone ? "true" : "false";
      });
}


// 추천 문제 생성 modal 열기 이벤트 리스너 = > 실행 함수 추가 필요
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".add-recommend-btn").forEach(btn => {
    btn.addEventListener("click", (event) => {
      const btn = event.currentTarget;
      const goalId = btn.dataset.goalId;
      const form = document.getElementById("problem-form");
      form.reset();
      form.goalId.value = goalId;

      document.querySelector("#problemModal h2").textContent = "추천 문제 생성";
      document.querySelector("#problemModal button[type='submit']").textContent = "추가";

      selectProblem(goalId);


      document.getElementById("problemModal").style.display = "flex";
    });
  });
});

// 추천 문제 생성 modal 닫기 이벤트 리스너
document.addEventListener("DOMContentLoaded", function () {
  const recommendClose = document.getElementById("recommendModalClose");
  recommendClose.addEventListener("click", function () {
    const form = document.getElementById("problem-form");
    form.reset();
    document.getElementById("problemModal").style.display = "none";
  });
});

// 추천 문제 생성 버튼 클릭 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("problem-form");

  form.onsubmit = function (e) {
    e.preventDefault();

    const checked = Array.from(form.querySelectorAll('input[name="problemId"]:checked'));
    const selectedIds = checked.map(cb => parseInt(cb.value));

    if (selectedIds.length === 0) {
      alert("문제를 하나 이상 선택하세요.");
      return;
    }

    const goalId = parseInt(form.goalId.value);

    fetch("/todos/from-problems", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        goalId: goalId,
        problemIds: selectedIds
      })
    })
        .then(res => {
          if (!res.ok) throw new Error("저장 실패");
          return res.text();
        })
        .then(() => {
          // alert("추천 문제 추가 완료!");
          document.getElementById("problemModal").style.display = "none";
          location.reload(); // 필요 시 새로고침
        })
        .catch(err => {
          console.error(err);
          alert("에러 발생: " + err.message);
        });
  };
});








// 목표 진행률 상 목표 완료 버튼 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
  const btn = document.querySelector('.goal-complete-btn');
  btn.addEventListener('click', () => {
    const goalId = btn.getAttribute('data-id');

    if (confirm('정말 목표를 완료하시겠습니까?')) {
      goalComplete(goalId);
    }
  });
});

// 목표 진행률 상 목표 완료 함수
function goalComplete(goalId) {
  fetch(`/goals/${goalId}/complete`, {
    method: 'POST',
    headers: {}
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


// 목표 삭제 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
  const btn = document.querySelector(".delete-btn");
  btn.addEventListener("click", () => {
    const goalId = btn.dataset.id;
    deleteGoal(goalId);
  });
});

// 투두 삭제 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
  const btn = document.querySelector(".delete-todo-btn");
  btn.addEventListener("click", () => {
    const todoId = btn.dataset.id;
    deleteTodo(todoId);
  });
});


// 목표 생성 함수
function createGoal() {
  const form = document.getElementById("goal-form");
  const companyId = form.dataset.companyId; // 필요시 상위에서 설정

  const data = {
    companyId: companyId,
    title: form.categoryName.value,
    categoryName: form.categoryName.value,
    startDate: form.startDate.value,
    endDate: form.endDate.value
  };

  fetch(`/goals/${companyId}/create`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
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

// 투두 생성 함수
function createTodo() {

  const form = document.getElementById("todo-form");
  const goalId = form.goalId.value;
  const content = form.title.value;
  const url = form.url.value;
  const startDate = form.startDate.value;

  const data = {
    goalId: goalId,
    content: content,
    url:url,
    startDate: startDate
  };

  fetch(`/todos/${goalId}/create`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
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



// 전역 변수로 상태 유지
let currentProblems = [];
let currentSort = { key: null, ascending: true };
let currentPage = 1;

// 추천 문제 조회 함수
function selectProblem() {
  fetch('/problem/select', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
      .then(res => res.json())
      .then(data => {
        currentProblems = data;
        renderProblemList(currentProblems);
        document.getElementById("problemModal").style.display = "block";
      })
      .catch(err => {
        console.error(err);
        alert("문제 목록 조회 실패");
      });
}

// 체크된 문제 기억용 전역 변수
let checkedProblemIds = new Set();

// 문제 리스트 렌더링 함수
function renderProblemList(problems) {
  const selectElement = document.getElementById("itemsPerPage");
  const searchInput = document.getElementById("problemSearch");
  const container = document.getElementById("problem-list");
  container.innerHTML = "";

  const itemsPerPage = parseInt(selectElement.value);

  //  검색 필터 적용
  const filtered = problems.filter(p =>
      p.title.toLowerCase().includes(searchInput.value.toLowerCase())
  );

  const start = (currentPage - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  const currentItems = filtered.slice(start, end);

  const table = document.createElement("table");
  table.className = "problem-table";

  table.innerHTML = `
    <thead>
      <tr>
        <th>선택</th>
        <th onclick="sortProblems('problemId')">문제 번호${renderSortIcon('problemId')}</th>
        <th onclick="sortProblems('site')">사이트${renderSortIcon('site')}</th>
        <th onclick="sortProblems('title')">문제 제목${renderSortIcon('title')}</th>
        <th onclick="sortProblems('level')">레벨${renderSortIcon('level')}</th>
        <th onclick="sortProblems('solveCount')">푼 횟수${renderSortIcon('solveCount')}</th>
      </tr>
    </thead>
    <tbody></tbody>
  `;

  const tbody = table.querySelector("tbody");

  currentItems.forEach(p => {
    const isChecked = checkedProblemIds.has(p.problemId); //  체크 상태 유지
    const row = document.createElement("tr");
    row.innerHTML = `
      <td><input type="checkbox" name="problemId" value="${p.problemId}" ${isChecked ? "checked" : ""}></td>
      <td>${p.problemId}</td>
      <td>${p.site}</td>
      <td>${p.title}</td>
      <td>${p.level}</td>
      <td>${p.solveCount}</td>
    `;

    const checkbox = row.querySelector("input[type='checkbox']");
    checkbox.addEventListener("change", () => {
      if (checkbox.checked) {
        checkedProblemIds.add(p.problemId);
      } else {
        checkedProblemIds.delete(p.problemId);
      }
    });

    tbody.appendChild(row);
  });

  container.appendChild(table);
  renderPagination(filtered.length, itemsPerPage);
}

// 페이지네이션 렌더링
function renderPagination(totalItems, itemsPerPage) {
  const pagination = document.createElement("div");
  pagination.className = "pagination";

  const totalPages = Math.ceil(totalItems / itemsPerPage);

  for (let i = 1; i <= totalPages; i++) {
    const btn = document.createElement("button");
    btn.textContent = i;
    btn.className = i === currentPage ? "active" : "";
    btn.addEventListener("click", () => {
      currentPage = i;
      renderProblemList(currentProblems);
    });
    pagination.appendChild(btn);
  }

  document.getElementById("problem-list").appendChild(pagination);
}

// 정렬 아이콘 렌더링
function renderSortIcon(key) {
  if (currentSort.key !== key) return '';
  return currentSort.ascending ? ' ▲' : ' ▼';
}

// 정렬 함수 전역 등록
window.sortProblems = function (key) {
  if (currentSort.key === key) {
    currentSort.ascending = !currentSort.ascending;
  } else {
    currentSort.key = key;
    currentSort.ascending = true;
  }

  currentProblems.sort((a, b) => {
    if (typeof a[key] === "string") {
      return currentSort.ascending
          ? a[key].localeCompare(b[key])
          : b[key].localeCompare(a[key]);
    } else {
      return currentSort.ascending
          ? a[key] - b[key]
          : b[key] - a[key];
    }
  });

  currentPage = 1;
  renderProblemList(currentProblems);
};

document.addEventListener("DOMContentLoaded", () => {
  const searchInput = document.getElementById("problemSearch");
  const selectElement = document.getElementById("itemsPerPage");
  const problemForm = document.getElementById("problem-form");

  //  검색 입력 이벤트
  if (searchInput) {
    searchInput.addEventListener("input", () => {
      currentPage = 1;
      renderProblemList(currentProblems);
    });
  }

  // 페이지당 항목 수 변경 이벤트
  if (selectElement) {
    selectElement.addEventListener("change", () => {
      currentPage = 1;
      renderProblemList(currentProblems);
    });
  }

  //  form 제출 이벤트
  if (problemForm) {
    problemForm.addEventListener("submit", e => {
      e.preventDefault();

      const goalId = parseInt(problemForm.goalId.value);
      const selectedIds = Array.from(checkedProblemIds);

      if (selectedIds.length === 0) {
        alert("문제를 하나 이상 선택하세요.");
        return;
      }

      fetch("/todos/from-problems", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          goalId: goalId,
          problemIds: selectedIds
        })
      })
          .then(res => {
            if (!res.ok) throw new Error("저장 실패");
            return res.text();
          })
          .then(() => {
            alert("추천 문제 추가 완료!");
            document.getElementById("problemModal").style.display = "none";
            location.reload();
          })
          .catch(err => {
            console.error(err);
            alert("에러 발생: " + err.message);
          });
    });
  }
});


// 목표 수정 함수
function updateGoal(goalId) {
  const form = document.getElementById("goal-form");

  const data = {
    title: form.categoryName.value,
    categoryName: form.categoryName.value,
    startDate: form.startDate.value,
    endDate: form.endDate.value,
    isDone: form.isDone.value === "true"
  };

  fetch(`/goals/${goalId}/update`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
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

// 투두 수정 함수
function updateTodo(todoId) {
  const form = document.getElementById("todo-form");
  const content = form.title.value;

  const data = {
    content: form.title.value,
    url: form.url.value,
    startDate: form.startDate.value,
    endDate: form.endDate.value,
    isDone: form.isDone.value === "true"
  };

  fetch(`/todos/${todoId}/update`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
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

// 목표 삭제 함수
function deleteGoal(goalId) {
  if (confirm("정말 삭제하시겠습니까?")) {
    fetch(`/goals/${goalId}/delete`, {
      method: 'DELETE',
      headers: {

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

// 투두 삭제 함수
function deleteTodo(todoId) {
  if (confirm("정말 삭제하시겠습니까?")) {
    fetch(`/todos/${todoId}/delete`, {
      method: 'DELETE',
      headers: {

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




// 투두 완료 체크 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll(".todo-checkbox").forEach(checkbox => {
    checkbox.addEventListener("change", () => {
      const todoId = checkbox.dataset.id;
      fetch(`/todos/${todoId}/toggle-check`, {
        method: 'POST',
        headers: {

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
              location.reload();
            }
          })
          .catch(err => {
            console.error("체크박스 상태 변경 실패", err);
            alert("상태 변경 중 오류 발생");
          });
    });
  });
});


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


// 버튼 함수

// 캘린더 보기 버튼
function showCalendar() {
  document.querySelector('#ongoingGoalsSection').style.display = 'none';
  document.querySelector('.calendar').style.display = 'flex';
  document.querySelector('#completedGoalsSection').style.display = 'none';

  setTimeout(() => {
    if (calendar) {
      calendar.render();
    } else {
      console.warn('calendar가 아직 초기화되지 않았음');
    }
  }, 0);

}

// 목표 보기 버튼
function showGoalList() {
  document.querySelector('#ongoingGoalsSection').style.display = 'flex';
  document.querySelector('.calendar').style.display = 'none';
  document.querySelector('#completedGoalsSection').style.display = 'none';


}

// 완료된 목표 보기 버튼
function showCompletedGoals() {
  document.querySelector('#ongoingGoalsSection').style.display = 'none';
  document.querySelector('.calendar').style.display = 'none';
  document.querySelector('#completedGoalsSection').style.display = 'flex';
}


// 목표진행률 상 완료 목표 버튼
document.addEventListener('DOMContentLoaded', () => {
  const toggleBtn = document.getElementById('toggleDoneGoalsBtn');
  let isHidden = false;

  if (toggleBtn) {
    toggleBtn.addEventListener('click', () => {
      const doneGoals = document.querySelectorAll('.goal-process .done-goal');
      doneGoals.forEach(goal => goal.classList.toggle('hidden'));
      isHidden = !isHidden;
      toggleBtn.textContent = isHidden ? '완료된 목표 보기' : '완료된 목표 숨기기';
    });
  }
});


// 기타


// full-calendar 관련 코드
let calendar;
document.addEventListener('DOMContentLoaded', function() {
  const companyId = document.querySelector('.calendar').dataset.companyid;

  var calendarEl = document.getElementById('calendar');
  calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: 'dayGridMonth',
    events: '/companies/' + companyId + '/events',
    eventClick: function(info) {
      info.jsEvent.preventDefault(); // ✅ 기본 링크 이동 막기
      window.open(info.event.url, '_blank');    // 새 창에서만 열리게
    }

  });
  // calendar.render();
});


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
      "Content-Type": "application/json"
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
