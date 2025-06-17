
/*
  goal 화면에서 사용하는 modal 관련 이벤트 리스너, 함수를 모아 두었습니다.
*/

/*
    '목표 생성' 관련 이벤트 리스너
*/

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

// 목표 생성 함수
function createGoal() {
    const form = document.getElementById("goal-form");
    const companyId = form.dataset.companyId;

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
            document.getElementById("goalModal").style.display = "none";
            location.reload();
        } else {
            alert("등록 실패");
        }
    }).catch(err => {
        console.error(err);
        alert("에러 발생");
    });
}



// '목표 생성' modal 닫기 이벤트 리스너
document.addEventListener("DOMContentLoaded", function () {
    const goalClose = document.getElementById("goalModalClose");
    goalClose.addEventListener("click", function () {
        const form = document.getElementById("goal-form");
        form.reset();
        document.getElementById("goalModal").style.display = "none";
    });
});


/*
    '목표 수정' 관련 이벤트 리스너
*/


// '목표 수정' modal 열기 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".update-btn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const goalId = btn.dataset.goalId;

            const form = document.getElementById("goal-form");

            await fillGoalForm(goalId); // 목표 불러오기 함수 사용 -> form에 goalId에 해당하는 값 채우기

            form.setAttribute("data-id", goalId);
            document.querySelector("#goalModal h2").textContent = "목표 수정";
            document.querySelector("#goalModal button[type='submit']").textContent = "수정";

            form.onsubmit = function (e) { // 폼이 제출 될때 실행 되는 이벤트 핸들러 설정
                e.preventDefault(); // 폼 제출시 페이지가 새로고침 되는것을 방지
                updateGoal(goalId);
            };

            document.getElementById("goalModal").style.display = "flex";
        });
    });
});

//목표 불러오기  함수
function fillGoalForm(goalId) {
    fetch(`/goals/${goalId}/select`)//서버에서 원시 response 객체를 받음
        .then(res => res.json())// 서버 응답을 json 형태로 변환
        .then(data => { // res.json() 에서 반환된 값, 자바스크립트 객체
            const form = document.getElementById("goal-form");
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
            document.getElementById("goalModal").style.display = "none";
            location.reload();
        } else {
            alert("수정 실패");
        }
    }).catch(err => {
        console.error(err);
        alert("수정 중 오류 발생");
    });
}

// '목표 수정' modal 닫기 이벤트 리스너 -> 생성/수정 은 modal을 공유하기에 '목표 생성' modal 닫기 이벤트 리스너를 공유 합니다.




// 목표 삭제 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".delete-btn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const goalId = btn.dataset.goalId;
            deleteGoal(goalId);
        });
    });
});

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