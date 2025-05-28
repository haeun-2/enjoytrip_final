<template>
  <div class="page-container">
    <!-- Hero Section -->
    <main class="hero">
      <div class="hero-content">
        <div class="text-section">
          <h1 class="main-title">
            <span :class="{ 'typing-text': !firstLineDone }">{{
              firstLine
            }}</span>
            <br />
            <span class="highlight typing-text">{{ secondLine }}</span>
          </h1>
          <p class="subtitle">
            전국 곳곳의 아름다운 여행지를 발견하고 특별한 추억을 만들어보세요
          </p>
        </div>

        <div class="cta-section">
          <button @click="handleStart" class="start-button">
            여행 시작하기
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
const firstLine = ref("");
const secondLine = ref("");
const fullFirstLine = "꿈꾸던 여행을";
const fullSecondLine = "현실로";
const firstLineDone = ref(false);
const router = useRouter();
const handleStart = () => {
  router.push({ name: "AttractionList" });
};

const typeLines = () => {
  firstLine.value = "";
  secondLine.value = "";
  firstLineDone.value = false;

  let i = 0;
  const typingFirst = setInterval(() => {
    firstLine.value += fullFirstLine[i++];
    if (i >= fullFirstLine.length) {
      clearInterval(typingFirst);
      firstLineDone.value = true;

      let j = 0;
      setTimeout(() => {
        const typingSecond = setInterval(() => {
          secondLine.value += fullSecondLine[j++];
          if (j >= fullSecondLine.length) {
            clearInterval(typingSecond);
            // 반복 재시작: 2초 후 다시 타이핑 시작
            setTimeout(() => {
              typeLines();
            }, 5000);
          }
        }, 150);
      }, 300);
    }
  }, 150);
};

onMounted(() => {
  typeLines();
});
</script>

<style scoped>
/* 기본 설정 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.page-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #eff6ff 0%, #e0e7ff 100%);
  position: relative;
  overflow: hidden;
}

/* Hero Section */
.hero {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 120px);
}

.hero-content {
  text-align: center;
  max-width: 768px;
}

.text-section {
  margin-bottom: 48px;
}

.main-title {
  font-size: 64px;
  font-weight: 700;
  color: #111827;
  line-height: 1.1;
  margin-bottom: 24px;
}

.highlight {
  color: var(--primary-color);
}

.subtitle {
  font-size: 24px;
  color: #6b7280;
  line-height: 1.6;
  max-width: 600px;
  margin: 0 auto;
}

.cta-section {
  margin-top: 32px;
}

.start-button {
  background-color: var(--primary-color);
  color: white;
  padding: 16px 48px;
  font-size: 18px;
  font-weight: 600;
  border: none;
  border-radius: 50px;
  cursor: pointer;
  box-shadow: 0 10px 25px rgba(37, 99, 235, 0.3);
  transition: all 0.3s ease;
  outline: none;
}

.start-button:hover {
  background-color: var(--primary-hover);
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 15px 35px rgba(37, 99, 235, 0.4);
}

.start-button:focus {
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.3);
}

.start-button:active {
  transform: translateY(0) scale(1.02);
}

/* 애니메이션 */
@keyframes pulse {
  0%,
  100% {
    opacity: 0.1;
    transform: scale(1);
  }
  50% {
    opacity: 0.3;
    transform: scale(1.1);
  }
}

.typing-text {
  border-right: 2px solid #2563eb;
  white-space: nowrap;
  overflow: hidden;
  display: inline-block;
  animation: blink-caret 0.75s step-end infinite;
}

@keyframes blink-caret {
  from,
  to {
    border-color: transparent;
  }
  50% {
    border-color: #2563eb;
  }
}
</style>
