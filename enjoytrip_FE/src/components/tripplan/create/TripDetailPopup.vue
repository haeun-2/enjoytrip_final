<template>
  <div class="popup-overlay">
    <div class="popup-container">
      <button class="close-button" @click="handleClick">
        <font-awesome-icon icon="xmark" style="font-size: 20px" />
      </button>
      <div class="popup-content">
        <div class="destination-image">
          <img :src="props.place.image1 || noImage" alt="여행지 이미지" />
        </div>

        <div class="destination-info">
          <div class="destination-header">
            <div class="destination-meta">
              <span class="destination-category">{{
                props.place.category.categoryName
              }}</span>
              <div class="likes-container">
                <font-awesome-icon
                  icon="heart"
                  style="color: var(--text-light)"
                />
                <span class="likes-count">{{ props.place.likeCount }}</span>
              </div>
            </div>
            <h2 class="destination-name">{{ props.place.title }}</h2>
          </div>
          <div class="action-buttons">
            <button class="action-btn" @click="goToKakaoMap">
              <font-awesome-icon class="icon" icon="location-arrow" />
              <span>길찾기</span>
            </button>
            <button class="action-btn">
              <font-awesome-icon class="icon" icon="phone" />
              <span>전화</span>
            </button>
            <button class="action-btn" @click="toggleLike">
              <font-awesome-icon
                class="icon"
                icon="heart"
                :style="{
                  color: place.liked ? 'var(--heart-color)' : 'black',
                }"
              />
              <span>좋아요</span>
            </button>
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
            <span>{{ props.place.address1 }}</span>
          </div>

          <div class="destination-description">
            <h3>웹사이트</h3>
            <p v-if="props.place.hompage">{{ props.place.hompage }}</p>
            <p v-else>웹사이트 정보가 없습니다.</p>
          </div>
          <div class="destination-description">
            <h3>연락처</h3>
            <p v-if="props.place.tel">{{ props.place.tel }}</p>
            <p v-else>연락처 정보가 없습니다.</p>
          </div>
          <div class="destination-description">
            <h3>상세 설명</h3>
            <p v-if="props.place.description">{{ props.place.overview }}</p>
            <p v-else>상세 설명이 없습니다.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import noImage from "@/assets/noImage.jpeg";
import { likeAttraction, unlikeAttraction } from "@/apis/attraction/attraction";

const props = defineProps({
  place: Object,
});

const emit = defineEmits(["close"]);
const handleClick = () => {
  emit("close");
};

const goToKakaoMap = () => {
  const url = `https://map.kakao.com/link/to/${props.place.title},${props.place.latitude},${props.place.longitude}`;
  window.open(url, "_blank"); // 새 탭 열기
  // 또는 location.href = url;  // 현재 창에서 이동
};

const toggleLike = async () => {
  if (props.place.liked) {
    // 취소
    await unlikeAttraction(props.place.id);
    props.place.liked = false;
    props.place.likeCount -= 1;
  } else {
    // 등록
    await likeAttraction(props.place.id);
    props.place.liked = true;
    props.place.likeCount += 1;
  }
};
</script>

<style scoped>
/* 오버레이 스타일 */
.popup-overlay {
  z-index: 20;
}

/* 팝업 컨테이너 스타일 */
.popup-container {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* 닫기 버튼 스타일 */
.close-button {
  position: absolute;
  top: 24px;
  right: 24px;
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
  height: 350px;
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
  /* flex: 1; */
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
  margin: 0 0 8px 0;
}

.destination-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 5px;
}

.destination-category {
  font-size: 14px;
  color: #666;
  background-color: #f0f0f0;
  padding: 3px 10px;
  border-radius: 12px;
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

.action-buttons {
  display: flex;
  gap: 10px;
}

.action-btn {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 0;
  border: 1px solid #ddd;
  background-color: white;
  border-radius: 8px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
}

.action-btn .icon {
  font-size: 18px;
  margin-bottom: 4px;
}

.action-btn:hover {
  background-color: var(--background-color);
}

.action-btn .icon.active {
  color: var(--heart-color);
}

/* 주소 스타일 */
.destination-address {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #555;
  font-size: 14px;
  margin-bottom: 12px;
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
  margin: 0 0 2px 0;
}

.destination-description p {
  font-size: 14px;
  line-height: 1.6;
  color: #555;
  margin: 4px 0;
}
</style>
