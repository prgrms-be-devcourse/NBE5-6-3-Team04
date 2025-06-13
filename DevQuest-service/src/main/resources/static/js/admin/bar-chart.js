(function ($) {
  $(document).ready(function () {
    const stackedBarChart = $('#stacked-bar-chart');

    if (stackedBarChart.length) {
      $.ajax({
        url: 'api/admin/company-stats',
        method: 'GET',
        dataType: 'json',
        success: function (response) {
          const labels = response.map(item => item.company);
          const projectData = response.map(item => item.projectCount);
          const maxValue = Math.max(...projectData) + 5;
          new Chart(stackedBarChart, {
            type: 'bar',
            data: {
              labels: labels,
              datasets: [{
                data: projectData,
                label: '기업별 프로젝트 수',
                backgroundColor: labels.map((_, i) => {
                  const colors = [
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(71,67,66,0.7)',
                    'rgba(104,29,10,0.7)'
                  ];
                  return colors[i % colors.length];
                })
              }]
            },
            options: {
              indexAxis: 'y',
              responsive: true,
              maintainAspectRatio: false,
              plugins: {
                legend: { display: false },
                title: {
                  display: true,
                  text: '기업별 총 프로젝트 수'
                }
              },
              scales: {
                x: {
                  type: 'linear',
                  beginAtZero: true,
                  min: 0,
                  max: maxValue
                },
                y: {
                  type: 'category'
                }
              }
            }
          });
        },
        error: function () {
          console.error('기업별 데이터 불러오기 실패');
        }
      });
    }
  });
}(jQuery));