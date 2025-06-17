// full-calendar 관련 코드
let calendar;
document.addEventListener('DOMContentLoaded', function() {
    const companyId = document.querySelector('.calendar').dataset.companyid;

    var calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: '/companies/' + companyId + '/events',
        eventClick: function(info) {
            info.jsEvent.preventDefault(); //  기본 링크 이동 막기
            window.open(info.event.url, '_blank');    // 새 창에서만 열리게
        }

    });
    // calendar.render();
});