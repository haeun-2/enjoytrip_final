<template>
  <div class="travel-plans-container">
    <!-- Header Section -->
    <MyPlanHeader @changeFilter="changeFilter" />

    <!-- Travel Plans List -->
    <div class="trips-list">
      <MyPlanItemCard
        v-for="trip in trips"
        :key="trip.id"
        class="trip-card"
        :trip="trip"
        @deletePlan="handleDeletePlan"
      />
    </div>
    <BasePagination
      class="pagenation-container"
      :page="pagination"
      @changePage="handlePageChange"
    />

    <!-- Empty State -->
    <div v-if="trips.length === 0" class="empty-state">
      <div class="empty-icon">📅</div>
      <h3>여행 계획이 없습니다</h3>
      <p>새로운 여행을 계획해보세요!</p>
      <TripPlanCreatePopup @date-range-selected="handleDateRange" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { createTripPlan, getPlansWithStatus } from "@/apis/tripplan/plan";
import MyPlanHeader from "@/components/tripplan/mine/MyPlanHeader.vue";
import TripPlanCreatePopup from "@/components/tripplan/board/TripPlanCreatePopup.vue";
import BasePagination from "@/components/common/BasePagination.vue";
import MyPlanItemCard from "@/components/tripplan/mine/MyPlanItemCard.vue";
const activeFilter = ref("all");
const pagination = ref({});
const pageRequest = ref({});
const trips = ref([]);

const changeFilter = async (changedFilter) => {
  activeFilter.value = changedFilter;
  if (activeFilter.value === "all") {
    await handleFirstPage(null);
  } else {
    await handleFirstPage(activeFilter.value);
  }
};

const handleDeletePlan = (tripId) => {
  handleFirstPage(activeFilter.value);
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
const goCreatePlan = (planInfo) => {
  router.push({
    name: "PlanCreate",
    query: { planInfo: JSON.stringify(planInfo) },
  });
};

const handlePageChange = async (num) => {
  pageRequest.value.page = num - 1;
  const response = await getPlansWithStatus(
    activeFilter.value,
    pageRequest.value
  );
  trips.value = response.list;
  trips.value.forEach((trip) => (trip.status = calcStatus(trip)));

  const navSize = Math.min(response.totalPage, 5);
  pagination.value = {
    navSize,
    currentPage: response.currentPage + 1,
    startPage: response.startPage,
    endPage: response.startPage + navSize - 1,
    hasPre: response.hasPre,
    hasNext: response.hasNext,
  };
};

onMounted(async () => {
  handleFirstPage(null);
});

const handleFirstPage = async (status) => {
  pageRequest.value.page = 0;
  pageRequest.value.size = 4;
  const response = await getPlansWithStatus(status, pageRequest.value);
  trips.value = response.list;
  trips.value.forEach((trip) => (trip.status = calcStatus(trip)));
  const navSize = Math.min(response.totalPage, 5);
  pagination.value = {
    navSize,
    currentPage: 1,
    startPage: 1,
    endPage: navSize,
    hasPre: false,
    hasNext: response.hasNext,
  };
};
const calcStatus = (trip) => {
  const now = new Date();
  const startDate = new Date(trip.startDate);
  const endDate = new Date(trip.endDate);
  endDate.setDate(endDate.getDate() + 1);
  if (now < startDate) {
    return "upcoming";
  } else if (now >= startDate && now <= endDate) {
    return "ongoing";
  } else {
    return "completed";
  }
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}
.travel-plans-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  min-height: 100vh;
}

.trips-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.trip-card {
  display: flex;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.2s ease;
  height: 200px;
}

.trip-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 80px 20px;
  background: white;
  border-radius: 12px;
  border: 1px solid var(--background-color);
}
.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}
.empty-state h3 {
  margin: 0 0 8px 0;
  color: #1a1a1a;
  font-size: 18px;
  font-weight: 600;
}
.empty-state p {
  color: #666;
  margin-bottom: 24px;
  font-size: 14px;
}

/* 페이지네이션 */
.pagenation-container {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  padding: 10px;
  z-index: 10;
}
</style>
