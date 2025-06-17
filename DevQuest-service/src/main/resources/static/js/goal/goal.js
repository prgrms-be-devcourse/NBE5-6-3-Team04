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
          }, 400);
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



document.addEventListener("DOMContentLoaded", () => {
  const inputBox = document.getElementById("userMessageInput");
  const chatHistory = document.getElementById("chatHistory");
  const modal = document.getElementById("chatModal");
  const openBtn = document.getElementById("openChatBtn");
  const closeBtn = document.getElementById("closeChatBtn");
  const personalityBtn = document.getElementById("togglePersonalityBtn");
  const personalityModal = document.getElementById("personalityModal");

  let isModalOpen = false;
  let personalityTimer = null;
  let selectedPersonality = null;

  // 채팅 입력 처리
  inputBox.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
      if (event.shiftKey) return;
      event.preventDefault();
      handleUserInput();
    }
    autoResizeTextarea(this);
  });

  document.getElementById("sendBtn").addEventListener("click", handleUserInput);

  function handleUserInput() {
    const message = inputBox.value.trim();
    if (message === "") return;

    appendMessage("user", message);
    sendAiMessage(message);
    inputBox.value = "";
    autoResizeTextarea(inputBox);
  }

  function sendAiMessage(message) {

    console.log("선택된 성격:", selectedPersonality);

    fetch("/api/ai/feedback", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        prompt: message,
        personality: selectedPersonality
      })
    })
    .then(res => res.json())
    .then(data => {
      appendMessage("ai", data.reply);
    })
    .catch(err => {
      console.error("AI 응답 실패", err);
      alert("AI 응답 중 오류발생");
    });
  }

  function appendMessage(sender, text) {
    if (!chatHistory) return;

    if (sender === "ai") {
      const wrapper = document.createElement("div");
      wrapper.classList.add("chat-message", "ai");

      const avatar = document.createElement("img");
      avatar.src = "/img/dev.png";
      avatar.alt = "AI 데브";
      avatar.className = "chat-avatar";

      const bubbleContainer = document.createElement("div");
      bubbleContainer.className = "chat-bubble-container";

      const name = document.createElement("span");
      name.className = "chat-name";
      name.textContent = "AI 데브";

      const bubble = document.createElement("div");
      bubble.className = "chat-bubble";
      bubble.textContent = text;

      bubbleContainer.appendChild(name);
      bubbleContainer.appendChild(bubble);
      wrapper.appendChild(avatar);
      wrapper.appendChild(bubbleContainer);
      chatHistory.appendChild(wrapper);
    } else {
      const p = document.createElement("p");
      p.classList.add("user");
      p.textContent = text;
      chatHistory.appendChild(p);
    }

    chatHistory.scrollTop = chatHistory.scrollHeight;
  }

  function autoResizeTextarea(el) {
    el.style.height = "auto";
    el.style.height = (el.scrollHeight + 2) + "px";
  }

  // 모달 열고 닫기
  openBtn.addEventListener("click", () => {
    if (!isModalOpen) {
      modal.style.display = "block";
      modal.style.animation = "slideUpRight 0.4s ease forwards";
      isModalOpen = true;
    } else {
      closeModalWithAnimation();
    }
  });

  closeBtn.addEventListener("click", closeModalWithAnimation);

  window.addEventListener("click", (e) => {
    if (
        isModalOpen &&
        !document.querySelector(".chat-modal-content").contains(e.target) &&
        e.target !== openBtn
    ) {
      closeModalWithAnimation();
    }
  });

  function closeModalWithAnimation() {
    modal.style.animation = "slideDownRight 0.3s ease forwards";
    modal.addEventListener("animationend", () => {
      modal.style.display = "none";
      isModalOpen = false;
    }, { once: true });
  }

  // MBTI 모달 hover 제어
  personalityBtn.addEventListener("mouseenter", () => {
    personalityModal.classList.add("visible");
  });

  personalityBtn.addEventListener("mouseleave", () => {
    personalityTimer = setTimeout(() => {
      personalityModal.classList.remove("visible");
    }, 400);
  });

  personalityModal.addEventListener("mouseenter", () => {
    clearTimeout(personalityTimer);
  });

  personalityModal.addEventListener("mouseleave", () => {
    personalityModal.classList.remove("visible");
  });

  // 성격 선택
  document.querySelectorAll(".personality-option").forEach(btn => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".personality-option").forEach(b => b.classList.remove("active"));
      btn.classList.add("active");
      selectedPersonality = btn.dataset.personality; // ✅ 중요: .textContent → .dataset.personality
      console.log("선택된 성격:", selectedPersonality);
    });
  });
});

// 선택된 성격 표시 UI 업데이트
const personalityDisplay = document.getElementById("selectedPersonalityDisplay");

document.querySelectorAll(".personality-option").forEach(btn => {
  btn.addEventListener("click", () => {
    document.querySelectorAll(".personality-option").forEach(b => b.classList.remove("active"));
    btn.classList.add("active");
    selectedPersonality = btn.dataset.personality;

    // ✅ 선택된 성격 UI에 표시
    personalityDisplay.textContent = `현재 선택된 성격: ${selectedPersonality}`;
  });
});

//===========================
