<template>
  <header class="header">
    <div class="header-left">
      <h1 class="page-title">나의 일정</h1>
      <div class="tabs">
        <button
          v-for="filter in filterOptions"
          :key="filter.value"
          :class="['tab', { active: activeFilter === filter.value }]"
          @click="changeFilter(filter.value)"
        >
          {{ filter.label }}
        </button>
      </div>
    </div>
    <div class="header-right">
      <TripPlanCreatePopup @date-range-selected="handleDateRange" />
    </div>
  </header>
</template>

<script setup>
import { ref } from "vue";
import TripPlanCreatePopup from "../board/TripPlanCreatePopup.vue";
import { createTripPlan } from "@/apis/tripplan/plan";
import { useRouter } from "vue-router";
const emit = defineEmits(["changeFilter"]);
const filterOptions = [
  { value: "all", label: "전체" },
  { value: "upcoming", label: "예정" },
  { value: "ongoing", label: "진행중" },
  { value: "completed", label: "완료" },
];
const activeFilter = ref("all");
const router = useRouter();
const goCreatePlan = (planInfo) => {
  router.push({
    name: "PlanCreate",
    query: { planInfo: JSON.stringify(planInfo) },
  });
};
const handleDateRange = async (range) => {
  const planInfo = {
    title: "여행 제목",
    startDate: range.startDate,
    endDate: range.endDate,
  };
  const response = await createTripPlan(planInfo);
  goCreatePlan(response);
};

const changeFilter = (changedFilter) => {
  activeFilter.value = changedFilter;
  emit("changeFilter", changedFilter);
};
</script>

<style scoped>
/* Header Styles */
.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
  background: white;
  padding: 24px;
  border-radius: 8px;
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
}

.tabs {
  display: flex;
  gap: 24px;
}

.tab {
  background: none;
  border: none;
  padding: 8px 0;
  font-size: 16px;
  color: #666;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
}

.tab.active {
  color: #1a1a1a;
  border-bottom-color: #1a1a1a;
  font-weight: 500;
}

.header-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 16px;
}
</style>
