function getCsrfToken() {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    return tokenMeta ? tokenMeta.getAttribute('content') : '';
}

function getCsrfHeader() {
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    return headerMeta ? headerMeta.getAttribute('content') : 'X-CSRF-TOKEN';
}

function postWithCsrf(url) {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    return fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        }
    });
}

function deleteAccount() {
    if (!confirm("정말 탈퇴하시겠습니까?")) return;

    postWithCsrf('/user/delete')
        .then(res => {
            if (!res.ok) throw new Error("탈퇴 실패");

            alert("탈퇴가 완료되었습니다.");
            return postWithCsrf('/logout');
        })
        .then(() => {
            window.location.href = "/user/withdraw-success";
        })
        .catch(err => {
            console.error(err);
            alert("탈퇴 또는 로그아웃 중 오류가 발생했습니다.");
        });
}

window.addEventListener("DOMContentLoaded", () => {
    const isWithdrawSuccess = document.body.classList.contains("withdraw-success");
    if (!isWithdrawSuccess) return;

    postWithCsrf('/logout')
        .catch(err => console.error("자동 로그아웃 실패:", err));
});
