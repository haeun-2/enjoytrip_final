<template>
  <div>
    <h2 class="page-title">대시보드</h2>

    <!-- 통계 카드 -->
    <div class="stats-grid">
      <div v-for="stat in stats" :key="stat.title" class="stat-card">
        <div class="stat-header">
          <span class="stat-title">{{ stat.title }}</span>
          <span class="stat-icon">{{ stat.icon }}</span>
        </div>
        <div class="stat-value">{{ stat.value }}</div>
        <div class="stat-change">{{ stat.change }}</div>
      </div>
    </div>

    <!-- 차트 영역 -->
    <div class="charts-grid">
      <!-- 결제 통계 (라인 차트) -->
      <div class="chart-card">
        <h2 class="chart-title">월별 통계</h2>
        <div class="chart-wrapper">
          <canvas ref="paymentChart"></canvas>
        </div>
      </div>

      <!-- 사용자 현황 (파이 차트) -->
      <div class="chart-card">
        <h2 class="chart-title">사용자 현황</h2>
        <div class="chart-wrapper">
          <canvas ref="userChart"></canvas>
        </div>
      </div>

      <!-- 여행 카테고리 (도넛 차트) -->
      <div class="chart-card">
        <h2 class="chart-title">여행 카테고리</h2>
        <div class="chart-wrapper">
          <canvas ref="categoryChart"></canvas>
        </div>
      </div>
    </div>
    <!-- 두 번째 행: 실시간 방문자 & 성과 지표 -->
    <div class="charts-grid">
      <!-- 실시간 방문자 (영역 차트) -->
      <div class="chart-container">
        <h2 class="chart-title">실시간 방문자</h2>
        <div class="chart-wrapper">
          <canvas ref="visitorChart"></canvas>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from "vue";
import {
  getStatCount,
  getMemberCount,
  getMonthlyStat,
  getCatogoryCount,
} from "@/apis/admin/stat";
import {
  // Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  RadialLinearScale,
  Title,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";
import { Chart as ChartJS } from "chart.js/auto";
// Chart.js 등록
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  ArcElement,
  RadialLinearScale,
  Title,
  Tooltip,
  Legend,
  Filler
);

// 차트 참조
const paymentChart = ref(null);
const userChart = ref(null);
const categoryChart = ref(null);
const visitorChart = ref(null);

// 차트 인스턴스들
let charts = [];

// 실시간 데이터 업데이트를 위한 인터벌
let updateInterval = null;

const monthData = ref({});
const memberData = ref({});
const categoryData = ref({});
onMounted(async () => {
  monthData.value = await getMonthlyStat();
  memberData.value = await getMemberCount();
  categoryData.value = await getCatogoryCount();
  initializeCharts();
  startRealTimeUpdates();
});

onUnmounted(() => {
  // 차트 정리
  charts.forEach((chart) => chart.destroy());
  if (updateInterval) {
    clearInterval(updateInterval);
  }
});

const initializeCharts = () => {
  // 월별 통계 차트
  const paymentCtx = paymentChart.value.getContext("2d");
  const paymentChartInstance = new ChartJS(paymentCtx, {
    type: "line",
    data: {
      labels: monthData.value.monthLabel,
      datasets: [
        {
          label: "게시글 추이",
          data: monthData.value.articles,
          borderColor: "#3B82F6",
          borderWidth: 3,
          tension: 0.4,
          pointBackgroundColor: "#3B82F6",
          pointBorderColor: "#ffffff",
          pointBorderWidth: 2,
          pointRadius: 6,
          pointHoverRadius: 8,
        },
        {
          label: "사용자 추이",
          data: monthData.value.members,
          borderColor: "#3B82F6",
          borderWidth: 3,
          tension: 0.4,
          pointBackgroundColor: "#3B82F6",
          pointBorderColor: "#ffffff",
          pointBorderWidth: 2,
          pointRadius: 6,
          pointHoverRadius: 8,
        },
        {
          label: "신고 추이",
          data: monthData.value.openReports,
          borderColor: "#3B82F6",
          borderWidth: 3,
          tension: 0.4,
          pointBackgroundColor: "#3B82F6",
          pointBorderColor: "#ffffff",
          pointBorderWidth: 2,
          pointRadius: 6,
          pointHoverRadius: 8,
        },
        {
          label: "Q&A 추이",
          data: monthData.value.openQnAs,
          borderColor: "#3B82F6",
          borderWidth: 3,
          tension: 0.4,
          pointBackgroundColor: "#3B82F6",
          pointBorderColor: "#ffffff",
          pointBorderWidth: 2,
          pointRadius: 6,
          pointHoverRadius: 8,
        },
        {
          label: "여행계획 추이",
          data: monthData.value.plans,
          borderColor: "#3B82F6",
          borderWidth: 3,
          tension: 0.4,
          pointBackgroundColor: "#3B82F6",
          pointBorderColor: "#ffffff",
          pointBorderWidth: 2,
          pointRadius: 6,
          pointHoverRadius: 8,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: {
        duration: 2000,
        easing: "easeInOutQuart",
      },
      plugins: {
        legend: {
          display: false,
        },
        tooltip: {
          backgroundColor: "rgba(0, 0, 0, 0.8)",
          titleColor: "#ffffff",
          bodyColor: "#ffffff",
          borderColor: "#3B82F6",
          borderWidth: 1,
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          grid: {
            color: "rgba(0, 0, 0, 0.1)",
          },
          ticks: {
            color: "#6B7280",
          },
        },
        x: {
          grid: {
            display: false,
          },
          ticks: {
            color: "#6B7280",
          },
        },
      },
    },
  });
  charts.push(paymentChartInstance);

  // 사용자 현황 파이 차트
  const userCtx = userChart.value.getContext("2d");
  const userChartInstance = new ChartJS(userCtx, {
    type: "pie",
    data: {
      labels: ["활성 사용자", "탈퇴 사용자", "정지 사용자"],
      datasets: [
        {
          data: [
            memberData.value.activeMember,
            memberData.value.deletedMember,
            memberData.value.blockedMember,
          ],
          backgroundColor: ["#3B82F6", "#6B7280", "#EF4444"],
          borderWidth: 0,
          hoverOffset: 10,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: {
        animateRotate: true,
        duration: 2000,
      },
      plugins: {
        legend: {
          position: "bottom",
          labels: {
            padding: 20,
            usePointStyle: true,
            color: "#374151",
          },
        },
        tooltip: {
          backgroundColor: "rgba(0, 0, 0, 0.8)",
          titleColor: "#ffffff",
          bodyColor: "#ffffff",
        },
      },
    },
  });
  charts.push(userChartInstance);

  // 여행계획 카테고리 도넛 차트
  const categoryCtx = categoryChart.value.getContext("2d");
  const categoryChartInstance = new ChartJS(categoryCtx, {
    type: "doughnut",
    data: {
      labels: [
        "휴양",
        "관광",
        "자연",
        "문화",
        "미식",
        "액티비티",
        "쇼핑",
        "사진",
      ],
      datasets: [
        {
          data: [
            categoryData.value.relaxing,
            categoryData.value.sightSeeing,
            categoryData.value.nature,
            categoryData.value.cultural,
            categoryData.value.foodie,
            categoryData.value.adventure,
            categoryData.value.shopping,
            categoryData.value.travel,
          ],
          backgroundColor: [
            "#3B82F6",
            "#10B981",
            "#F59E0B",
            "#EF4444",
            "#8B5CF6",
            "#3B82F6",
            "#10B981",
            "#F59E0B",
          ],
          borderWidth: 0,
          hoverOffset: 8,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: {
        animateRotate: true,
        duration: 2000,
      },
      cutout: "60%",
      plugins: {
        legend: {
          position: "bottom",
          labels: {
            padding: 15,
            usePointStyle: true,
            color: "#374151",
          },
        },
        tooltip: {
          backgroundColor: "rgba(0, 0, 0, 0.8)",
          titleColor: "#ffffff",
          bodyColor: "#ffffff",
        },
      },
    },
  });
  charts.push(categoryChartInstance);

  // 실시간 방문자 영역 차트
  const visitorCtx = visitorChart.value.getContext("2d");
  const visitorChartInstance = new ChartJS(visitorCtx, {
    type: "line",
    data: {
      labels: Array.from({ length: 24 }, (_, i) => `${i}:00`),
      datasets: [
        {
          label: "방문자 수",
          data: generateRandomData(24, 50, 200),
          borderColor: "#10B981",
          backgroundColor: "rgba(16, 185, 129, 0.2)",
          borderWidth: 2,
          fill: true,
          tension: 0.4,
          pointRadius: 0,
          pointHoverRadius: 6,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: {
        duration: 1000,
      },
      plugins: {
        legend: {
          display: false,
        },
        tooltip: {
          backgroundColor: "rgba(0, 0, 0, 0.8)",
          titleColor: "#ffffff",
          bodyColor: "#ffffff",
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          grid: {
            color: "rgba(0, 0, 0, 0.1)",
          },
          ticks: {
            color: "#6B7280",
          },
        },
        x: {
          grid: {
            display: false,
          },
          ticks: {
            color: "#6B7280",
          },
        },
      },
    },
  });
  charts.push(visitorChartInstance);
};

const generateRandomData = (length, min, max) => {
  return Array.from(
    { length },
    () => Math.floor(Math.random() * (max - min + 1)) + min
  );
};

const startRealTimeUpdates = () => {
  updateInterval = setInterval(() => {
    // 실시간 방문자 차트 업데이트
    const visitorChartInstance = charts[4];
    if (visitorChartInstance) {
      const newData = generateRandomData(24, 50, 200);
      visitorChartInstance.data.datasets[0].data = newData;
      visitorChartInstance.update("none");
    }
  }, 5000); // 5초마다 업데이트
};

const stat = ref({});
const stats = ref([]);

function calculateGrowthRate(now, prev) {
  if (!prev || prev === 0) {
    return now > 0 ? "+100.00%" : "0.00%";
  }

  const diff = now - prev;
  const rate = (diff / prev) * 100;
  const fixedRate = Math.abs(rate).toFixed(2); // 항상 양수로 포맷 후 기호 수동 붙이기

  if (rate > 0) return `+${fixedRate}%`;
  if (rate < 0) return `-${fixedRate}%`;
  return "0.00%";
}

onMounted(async () => {
  const response = await getStatCount();
  stat.value = response;
  const memberRate = calculateGrowthRate(
    stat.value.nowMember,
    stat.value.prevMember
  );
  const articleRate = calculateGrowthRate(
    stat.value.nowArticle,
    stat.value.prevArticle
  );
  const openReportRate = calculateGrowthRate(
    stat.value.nowOpenReport,
    stat.value.prevOpenReport
  );
  const openQnARate = calculateGrowthRate(
    stat.value.nowOpenQnA,
    stat.value.prevOpenQnA
  );
  stats.value = [
    {
      title: "총 회원수",
      value: stat.value.totalMember,
      change: `${memberRate}% from last month`,
      icon: "👥",
    },
    {
      title: "총 게시글",
      value: stat.value.totalArticle,
      change: `${articleRate}% from last month`,
      icon: "📄",
    },
    {
      title: "미처리 신고",
      value: stat.value.totalOpenReport,
      change: `${openReportRate}% from last month`,
      icon: "⚠️",
    },
    {
      title: "미답변 QnA",
      value: stat.value.totalOpenQnA,
      change: `${openQnARate}% from last month`,
      icon: "💬",
    },
  ];
});
</script>

<style scoped>
.page-title {
  font-size: 1.875rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  color: #1e293b;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.page-controls {
  display: flex;
  gap: 0.5rem;
}

/* 통계 카드 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.stat-title {
  font-size: 0.875rem;
  color: #64748b;
}

.stat-icon {
  font-size: 1rem;
  color: #94a3b8;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #1e293b;
  margin-bottom: 0.25rem;
}

.stat-change {
  font-size: 0.75rem;
  color: #64748b;
}

/* 차트 */
.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 1.5rem;
}

.chart-card {
  background: white;
  padding: 1.5rem;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.chart-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 1.5rem;
  text-align: center;
}

.chart-wrapper {
  position: relative;
  height: 300px;
  width: 100%;
}

/* 애니메이션 키프레임 */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 차트 컨테이너별 지연 애니메이션 */
.chart-container:nth-child(1) {
  animation-delay: 0.1s;
}

.chart-container:nth-child(2) {
  animation-delay: 0.2s;
}

.chart-container:nth-child(3) {
  animation-delay: 0.3s;
}

.chart-container:nth-child(4) {
  animation-delay: 0.4s;
}

.chart-container:nth-child(5) {
  animation-delay: 0.5s;
}

.chart-container:nth-child(6) {
  animation-delay: 0.6s;
}

/* 로딩 애니메이션 */
.chart-wrapper::before {
  content: "";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  z-index: 1;
}

.chart-wrapper canvas {
  position: relative;
  z-index: 2;
}

@keyframes spin {
  0% {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  100% {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}

/* 차트가 로드되면 로딩 애니메이션 숨기기 */
.chart-wrapper:has(canvas[style*="display: block"])::before {
  display: none;
}
</style>
