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
                const offset = -3 + i; // -3px ~ +3px 간격으로
                return `<div class="badge-layer" style="transform: translateZ(${offset}px);"></div>`;
            }).join("");

            list.innerHTML = `
  <div class="achievement-grid">
    ${data.map(a => `
      <div class="achievement-card ${a.achieved ? 'achieved' : 'locked'}">
        <div 
          class="badge" 
          style="--img-url: url('${a.imageUrl}');"
          title="${a.name}"
        >
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

