
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
      .then(res => res.json())
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
