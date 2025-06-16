function openModal() {
    document.getElementById("achievementListModal").style.display = "block";

    const list = document.getElementById("achievementList");
    list.innerHTML = "<p>불러오는 중...</p>";

    fetch("/member/achievements")
        .then(response => {
            if (!response.ok) {
                throw new Error("업적 데이터를 불러오지 못했습니다.");
            }
            return response.json();
        })
        .then(data => {
            const list = document.getElementById("achievementList");
            if (!Array.isArray(data)) {
                list.innerHTML = "<p>데이터 형식 오류</p>";
                return;
            }
            list.innerHTML = data.map(a => `
              <div class="achievement-item ${a.achieved ? 'achieved' : 'locked'}">
                    <span><strong>${a.name}</strong> : ${a.description}</span>
              </div>
            `).join("");
        })
        .catch(error => {
            console.error(error);
            document.getElementById("achievementList").innerHTML = "<p>업적 목록을 가져오는 데 실패했습니다.</p>";
        });
}

function outsideModalClick(event) {
    const modalContent = document.querySelector(".ccmModal-content");
    if (!modalContent.contains(event.target)) {
        closeListModal();
    }
}
function closeListModal() {
    const modal = document.getElementById("achievementListModal");
    if (modal) modal.style.display = "none";
}

