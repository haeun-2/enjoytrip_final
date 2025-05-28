<template>
  <!-- 헤더 영역 -->
  <div>
    <header class="header">
      <div>
        <h2>{{ headerInfo.name }}</h2>
        <span class="date"
          >{{ headerInfo.startDate }} ~ {{ headerInfo.endDate }}</span
        >
      </div>
      <button class="edit-btn" @click="handleCopy">내일정으로 복사</button>
    </header>
    <!-- 탭 메뉴 -->
    <div class="tab-menu">
      <div class="tab-buttons">
        <!-- 전체일정 탭 -->
        <button
          class="tab-button"
          :class="{ active: activeTab === 'all' }"
          @click="selectTab('all')"
        >
          전체일정
        </button>

        <!-- 일차별 탭 -->
        <button
          v-for="(day, index) in days"
          :key="index"
          class="tab-button"
          :class="{ active: activeTab === index }"
          @click="selectTab(index, day)"
        >
          {{ day }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

// 상태 관리
const activeTab = ref("all");

const props = defineProps({
  headerInfo: Object,
  days: Array,
  selectedDay: Number,
});

const emit = defineEmits(["changeDay", "copy"]);

// 탭 클릭 핸들러
function selectTab(tabValue, day = -1) {
  activeTab.value = tabValue;
  if (day == -1) {
    emit("changeDay", -1);
  } else if (day) {
    emit("changeDay", tabValue);
  }
}

function handleCopy() {
  alert("미구현 기능입니다.");
  emit("copy");
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background-color: var(--card-color);
  z-index: 10;
  white-space: nowrap;
}

.header h2 {
  margin: 0;
  margin-top: 10px;
}

.date {
  font-size: 16px;
  font-weight: 400;
  color: var(--text-light);
}

.edit-btn {
  padding: 6px 12px;
  background-color: #fff;
  border: 1.5px solid var(--primary-color);
  border-radius: 20px;
  color: var(--primary-color);
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  padding: 3px 10px;
  margin-left: 12px;
}

.tab-menu {
  background-color: var(--card-color);
  border-bottom: 1px solid var(--border-color);
}

.tab-buttons {
  display: flex;
  padding: 0 18px;
  border-bottom: 1px solid var(--border-color);
  overflow-y: auto;
  scrollbar-width: thin; /* Firefox */
  scrollbar-color: #ccc transparent;
}

.tab-button {
  padding: 16px 24px;
  font-size: 16px;
  font-weight: 500;
  color: var(--text-color);
  background: none;
  border: none;
  cursor: pointer;
  position: relative;
  transition: all 0.2s ease;
  text-wrap: nowrap;
}

.tab-button.active {
  color: var(--primary-color);
  font-weight: 600;
}

.tab-button.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background-color: var(--primary-color);
}
</style>
