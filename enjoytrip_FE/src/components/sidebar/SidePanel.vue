<template>
  <div class="side-tab-container">
    <!-- 사이드 탭 래퍼 -->
    <div
      class="side-tab-wrapper"
      :class="{ open: isOpen }"
      :style="{ width: panelWidth + 'px' }"
    >
      <div class="side-tab">
        <!-- 콘텐츠 영역 -->
        <slot></slot>
      </div>
      <!-- 리사이저 (드래그 핸들) -->
      <div
        class="resizer"
        @mousedown="startResize"
        @touchstart="startResize"
      ></div>

      <!-- 토글 버튼 -->
      <button
        class="toggle-button"
        @click="isOpen = !isOpen"
        aria-label="사이드 패널 열기/닫기"
      >
        <font-awesome-icon
          class="toggle-button-icon"
          :icon="isOpen ? 'chevron-left' : 'chevron-right'"
        />
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted, watch, computed } from "vue";
const props = defineProps({
  minSize: Number,
  maxSize: Number,
});

// 상태 관리
const minWidth = computed(() => props.minSize ?? minWidth);
const maxWidth = computed(() => props.maxSize ?? minWidth);

const isOpen = ref(true);
const panelWidth = ref(minWidth.value);
const isResizing = ref(false);
const startX = ref(0);
const startWidth = ref(0);

watch(maxWidth, (newMaxWidth) => {
  if (panelWidth.value > newMaxWidth) {
    panelWidth.value = newMaxWidth;
  }
});
watch(minWidth, (newMinWidth) => {
  if (panelWidth.value < newMinWidth) {
    panelWidth.value = newMinWidth;
  }
});
// 리사이징 시작
const startResize = (event) => {
  if (!isOpen.value) return;

  isResizing.value = true;

  // 마우스 이벤트와 터치 이벤트 모두 지원
  startX.value =
    event.type === "touchstart" ? event.touches[0].clientX : event.clientX;

  startWidth.value = panelWidth.value;

  // 이벤트 전파 방지
  event.preventDefault();
  event.stopPropagation();

  // 이벤트 리스너 추가
  document.addEventListener("mousemove", resize);
  document.addEventListener("touchmove", resize);
  document.addEventListener("mouseup", stopResize);
  document.addEventListener("touchend", stopResize);
};

// 리사이징 중
const resize = (event) => {
  if (!isResizing.value) return;

  // 마우스 이벤트와 터치 이벤트 모두 지원
  const clientX =
    event.type === "touchmove" ? event.touches[0].clientX : event.clientX;

  // 왼쪽에서 오른쪽으로 드래그하므로 차이를 더해줌
  // 최소 너비와 최대 너비 제한
  panelWidth.value = Math.max(
    minWidth.value,
    Math.min(maxWidth.value, startWidth.value + clientX - startX.value)
  );
};

// 리사이징 종료
const stopResize = () => {
  isResizing.value = false;

  // 이벤트 리스너 제거
  document.removeEventListener("mousemove", resize);
  document.removeEventListener("touchmove", resize);
  document.removeEventListener("mouseup", stopResize);
  document.removeEventListener("touchend", stopResize);
};

// 열림/닫힘 상태 감지해서 너비 조절
watch(isOpen, (newVal) => {
  if (newVal) {
    panelWidth.value = minWidth.value;
  }
});

// 컴포넌트 언마운트 시 이벤트 리스너 정리
onUnmounted(() => {
  document.removeEventListener("mousemove", resize);
  document.removeEventListener("touchmove", resize);
  document.removeEventListener("mouseup", stopResize);
  document.removeEventListener("touchend", stopResize);
});
</script>

<style>
.side-tab-container {
  position: relative;
  min-height: 100vh;
  overflow: auto;
}

/* 사이드 탭 스타일 */
.side-tab-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  background-color: white;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  z-index: 30;
  transition: transform 0.3s ease;
  overflow: visible; /* 토글 버튼이 바깥에 위치할 수 있도록 */
}

.side-tab-wrapper:not(.open) {
  transform: translateX(-100%);
}

/* 토글 버튼 스타일 */
.toggle-button {
  position: absolute;
  top: 50%;
  right: -25px; /* 패널 바깥에 위치 */
  transform: translateY(-50%);
  width: 25px;
  height: 50px;
  border-radius: 0 10px 10px 0;
  background-color: white;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.toggle-button-icon {
  color: black;
  font-size: 15px;
}

.side-tab {
  width: 100%;
  height: 100%;
  background-color: white;
  overflow-y: auto;
  position: relative;
}
.side-tab::-webkit-scrollbar {
  display: none;
} /* 스크롤바 숨김 */

/* 리사이저 (드래그 핸들) 스타일 */
.resizer {
  position: absolute;
  top: 0;
  right: 0;
  width: 6px;
  height: 100%;
  background-color: transparent;
  cursor: col-resize;
  z-index: 3;
}

.resizer:hover,
.resizer:active {
  background-color: rgba(0, 0, 0, 0.1);
}
</style>
