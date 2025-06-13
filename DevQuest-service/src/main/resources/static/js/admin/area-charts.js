(function ($) {
  $(document).ready(function () {
    const chartLabels = typeof langLabels !== 'undefined' && langLabels.length > 0
        ? langLabels
        : ['Java', 'Python', 'JavaScript', 'C++', 'Go', 'Kotlin'];

    const chartData = typeof langScores !== 'undefined' && langScores.length > 0
        ? langScores
        : [65, 70, 50, 40, 60, 55];

    const data = {
      labels: chartLabels,
      datasets: [{
        backgroundColor: 'rgba(54, 162, 235, 0.5)',
        borderColor: 'rgba(54, 162, 235, 1)',
        pointBackgroundColor: 'rgba(54, 162, 235, 1)',
        data: chartData,
        label: '유저 관심 언어 점수'
      }]
    };

    const $radarChart = $('#radar-chart');
    if ($radarChart.length) {
      new Chart($radarChart, {
        type: 'radar',
        data: data,
        options: {
          maintainAspectRatio: false,
          responsive: true,
          plugins: {
            title: {
              display: true,
              text: '유저 관심 언어 기반 직무 스탯 차트'
            }
          }
        }
      });
    }
  });
}(jQuery));