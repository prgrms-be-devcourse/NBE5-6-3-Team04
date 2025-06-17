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

            const thicknessLayers = Array.from({ length: 7 }, (_, i) => {
                const offset = -3 + i;
                return `<div class="badge-layer" style="transform: translateZ(${offset}px);"></div>`;
            }).join("");

            list.innerHTML = `
              <div class="achievement-grid">
                ${data.map((a, index) => `
                  <div class="achievement-card ${a.achieved ? 'achieved' : 'locked'}" 
                       onclick="showAchievementDetail('${escapeHtml(a.name)}', '${escapeHtml(a.description)}', ${a.achieved}, event)">
                    <div class="badge" style="--img-url: url('${a.imageUrl}');" title="${a.name}">
                      ${a.achieved ? thicknessLayers : ""}
                    </div>
                    <p>${a.name}</p>
                  </div>
                `).join("")}
              </div>
            `;
        })
        .catch(error => {
            console.error(error);
            document.getElementById("achievementList").innerHTML = "<p>업적 목록을 가져오는 데 실패했습니다.</p>";
        });
}

function showAchievementDetail(name, description, achieved, event) {
    const modal = document.getElementById("achievementDetailModal");
    const titleEl = document.getElementById("detailTitle");
    const descEl = document.getElementById("detailDescription");

    titleEl.textContent = achieved ? `${name} (획득 완료 ✅)` : `${name} (미획득)`;
    descEl.textContent = description;

    document.getElementById("achievementDetailModal").style.display = "block";

    const rect = event.currentTarget.getBoundingClientRect();
    const parentRect = document.getElementById("achievementListModal").getBoundingClientRect();

    modal.classList.remove("active"); // 초기화 (재실행 위해)
    void modal.offsetWidth;           // reflow 강제
    modal.classList.add("active");
}

function closeDetailModal() {
    document.getElementById("achievementDetailModal").style.display = "none";
}

function closeDetailModalOutside(event) {
    const modalContent = document.querySelector("#achievementDetailModal .ccmModal-content");
    if (!modalContent.contains(event.target)) {
        closeDetailModal();
    }
}
function escapeHtml(str) {
    return str.replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, '&quot;').replace(/'/g, '&#039;');
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

