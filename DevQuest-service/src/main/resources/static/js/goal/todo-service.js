
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

// 투두 생성 함수
function createTodo() {

    const form = document.getElementById("todo-form");
    const goalId = form.goalId.value;

    const data = {
        goalId: goalId,
        content: form.title.value,
        url:form.url.value,
        startDate: form.startDate.value
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
                document.getElementById("todoModal").style.display = "none";
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
        const todoId = btn.dataset.todoId;

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
                document.getElementById("todoModal").style.display = "none";
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



// 투두 삭제 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".delete-todo-btn").forEach(btn => {
        btn.addEventListener("click",  () => {
            const todoId = btn.dataset.todoId;
            deleteTodo(todoId);
        });
    });
});


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
            const todoId = checkbox.dataset.todoId;
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