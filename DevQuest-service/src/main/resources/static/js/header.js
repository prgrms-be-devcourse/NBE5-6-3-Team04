function goToDashboard() {
    location.href = '/dashboard';
}

function logout() {
    fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        }
    })
    .then(() => {
        window.location.href = '/signin';
    })
    .catch(err => {
        console.error('Logout failed', err);
        alert('로그아웃 실패');
    });
}
