<template>
  <div class="timeline-item">
    <!-- 마커 및 번호 -->
    <div class="timeline-marker">
      <div
        class="marker-number"
        :style="{
          fontSize: '12px',
          backgroundColor: markerColor[dayOrder],
        }"
      >
        {{ itemOrder }}
      </div>
    </div>
    <!-- 콘텐츠 영역 -->
    <div class="timeline-content">
      <p style="font-size: 12px; color: #757575">{{ placeAddress }}</p>
      <!-- 장소 이름 -->
      <div class="place-name">{{ placeName }}</div>
      <!-- 장소 유형 -->
      <div class="place-type" :class="typeClass">{{ placeType }}</div>
    </div>
    <!-- 장소 이미지 -->
    <div class="place-image">
      <img :src="imageUrl || noImage" :alt="placeName" />
    </div>
    <!-- 이동 시간 정보 (있는 경우) -->
    <div v-if="transportTime" class="transport-info">
      <div class="transport-icon">
        <font-awesome-icon icon="car" />
      </div>
      <div class="transport-time">{{ transportTime }}</div>
    </div>
    <div v-else class="transport-info">없음</div>
  </div>
</template>

<script setup>
import noImage from "@/assets/noimage.jpeg";
import { computed } from "vue";
const markerColor = [
  "#f44336",
  "#fb8500",
  "#ffc300",
  "#80b918",
  "#1f7a8c",
  "#023047",
  "#5a189a",
];

// 컴포넌트 props 정의
const props = defineProps({
  dayOrder: {
    type: Number,
    required: true,
  },
  itemOrder: {
    type: Number,
    required: true,
  },
  itemNumber: {
    type: Number,
    required: true,
  },
  placeType: {
    type: String,
    required: true,
  },
  placeName: {
    type: String,
    required: true,
  },
  placeAddress: {
    type: String,
    required: true,
  },
  imageUrl: {
    type: String,
    required: true,
  },
  transportTime: {
    type: String,
    default: null,
  },
});

// 장소 유형에 따른 클래스 계산
const typeClass = computed(() => {
  const typeMap = {
    명소: "type-attraction",
    숙소: "type-accommodation",
    식당: "type-restaurant",
    카페: "type-cafe",
    쇼핑: "type-shopping",
  };

  return typeMap[props.placeType] || "type-default";
});
</script>

<style scoped>
.timeline-item {
  display: flex;
  position: relative;
  margin-bottom: 24px;
  padding-bottom: 16px;
  width: 350px;
}

/* 타임라인 마커 스타일 */
.timeline-marker {
  position: relative;
  width: 40px;
  height: 40px;
  margin-right: 16px;
  z-index: 1;
}

.marker-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #e74c3c;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
}

/* 콘텐츠 영역 스타일 */
.timeline-content {
  flex: 1;
}

.place-type {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
  margin-bottom: 8px;
}

.type-attraction {
  background-color: #e3f2fd;
  color: #2196f3;
}

.type-accommodation {
  background-color: #e8f5e9;
  color: #4caf50;
}

.type-restaurant {
  background-color: #ffebee;
  color: #f44336;
}

.type-cafe {
  background-color: #fff8e1;
  color: #ffc107;
}

.type-shopping {
  background-color: #f3e5f5;
  color: #9c27b0;
}

.type-default {
  background-color: #f5f5f5;
  color: #757575;
}

.place-name {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

/* 이미지 스타일 */
.place-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-left: 16px;
}

.place-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 이동 시간 정보 스타일 */
.transport-info {
  position: absolute;
  bottom: -8px;
  left: 0px;
  display: flex;
  align-items: center;
  padding: 4px 8px;
  /* background-color: #f5f5f5; */
  /* border-radius: 12px; */
  font-size: 12px;
  color: #757575;
  /* box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1); */
}

.transport-icon {
  background-color: white;
  border-radius: 12px;
  padding: 4px 0px;
  margin-right: 4px;
  display: flex;
  align-items: center;
  font-size: 16px;
  color: #adadad;
}

.transport-time {
  font-weight: 500;
  margin-left: 8px;
}
</style>
