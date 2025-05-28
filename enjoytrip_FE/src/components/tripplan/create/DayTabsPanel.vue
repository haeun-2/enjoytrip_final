<template>
  <div class="day-tabs-panel">
    <!-- 일차 탭 헤더 -->
    <div class="day-tabs-header">
      <div
        v-for="day in totalDays"
        :key="day"
        class="day-tab"
        :class="{ active: selectedDay === day }"
        @click="selectDay(day)"
      >
        {{ day }}일차
      </div>
    </div>

    <!-- 선택된 일차의 장소 목록 패널 -->
    <div class="day-places-panel" v-if="currentDayPlaces.length > 0">
      <div class="panel-header">
        <h3>{{ selectedDay }}일차 선택 장소</h3>
        <span class="place-count">{{ currentDayPlaces.length }}개 장소</span>
      </div>

      <transition-group
        name="place-move"
        tag="div"
        class="selected-places-list"
        ref="placesList"
      >
        <div
          v-for="(placeId, index) in currentDayPlaces"
          :key="placeId"
          class="selected-place-item"
          :class="{ dragging: draggedIndex === index }"
          draggable="true"
          @dragstart="dragStart($event, index, placeId)"
          @dragover.prevent
          @dragenter.prevent="dragEnter($event, index)"
          @drop="drop($event, index)"
          @dragend="dragEnd()"
        >
          <div class="drag-handle">
            <font-awesome-icon icon="bars" style="font-size: 13px" />
          </div>
          <div class="place-order">{{ index + 1 }}</div>
          <div class="place-info">
            <h4>{{ getPlaceById(placeId)?.name || "장소 정보 없음" }}</h4>
            <p>{{ getPlaceById(placeId)?.description || "" }}</p>
          </div>
          <button
            class="remove-place-btn"
            @click.stop="handleRemovePlace(placeId)"
          >
            <font-awesome-icon icon="xmark" />
          </button>

          <!-- 드래그 오버 표시기 -->
          <div
            v-if="dragOverIndex === index && draggedIndex !== index"
            class="drag-indicator"
            :class="{
              'drag-indicator-top': draggedIndex > index,
              'drag-indicator-bottom': draggedIndex < index,
            }"
          ></div>
        </div>
      </transition-group>
    </div>

    <!-- 선택된 장소가 없을 때 표시할 메시지 -->
    <div class="empty-places-message" v-else>
      <p>{{ selectedDay }}일차에 선택된 장소가 없습니다.</p>
      <p>왼쪽 패널에서 장소를 선택해주세요.</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, nextTick } from "vue";

// Props 정의
const props = defineProps({
  selectedDay: {
    type: Number,
    required: true,
  },
  totalDays: {
    type: Number,
    required: true,
  },
  dayPlaces: {
    type: Object,
    required: true,
  },
  placesCache: {
    type: Object,
    required: true,
  },
});

// Emits 정의
const emit = defineEmits([
  "update:selectedDay",
  "removePlace",
  "reorderPlaces",
]);

// 현재 선택된 일차의 장소 목록
const currentDayPlaces = computed(() => {
  return props.dayPlaces[props.selectedDay] || [];
});

// 드래그 앤 드롭 관련 상태
const draggedIndex = ref(null);
const dragOverIndex = ref(null);
const draggedPlaceId = ref(null);
const placesList = ref(null);

// 일차 선택 함수
const selectDay = (day) => {
  emit("update:selectedDay", day);
};

// 장소 ID로 장소 정보 가져오기
const getPlaceById = (placeId) => {
  return props.placesCache[placeId];
};

// 장소 제거 함수
const handleRemovePlace = (placeId) => {
  emit("removePlace", placeId);
};

// 드래그 시작 이벤트 핸들러
const dragStart = (event, index, placeId) => {
  // 드래그 중인 항목 정보 저장
  draggedIndex.value = index;
  draggedPlaceId.value = placeId;
  dragOverIndex.value = index;

  // 드래그 데이터 설정
  event.dataTransfer.effectAllowed = "move";
  event.dataTransfer.setData(
    "text/plain",
    JSON.stringify({
      index,
      placeId,
    })
  );

  // 드래그 중인 요소에 스타일 적용
  nextTick(() => {
    event.target.classList.add("dragging");
  });
};

// 드래그 엔터 이벤트 핸들러
const dragEnter = (event, index) => {
  // 드래그 오버 인덱스 업데이트
  if (draggedIndex.value !== null && index !== draggedIndex.value) {
    dragOverIndex.value = index;
  }
};

// 드롭 이벤트 핸들러
const drop = (event, dropIndex) => {
  event.preventDefault();

  // 드래그 데이터 가져오기
  const data = event.dataTransfer.getData("text/plain");
  let dragData;

  try {
    dragData = JSON.parse(data);
  } catch (e) {
    console.error("Invalid drag data:", e);
    return;
  }

  const { index: fromIndex } = dragData;

  // 같은 위치에 드롭한 경우 무시
  if (fromIndex === dropIndex) {
    return;
  }

  // 부모 컴포넌트에 순서 변경 이벤트 발생
  emit("reorderPlaces", {
    day: props.selectedDay,
    fromIndex,
    toIndex: dropIndex,
  });

  // 드래그 상태 초기화
  dragEnd();
};

// 드래그 종료 이벤트 핸들러
const dragEnd = () => {
  // 드래그 상태 초기화
  draggedIndex.value = null;
  dragOverIndex.value = null;
  draggedPlaceId.value = null;

  // 모든 드래그 관련 클래스 제거
  const items = document.querySelectorAll(".selected-place-item");
  items.forEach((item) => {
    item.classList.remove("dragging");
  });
};
</script>

<style scoped>
/* 오른쪽 일차 탭 패널 스타일 */
.day-tabs-panel {
  width: 400px;
  height: 100%;
  border-left: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
  background-color: white;
}

.day-tabs-header {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
  overflow-x: auto;
  padding: 0 8px;
}

.day-tab {
  padding: 12px 16px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  position: relative;
  color: #666;
}

.day-tab.active {
  color: #2196f3;
  font-weight: 600;
}

.day-tab.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #2196f3;
}

.day-places-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  padding: 16px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.place-count {
  font-size: 14px;
  color: #666;
}

.selected-places-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
  position: relative;
}

.selected-place-item {
  display: flex;
  align-items: center;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 8px;
  background-color: #f9f9f9;
  position: relative;
  cursor: grab;
  transition: transform 0.2s ease, background-color 0.2s ease,
    box-shadow 0.2s ease;
}

.selected-place-item:hover {
  background-color: #f0f0f0;
}

.selected-place-item.dragging {
  opacity: 0.6;
  background-color: #e3f2fd;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.drag-handle {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  margin-right: 8px;
  cursor: grab;
}

.place-order {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #2196f3;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  margin-right: 12px;
  flex-shrink: 0;
}

.place-info {
  flex: 1;
  min-width: 0;
}

.place-info h4 {
  margin: 0 0 4px;
  font-size: 14px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.place-info p {
  margin: 0;
  font-size: 12px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.remove-place-btn {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: transparent;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #666;
  margin-left: 8px;
  flex-shrink: 0;
}

.remove-place-btn:hover {
  background-color: #e0e0e0;
}

/* 드래그 인디케이터 스타일 */
.drag-indicator {
  position: absolute;
  left: 0;
  right: 0;
  height: 4px;
  background-color: #2196f3;
  z-index: 5;
}

.drag-indicator-top {
  top: -2px;
}

.drag-indicator-bottom {
  bottom: -2px;
}

.empty-places-message {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24px;
  color: #666;
  text-align: center;
}

.empty-places-message p {
  margin: 4px 0;
  font-size: 14px;
}

@media (max-width: 1200px) {
  .day-tabs-panel {
    width: 250px;
  }
}

@media (max-width: 768px) {
  .day-tabs-panel {
    width: 100%;
    height: auto;
    border-left: none;
    border-top: 1px solid #e0e0e0;
  }
}

.place-move-move {
  transition: transform 0.3s ease;
}
</style>
