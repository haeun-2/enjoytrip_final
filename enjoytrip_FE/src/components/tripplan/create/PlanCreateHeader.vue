<template>
  <div>
    <!-- 헤더 섹션 -->
    <div class="header">
      <div class="trip-info">
        <input
          :value="props.title"
          @@keyup.enter="handleInput"
          @input="handleInput"
        />
        <p class="date-range">{{ props.startDate }} - {{ props.endDate }}</p>
      </div>
      <div class="day-selector">
        <button class="clear-button" @click="clear">일정 초기화</button>
        <button class="save-button" @click="openDescriptionModal">
          일정 생성
        </button>
        <PlanDescriptionModal
          ref="descriptionModal"
          @submit="handleSaveWithDescription"
        />
      </div>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <!-- 왼쪽 패널 -->
      <div class="left-panel">
        <!-- 탭 메뉴 -->
        <div class="tabs">
          <button
            class="tab"
            :class="{ active: activeTab === 'places' }"
            @click="changeTab('places')"
          >
            장소 선택
          </button>
          <button
            class="tab"
            :class="{ active: activeTab === 'myPlaces' }"
            @click="changeTab('myPlaces')"
          >
            내 장소 목록
          </button>
        </div>
        <div class="filter-container">
          <BaseSearchBar text="장소명을 입력하세요" />
          <BaseSearchConditionBar @changeSearch="changeSearch" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import BaseSearchBar from "@/components/common/BaseSearchBar.vue";
import BaseSearchConditionBar from "@/components/common/BaseSearchConditionBar.vue";
import PlanDescriptionModal from "./PlanDescriptionModal.vue";

// 상태 정의
const emit = defineEmits([
  "changeSearch",
  "clear",
  "update:title",
  "save",
  "changeTab",
]);
const activeTab = ref("places");
const changeSearch = (data) => {
  emit("changeSearch", data);
};
const changeTab = (to) => {
  activeTab.value = to;
  emit("changeTab", to);
};
const clear = () => {
  emit("clear", clear);
};

const save = (desc) => {
  emit("save", desc);
};
// 제목 수정 가능
const props = defineProps({
  title: String,
  startDate: String,
  endDate: String,
});
// 내부 상태로 title을 복사
const editableTitle = ref(props.title);
// props.title이 바뀌면 editableTitle도 갱신
watch(
  () => props.title,
  (newVal) => {
    editableTitle.value = newVal;
  }
);
// 사용자 입력 시 부모에 알림
const handleInput = (event) => {
  emit("update:title", event.target.value);
};

const descriptionModal = ref();

const openDescriptionModal = () => {
  descriptionModal.value.show();
};

const handleSaveWithDescription = (desc) => {
  // 여기서 저장 처리
  console.log("사용자 입력 설명:", desc);
  save(desc);
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
  width: 410px;
  box-sizing: border-box;
}

.trip-info input {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
  border: none;
  outline: none;
  background: transparent;
  width: 100%;
}

.date-range {
  margin: 4px 0 0;
  font-size: 14px;
  color: #666;
}

.day-selector {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 10px;
}

.day-number {
  display: flex;
  align-items: center;
  margin-right: 16px;
}

.number {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background-color: #333;
  color: white;
  border-radius: 50%;
  font-weight: bold;
  margin-right: 8px;
}

.day-info {
  font-size: 14px;
  color: #666;
}
.save-button {
  padding: 8px 12px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  width: 100px;
}
.save-button:hover {
  background-color: var(--primary-hover);
}
.clear-button {
  padding: 8px 12px;
  background-color: var(--error-color);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  width: 100px;
}
.clear-button:hover {
  background-color: #f02315;
}
.main-content {
  display: flex;
  overflow: hidden;
  width: 410px;
}

.left-panel {
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.tabs {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
  margin-bottom: 10px;
}

.tab {
  flex: 1;
  padding: 12px;
  text-align: center;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 15px;
  color: #666;
  position: relative;
}

.tab.active {
  color: var(--primary-color);
  font-weight: 600;
}

.tab.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: var(--primary-color);
}
</style>
