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

document.addEventListener("DOMContentLoaded", () => {
  // DOM 요소 초기화
  const inputBox = document.getElementById("userMessageInput");
  const chatHistory = document.getElementById("chatHistory");
  const modal = document.getElementById("chatModal");
  const openBtn = document.getElementById("openChatBtn");
  const closeBtn = document.getElementById("closeChatBtn");

  let isModalOpen = false;

  // 채팅 입력 처리 및 자동 리사이징
  inputBox.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
      if (event.shiftKey) return; // 줄바꿈 허용
      event.preventDefault();     // 기본 Enter 동작 막기
      handleUserInput();
    }
    autoResizeTextarea(this);
  });

  // 전송 버튼 클릭
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
    fetch("/api/ai/feedback", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ prompt: message })
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

  // 모달 열기
  openBtn.addEventListener("click", () => {
    if (!isModalOpen) {
      modal.style.display = "block";
      modal.style.animation = "slideUpRight 0.4s ease forwards";
      isModalOpen = true;
    } else {
      closeModalWithAnimation();
    }
  });

  // 모달 닫기
  closeBtn.addEventListener("click", closeModalWithAnimation);

  // 모달 바깥 클릭 시 닫기
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
    modal.addEventListener(
        "animationend",
        () => {
          modal.style.display = "none";
          isModalOpen = false;
        },
        { once: true }
    );
  }
});



// //Gemini 답장 메시지 전송
// function sendAiMessage(message) {
//   // const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
//   // const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
//
//   fetch("/api/ai/feedback", {
//     method: "POST",
//     headers: {
//       "Content-Type": "application/json",
//       // [csrfHeader]: csrfToken
//     },
//     body: JSON.stringify({ prompt: message })
//   })
//   .then(res => res.json())
//   .then(data => {
//     appendMessage("ai", data.reply);
//   })
//   .catch(err => {
//     console.error("AI 응답 실패", err);
//     alert("AI 응답 중 오류발생");
//   });
// }
//
// //채팅 입력 처리 및 자동 리사이징
// const inputBox = document.getElementById("userMessageInput");
// const chatHistory = document.getElementById("chatHistory");
//
// inputBox.addEventListener("keydown", function (event) {
//   if (event.key === "Enter") {
//     if (event.shiftKey) return; // 줄바꿈 허용
//     event.preventDefault();     // 기본 Enter 동작 막기
//     handleUserInput();
//   }
//   autoResizeTextarea(this);
// });
//
// function handleUserInput() {
//   const message = inputBox.value.trim();
//   if (message === "") return;
//
//   appendMessage("user", message);
//   sendAiMessage(message);
//   inputBox.value = "";
//   autoResizeTextarea(inputBox);
// }
//
// function appendMessage(sender, text) {
//   const chatHistory = document.getElementById("chatHistory");
//
//   if (sender === "ai") {
//     const wrapper = document.createElement("div");
//     wrapper.classList.add("chat-message", "ai"); // 전체 메시지 묶음
//
//     //프로필 이미지 왼쪽에 배치
//     const avatar = document.createElement("img");
//     avatar.src = "/img/dev.png";
//     avatar.alt = "AI 데브";
//     avatar.className = "chat-avatar";
//
//     // 말풍선 + 닉네임
//     const bubbleContainer = document.createElement("div");
//     bubbleContainer.className = "chat-bubble-container";
//
//     // 닉네임
//     const name = document.createElement("span");
//     name.className = "chat-name";
//     name.textContent = "AI 데브";
//
//     // 말풍선
//     const bubble = document.createElement("div");
//     bubble.className = "chat-bubble";
//     bubble.textContent = text;
//
//     // 말풍선 영역
//     bubbleContainer.appendChild(name);
//     bubbleContainer.appendChild(bubble);
//
//     // 전체 메시지
//     wrapper.appendChild(avatar);
//     wrapper.appendChild(bubbleContainer);
//
//     // 대화창에 추가
//     chatHistory.appendChild(wrapper);
//   } else {
//     // 사용자 메시지
//     const p = document.createElement("p");
//     p.classList.add("user");
//     p.textContent = text;
//     chatHistory.appendChild(p);
//   }
//
//   // 스크롤 항상 맨 아래로
//   chatHistory.scrollTop = chatHistory.scrollHeight;
// }
//
// // 모달 열기/닫기 애니메이션 처리
// document.addEventListener("DOMContentLoaded", () => {
//   const modal = document.getElementById("chatModal");
//   const openBtn = document.getElementById("openChatBtn");
//   const closeBtn = document.getElementById("closeChatBtn");
//
//   let isModalOpen = false;
//
//   // 버튼 클릭 시 토글
//   openBtn.addEventListener("click", () => {
//     if (!isModalOpen) {
//       modal.style.display = "block";
//       modal.style.animation = "slideUpRight 0.4s ease forwards";
//       isModalOpen = true;
//     } else {
//       closeModalWithAnimation();
//     }
//   });
//
//   // 닫기 버튼 기능
//   closeBtn.addEventListener("click", closeModalWithAnimation);
//
//   // 모달 바깥 클릭 시 닫기 (내부 클릭 시 닫히지 않게 처리 포함)
//   window.addEventListener("click", (e) => {
//     if (isModalOpen && !document.querySelector('.chat-modal-content').contains(e.target) && e.target !== openBtn) {
//       closeModalWithAnimation();
//     }
//   });
//
//   // 닫기 애니메이션 함수
//   function closeModalWithAnimation() {
//     modal.style.animation = "slideDownRight 0.3s ease forwards";
//     modal.addEventListener("animationend", () => {
//       modal.style.display = "none";
//       isModalOpen = false;
//     }, { once: true });
//   }
// });