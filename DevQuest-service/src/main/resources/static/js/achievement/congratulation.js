const jsConfetti = new JSConfetti();
document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const rawParam = urlParams.get("achievementName");

    const modal = document.getElementById("achievementCongratsModal");
    modal.addEventListener("mousedown", function () {
        closeCongratsModal();
    });

    if (rawParam) {
        const decoded = decodeURIComponent(rawParam.replace(/\+/g, ' '));
        console.log("🎯 감지된 achievementName:", decoded);
        showAchievementModal(decoded);

        const baseUrl = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, baseUrl);
    }
});

function showAchievementModal(achievementName) {
    const modal = document.getElementById("achievementCongratsModal");
    const message = document.getElementById("congratsMessage");

    message.innerText = `"${achievementName}" 업적을 달성했어요!`;

    // fade-in
    modal.style.display = "block";
    void modal.offsetWidth; // 강제로 reflow 유도
    modal.classList.add("active");

    // confetti 효과
    const duration = 2500;
    const animationEnd = Date.now() + duration;

    const interval = setInterval(() => {
        const timeLeft = animationEnd - Date.now();
        if (timeLeft <= 0) {
            clearInterval(interval);
            return;
        }
        jsConfetti.addConfetti({
            confettiColors: [
                "#ff0a54", "#ff477e", "#ff7096",
                "#ff85a1", "#fbb1bd", "#f9bec7",
            ],
            confettiRadius: 5,
            confettiNumber: 500,
        });
    }, 300);
}
function closeCongratsModal() {
    document.getElementById("achievementCongratsModal").style.display = "none";
}
