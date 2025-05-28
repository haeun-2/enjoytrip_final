<template>
  <div>
    <div class="trip-image-container">
      <img
        src="https://images.unsplash.com/photo-1507525428034-b723cf961d3e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80"
        alt="제주도 해변"
      />
      <div v-if="trip.status !== 'ongoing'" class="d-day-badge">
        {{ calculateDDay(trip.startDate, trip.endDate, trip.status) }}
      </div>
    </div>

    <div class="trip-content">
      <div class="trip-main-info">
        <h3 class="trip-destination">{{ trip.title }}</h3>
        <p class="trip-description">
          {{ trip.description || "여행 이름 입력" }}
        </p>
        <div class="trip-dates">
          {{ formatDate(trip.startDate) }} ~ {{ formatDate(trip.endDate) }}
        </div>

        <div class="trip-icons">
          <div class="trip-icon">
            <font-awesome-icon icon="fa-pen-to-square" />
            <button @click="editTrip(trip.id)">수정</button>
          </div>
          <div class="trip-icon" @click="deleteTrip(trip.id)">
            <font-awesome-icon icon="fa-trash" />
            <button>삭제</button>
          </div>
        </div>
      </div>

      <div class="trip-meta">
        <div :class="['status-badge', `status-${trip.status}`]">
          {{ statusMap[trip.status] }}
        </div>
        <div class="last-modified">최근수정일 {{ trip.lastModified }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { deleteTripPlan } from "@/apis/tripplan/plan";

const { trip } = defineProps({
  trip: Object,
});
const emit = defineEmits(["deletePlan"]);
const statusMap = {
  upcoming: "예정",
  ongoing: "진행중",
  completed: "종료",
};
function formatDate(dateString) {
  const date = new Date(dateString);
  return date
    .toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    })
    .replace(/\./g, "-")
    .replace(/ /g, "")
    .slice(0, -1);
}

function calculateDDay(startDate, endDate, status) {
  const now = new Date();
  const start = new Date(startDate);
  const end = new Date(endDate);
  const msPerDay = 1000 * 60 * 60 * 24;

  if (status === "upcoming") {
    const diff = Math.ceil((start - now) / msPerDay);
    return `D-${diff}`;
  } else if (status === "completed") {
    const diff = Math.ceil((now - end) / msPerDay);
    return `D+${diff}`;
  }
  return "";
}

const deleteTrip = async (tripId) => {
  const response = await deleteTripPlan(tripId);
  emit("deletePlan", tripId);
};
</script>

<style scoped>
/* Trip Cards */

.trip-image-container {
  position: relative;
  width: 280px;
  height: 100%;
  flex-shrink: 0;
}

.trip-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.d-day-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.trip-content {
  flex: 1;
  padding: 20px;
  padding-bottom: 0px;
  display: flex;
  justify-content: space-between;
}

.trip-main-info {
  flex: 1;
}

.trip-destination {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
}

.trip-description {
  margin: 0 0 12px 0;
  color: #999;
  font-size: 14px;
}

.trip-dates {
  margin-bottom: 20px;
  color: #1a1a1a;
  font-size: 16px;
  font-weight: 500;
}

/* 아이콘 버튼들 */
.trip-icons {
  display: flex;
  gap: 8px;
  position: relative;
  bottom: -12px;
}

.trip-icon {
  display: flex;
  align-items: center;
  gap: 2px;
  padding: 6px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: white;
  color: #666;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
}
.trip-icon button {
  display: flex;
  align-items: center;
  border: none;
  border-radius: 8px;
  background: white;
  color: #666;
  font-size: 13px;
  cursor: pointer;
  padding: 2px;
  transition: all 0.2s ease;
}
.trip-icon:hover {
  border-color: #2962ff;
  color: #2962ff;
  background: #f8fafe;
}

.trip-icon button:hover {
  border-color: #2962ff;
  color: #2962ff;
  background: #f8fafe;
}

.trip-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  gap: 12px;
  padding-bottom: 24px;
  position: relative;
}

.last-modified {
  color: #999;
  font-size: 12px;
  white-space: nowrap;
}

.status-badge {
  display: inline-block;
  padding: 1px 10px;
  height: 24px;
  line-height: 24px; /* 높이와 동일하게 */
  border-radius: 15px;
  font-size: 13px;
  color: white;
  text-align: center;
}

.status-ongoing {
  background-color: #e0f2fe;
  color: #0369a1;
}

.status-upcoming {
  background-color: #fef9c3;
  color: #b66c19;
}

.status-completed {
  background-color: #f3f4f6;
  color: #4b5563;
}
</style>
