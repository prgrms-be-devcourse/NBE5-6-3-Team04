document.addEventListener("DOMContentLoaded", function () {
	const ctx = document.getElementById("card-legend-line-chart").getContext("2d");

	new Chart(ctx, {
		type: "line",
		data: {
			labels: labels,
			datasets: [
				{
					label: "가입자 수",
					data: joinData,
					borderColor: "blue",
					fill: false
				},
				{
					label: "탈퇴자 수",
					data: leaveData,
					borderColor: "red",
					fill: false
				}
			]
		},
		options: {
			responsive: true,
			plugins: {
				legend: { position: "top" },
				title: {
					display: true,
					text: "일별 가입자/탈퇴자 수"
				}
			},
			scales: {
				x: {
					title: { display: true, text: "날짜" }
				},
				y: {
					beginAtZero: true,
					title: { display: true, text: "인원 수" }
				}
			}
		}
	});
});