<template>
  <div class="loading-overlay">
    <div class="loading-container">
      <div class="spinner-wrapper">
        <!-- Spinner -->
        <div class="spinner"></div>

        <!-- Loading Text -->
        <p class="loading-text">잠시만 기다려주세요</p>

        <!-- Dots Animation -->
        <div class="dots">
          <span class="dot"></span>
          <span class="dot"></span>
          <span class="dot"></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.3); /* 반투명 어두운 배경 */
  z-index: 999; /* overlay가 가장 아래에 위치 */
  display: flex;
  align-items: center;
  justify-content: center;
}
.loading-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9fafb;
  padding: 30px 50px;
  border-radius: 10px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.spinner-wrapper {
  text-align: center;
}

/* 회전 스피너 */
.spinner {
  width: 48px;
  height: 48px;
  border: 4px solid #e5e7eb;
  border-top: 4px solid #2563eb;
  border-radius: 50%;
  margin: 0 auto 20px;
  animation: spin 1s linear infinite;
}

/* 로딩 텍스트 */
.loading-text {
  color: #6b7280;
  font-size: 18px;
  font-weight: 500;
  margin: 0 0 16px 0;
}

/* 점들 컨테이너 */
.dots {
  display: flex;
  justify-content: center;
  gap: 4px;
}

/* 개별 점 */
.dot {
  width: 8px;
  height: 8px;
  background-color: #2563eb;
  border-radius: 50%;
  animation: bounce 1.4s ease-in-out infinite;
}

.dot:nth-child(2) {
  animation-delay: 0.1s;
}

.dot:nth-child(3) {
  animation-delay: 0.2s;
}

/* 회전 애니메이션 */
@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 점프 애니메이션 */
@keyframes bounce {
  0%,
  20%,
  53%,
  80%,
  100% {
    transform: translateY(0);
  }
  40%,
  43% {
    transform: translateY(-8px);
  }
  70% {
    transform: translateY(-4px);
  }
  90% {
    transform: translateY(-2px);
  }
}
</style>
