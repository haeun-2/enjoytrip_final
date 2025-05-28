<template>
  <div class="popup-overlay">
    <div class="popup-container">
      <button class="close-button" @click="handleClick">
        <font-awesome-icon icon="xmark" style="font-size: 20px" />
      </button>
      <div class="popup-content">
        <div class="destination-image">
          <img :src="props.place.image" alt="여행지 이미지" />
        </div>

        <div class="destination-info">
          <div class="destination-header">
            <div class="destination-meta">
              <span class="destination-category">{{
                props.place.category
              }}</span>
              <div class="likes-container">
                <font-awesome-icon
                  icon="heart"
                  style="color: var(--heart-color)"
                />
                <span class="likes-count">{{ props.place.likeCount }}</span>
              </div>
            </div>
            <h2 class="destination-name">{{ props.place.name }}</h2>
          </div>

          <div class="destination-address">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              class="address-icon"
            >
              <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
              <circle cx="12" cy="10" r="3"></circle>
            </svg>
            <span>{{ props.place.address }}</span>
          </div>

          <div class="destination-description">
            <h3>상세 설명</h3>
            <p v-if="props.place.description">{{ props.place.description }}</p>
            <p v-else>상세 설명이 없습니다.</p>
          </div>

          <div class="action-buttons">
            <button class="add-button" @click="handleAdd">일정에 추가</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  place: Object,
});

const emit = defineEmits(["close", "add"]);
const handleClick = () => {
  emit("close");
};
const handleAdd = () => {
  emit("add", props.place.id);
};
</script>

<style scoped>
/* 오버레이 스타일 */
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 99;
}

/* 팝업 컨테이너 스타일 */
.popup-container {
  width: 40%;
  height: 40%;
  min-width: 320px;
  min-height: 500px;
  max-height: 80vh;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  z-index: 100;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* 닫기 버튼 스타일 */
.close-button {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 101;
  transition: background-color 0.2s;
}

.close-button:hover {
  background-color: rgba(255, 255, 255, 1);
}

.close-icon {
  color: #333;
  width: 20px;
  height: 20px;
}

/* 팝업 내용 스타일 */
.popup-content {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow-y: auto;
}

/* 여행지 이미지 스타일 */
.destination-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.destination-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 여행지 정보 스타일 */
.destination-info {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 여행지 헤더 스타일 */
.destination-header {
  margin-bottom: 8px;
}

.destination-name {
  font-size: 24px;
  font-weight: 700;
  color: #333;
  margin: 0;
}

.destination-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.destination-category {
  font-size: 14px;
  color: #666;
  background-color: #f0f0f0;
  padding: 4px 10px;
  border-radius: 16px;
}

.likes-container {
  display: flex;
  align-items: center;
  gap: 6px;
}

.like-icon {
  color: var(--heart-color);
}

.likes-count {
  font-size: 14px;
  color: #666;
}

/* 주소 스타일 */
.destination-address {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #555;
  font-size: 14px;
  margin-bottom: 16px;
}

.address-icon {
  color: #666;
}

/* 설명 스타일 */
.destination-description {
  flex: 1;
}

.destination-description h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
}

.destination-description p {
  font-size: 14px;
  line-height: 1.6;
  color: #555;
}

/* 액션 버튼 스타일 */
.action-buttons {
  display: flex;
  gap: 12px;
  margin-top: 16px;
}

.add-button,
.share-button {
  padding: 10px 16px;
  border-radius: 8px;
  border: none;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.add-button {
  background-color: var(--primary-color);
  color: white;
  flex: 1;
}

.add-button:hover {
  background-color: var(--primary-hover);
}

/* 반응형 스타일 */
@media (min-width: 768px) {
  .popup-container {
    width: 600px;
    height: 600px;
  }

  .destination-image {
    height: 250px;
  }
}

@media (max-width: 767px) {
  .popup-container {
    width: 90%;
    height: 80%;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>
