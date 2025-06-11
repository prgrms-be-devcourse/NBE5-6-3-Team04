function goToDashboard() {
    location.href = '/dashboard';
}

function logout() {
    const token = getCsrfToken();
    const header = getCsrfHeader();

    if (!header || !token) {
        console.error('CSRF token or header not found');
        alert('보안 토큰이 누락되어 로그아웃할 수 없습니다.');
        return;
    }

    fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [header]: token
        }
    })
    .then(() => {
        window.location.href = '/';
    })
    .catch(err => {
        console.error('Logout failed', err);
        alert('로그아웃 실패');
    });
}

function getCsrfToken() {
    const token = document.querySelector('meta[name="_csrf"]');
    return token ? token.getAttribute('content') : '';
}

function getCsrfHeader() {
    const header = document.querySelector('meta[name="_csrf_header"]');
    return header ? header.getAttribute('content') : 'X-CSRF-TOKEN';
}
