// full-calendar 관련 코드
let calendar;
document.addEventListener('DOMContentLoaded', function() {
    const companyId = document.querySelector('.calendar').dataset.companyid;

    var calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: '/companies/' + companyId + '/events',
        editable: true,

        eventClick: function(info) {
            info.jsEvent.preventDefault(); //  기본 링크 이동 막기
            window.open(info.event.url, '_blank');    // 새 창에서만 열리게
        },

        eventDrop: function(info) {
            const event = info.event;
            const todoId = event.extendedProps.todoId;

            const title = event.title;
            const url = event.extendedProps.url;
            const isDone = event.extendedProps.isDone;

            const data = {

                content: title,
                url: url,
                isDone: isDone,
                startDate: event.startStr,
                endDate: event.startStr
            };

            fetch(`/todos/${todoId}/update`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            }).then(res => {
                if (!res.ok) {
                    alert("업데이트 실패!");
                    info.revert(); // 실패 시 원래 자리로 복귀
                }
            }).catch(() => {
                alert("오류 발생");
                info.revert();
            });
        }

    });
    // calendar.render();
});


