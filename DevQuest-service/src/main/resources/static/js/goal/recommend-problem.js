// 추천 문제 생성 modal 열기 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".add-recommend-btn").forEach(btn => {
        btn.addEventListener("click", (event) => {
            const btn = event.currentTarget;
            const goalId = btn.dataset.goalId;
            const form = document.getElementById("problem-form");
            form.reset();
            form.goalId.value = goalId;

            document.querySelector("#problemModal h2").textContent = "추천 문제 생성";
            document.querySelector("#problemModal button[type='submit']").textContent = "추가";

            selectProblem(goalId);


            document.getElementById("problemModal").style.display = "flex";
        });
    });
});


// 추천 문제 생성 modal 닫기 이벤트 리스너
document.addEventListener("DOMContentLoaded", function () {
    const recommendClose = document.getElementById("recommendModalClose");
    recommendClose.addEventListener("click", function () {
        const form = document.getElementById("problem-form");
        form.reset();
        document.getElementById("problemModal").style.display = "none";
    });
});


// 추천 문제 생성 버튼 클릭 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("problem-form");

    form.onsubmit = function (e) {
        e.preventDefault();

        const checked = Array.from(form.querySelectorAll('input[name="problemId"]:checked'));
        const selectedIds = checked.map(cb => parseInt(cb.value));

        if (selectedIds.length === 0) {
            alert("문제를 하나 이상 선택하세요.");
            return;
        }

        const goalId = parseInt(form.goalId.value);

        data = {
            goalId: goalId,
            problemIds: selectedIds
        }

        fetch("/todos/from-problems", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(res => {
                if (!res.ok) throw new Error("저장 실패");
                return res.text();
            })
            .then(() => {
                alert("추천 문제 추가 완료!");
                document.getElementById("problemModal").style.display = "none";
                location.reload();
            })
            .catch(err => {
                console.error(err);
                alert("에러 발생: " + err.message);
            });
    };
});


// 전역 변수로 상태 유지
let currentProblems = []; //현재 문제들
let checkedProblemIds = new Set();// 체크된 문제들
let currentSort = { key: null, ascending: true };
let currentPage = 1;


// 추천 문제 조회 함수
function selectProblem() {
    fetch('/problem/select', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(res => res.json())
        .then(data => {
            currentProblems = data;
            renderProblemList(currentProblems);
            document.getElementById("problemModal").style.display = "block";
        })
        .catch(err => {
            console.error(err);
            alert("문제 목록 조회 실패");
        });
}



// 문제 리스트 렌더링 함수
function renderProblemList(problems) {
    const selectElement = document.getElementById("itemsPerPage"); // 페이지 당 몇개 정렬 할것인지 선택 요소
    const searchInput = document.getElementById("problemSearch"); // 검색할 문제 제목 인풋 텍스트
    const includeSolved = document.getElementById("includeSolved").checked; // 해결 문제 포함 미포함 여부
    const container = document.getElementById("problem-list"); // 문제 목록 div
    container.innerHTML = "";

    const itemsPerPage = parseInt(selectElement.value); // 페이지당 문제 출력수 정수로 바꾸어 저장

    // 검색 필터 적용
    let filtered = problems.filter(p => p.title.toLowerCase().includes(searchInput.value.toLowerCase())); //  문제 제목 검색 필터 적용

    // 해결 문제 필터 적용
    if (!includeSolved) { // 체크가 되어 있지 않으면 통과
        filtered = filtered.filter(p => p.solveCount === 0);
    }

    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const currentItems = filtered.slice(start, end);

    const table = document.createElement("table");
    table.className = "problem-table";

    table.innerHTML = `
    <thead>
      <tr>
        <th>선택</th>
        <th onclick="sortProblems('problemId')">문제 번호${renderSortIcon('problemId')}</th>
        <th onclick="sortProblems('site')">사이트${renderSortIcon('site')}</th>
        <th onclick="sortProblems('title')">문제 제목${renderSortIcon('title')}</th>
        <th onclick="sortProblems('level')">레벨${renderSortIcon('level')}</th>
        <th onclick="sortProblems('solveCount')">푼 횟수${renderSortIcon('solveCount')}</th>
      </tr>
    </thead>
    <tbody></tbody>
  `;

    const tbody = table.querySelector("tbody");

    currentItems.forEach(p => {
        const isChecked = checkedProblemIds.has(p.problemId); //  체크 상태 유지
        const row = document.createElement("tr");
        row.innerHTML = `
      <td><input type="checkbox" name="problemId" value="${p.problemId}" ${isChecked ? "checked" : ""}></td>
      <td>${p.problemId}</td>
      <td>${p.site}</td>
      <td><a href="${p.url}" target="_blank">${p.title}</a></td>
      <td>${p.level}</td>
      <td>${p.solveCount}</td>
    `;

        const checkbox = row.querySelector("input[type='checkbox']");
        checkbox.addEventListener("change", () => {
            if (checkbox.checked) {
                checkedProblemIds.add(p.problemId);
            } else {
                checkedProblemIds.delete(p.problemId);
            }
        });

        tbody.appendChild(row);
    });

    container.appendChild(table);
    renderPagination(filtered.length, itemsPerPage);
}

// 페이지네이션 렌더링
// function renderPagination(totalItems, itemsPerPage) {
//     const pagination = document.createElement("div");
//     pagination.className = "pagination";
//
//     const totalPages = Math.ceil(totalItems / itemsPerPage);
//
//     for (let i = 1; i <= totalPages; i++) {
//         const btn = document.createElement("button");
//         btn.textContent = i;
//         btn.className = i === currentPage ? "active" : "";
//         btn.addEventListener("click", () => {
//             currentPage = i;
//             renderProblemList(currentProblems);
//         });
//         pagination.appendChild(btn);
//     }
//
//     document.getElementById("problem-list").appendChild(pagination);
// }

function renderPagination(totalItems, itemsPerPage) {
    const pagination = document.createElement("div");
    pagination.className = "pagination";

    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const blockSize = 10; // 한 블록에 보여줄 페이지 수
    const currentBlock = Math.ceil(currentPage / blockSize);
    const startPage = (currentBlock - 1) * blockSize + 1;
    const endPage = Math.min(startPage + blockSize - 1, totalPages);

    // [처음] 버튼
    if (startPage > 1) {
        const firstBtn = document.createElement("button");
        firstBtn.textContent = "처음";
        firstBtn.addEventListener("click", () => {
            currentPage = 1;
            renderProblemList(currentProblems);
        });
        pagination.appendChild(firstBtn);
    }

    // [이전] 버튼
    if (startPage > 1) {
        const prevBtn = document.createElement("button");
        prevBtn.textContent = "이전";
        prevBtn.addEventListener("click", () => {
            currentPage = startPage - 1;
            renderProblemList(currentProblems);
        });
        pagination.appendChild(prevBtn);
    }

    // 페이지 숫자 버튼
    for (let i = startPage; i <= endPage; i++) {
        const btn = document.createElement("button");
        btn.textContent = i;
        btn.className = i === currentPage ? "active" : "";
        btn.addEventListener("click", () => {
            currentPage = i;
            renderProblemList(currentProblems);
        });
        pagination.appendChild(btn);
    }

    // [다음] 버튼
    if (endPage < totalPages) {
        const nextBtn = document.createElement("button");
        nextBtn.textContent = "다음";
        nextBtn.addEventListener("click", () => {
            currentPage = endPage + 1;
            renderProblemList(currentProblems);
        });
        pagination.appendChild(nextBtn);
    }

    // [끝] 버튼
    if (endPage < totalPages) {
        const lastBtn = document.createElement("button");
        lastBtn.textContent = "끝";
        lastBtn.addEventListener("click", () => {
            currentPage = totalPages;
            renderProblemList(currentProblems);
        });
        pagination.appendChild(lastBtn);
    }

    document.getElementById("problem-list").appendChild(pagination);
}

// 정렬 아이콘 렌더링
function renderSortIcon(key) {
    if (currentSort.key !== key) return ''; // 자신과 키가 다르면 아이콘 붙이지 않음
    return currentSort.ascending ? ' ▲' : ' ▼'; // 자신의 값이 true면 ▲ / false 면 ▼
}

// 정렬 함수 전역 등록
window.sortProblems = function (key) {
    if (currentSort.key === key) {
        currentSort.ascending = !currentSort.ascending; // 정렬 방향 반전
    } else {
        currentSort.key = key;
        currentSort.ascending = true;
    }

    currentProblems.sort((a, b) => {
        if (typeof a[key] === "string") { // 정렬 대상이 문자열인지 확인
            return currentSort.ascending
                ? a[key].localeCompare(b[key])
                : b[key].localeCompare(a[key]);
        } else {
            return currentSort.ascending
                ? a[key] - b[key]
                : b[key] - a[key];
        }
    });

    currentPage = 1;
    renderProblemList(currentProblems);
};

document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("problemSearch");
    const selectElement = document.getElementById("itemsPerPage");
    const includeSolved = document.getElementById("includeSolved");

    //  검색 입력 이벤트
    if (searchInput) {
        searchInput.addEventListener("input", () => {
            currentPage = 1;
            renderProblemList(currentProblems);
        });
    }

    // 페이지당 항목 수 변경 이벤트
    if (selectElement) {
        selectElement.addEventListener("change", () => {
            currentPage = 1;
            renderProblemList(currentProblems);
        });
    }

    if (includeSolved) {
        includeSolved.addEventListener("change", () => {
            currentPage = 1;
            renderProblemList(currentProblems);
        });
    }




});