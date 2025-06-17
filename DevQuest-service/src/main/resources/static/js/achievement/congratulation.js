const jsConfetti = new JSConfetti();
document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const rawParam = urlParams.get("achievementName");

    if (rawParam) {
        const decoded = decodeURIComponent(rawParam.replace(/\+/g, ' '));
        console.log("🎯 감지된 achievementName:", decoded);
        showAchievementModal(decoded);

        const baseUrl = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, baseUrl);
    }
});

function showAchievementModal(achievementName) {
    document.getElementById("congratsMessage").innerText = `"${achievementName}" 업적을 달성했어요!`;
    // 모달 보이기
    document.getElementById("achievementCongratsModal").style.display = "block";

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
                "#ff0a54",
                "#ff477e",
                "#ff7096",
                "#ff85a1",
                "#fbb1bd",
                "#f9bec7",
            ],
            confettiRadius: 5,
            confettiNumber: 500,
        });

        // jsConfetti.addConfetti({
        //     emojis: ['🎉', '🐱'],
        //     emojiSize: 36,
        //     confettiNumber: 40,
        // });
    }, 300);
}

function closeCongratsModal() {
    document.getElementById("achievementCongratsModal").style.display = "none";
}
