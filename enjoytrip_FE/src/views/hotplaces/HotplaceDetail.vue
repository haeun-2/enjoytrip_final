<template>
  <div v-show="selectedPlace" class="modal-overlay" @click="closeModal">
    <div class="modal-container" @click.stop>
      <button class="modal-close-button" @click="closeModal">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <line x1="18" y1="6" x2="6" y2="18"></line>
          <line x1="6" y1="6" x2="18" y2="18"></line>
        </svg>
      </button>

      <div class="modal-content">
        <div class="modal-image-container">
          <img :src="selectedPlace?.image" alt="Hotplace" class="modal-image" />
          <div class="modal-category">
            <BaseBadge :text="selectedPlace?.category.categoryName" />
          </div>
        </div>

        <div class="modal-details">
          <div class="title-like-container">
            <h2 class="modal-title">{{ selectedPlace?.name }}</h2>
            <button @click="likeHotplace" class="like-button">
              <font-awesome-icon
                :icon="[selectedPlace?.isLiked ? 'fa' : 'far', 'heart']"
                :style="{
                  fontSize: '15px',
                  color: 'red',
                }"
              />
              <span>좋아요</span>
            </button>
          </div>

          <div class="modal-info-grid">
            <div class="modal-info-item">
              <div class="info-label">주소</div>
              <div class="info-value">{{ selectedPlace?.address }}</div>
            </div>

            <div class="modal-info-item">
              <div class="info-label">지역</div>
              <div class="info-value">
                {{ selectedPlace?.sido.sidoName }}
                {{ selectedPlace?.gugun.gugunName }}
              </div>
            </div>

            <div class="modal-info-item">
              <div class="info-label">카테고리</div>
              <div class="info-value">
                {{ selectedPlace?.category.categoryName }}
              </div>
            </div>

            <div class="modal-info-item">
              <div class="info-label">방문일</div>
              <div class="info-value">
                {{ formatDate(selectedPlace?.visitedAt) }}
              </div>
            </div>
          </div>

          <div class="modal-description">
            <div class="info-label">설명</div>
            <p class="description-text">{{ selectedPlace?.description }}</p>
          </div>

          <div class="modal-map">
            <div class="info-label">위치</div>

            <div class="map-container" ref="mapContainer"></div>
          </div>

          <BaseButtonEditDelete
            @editClick="editPlace"
            @deleteClick="confirmDeletePlace"
            v-if="userStore.isAuthor(selectedPlace?.member.id)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, useTemplateRef, watch, onMounted } from "vue";
import { formatDate } from "@/utils/date.js";
import BaseBadge from "@/components/common/BaseBadge.vue";
import { useRouter } from "vue-router";
import BaseButtonEditDelete from "@/components/common/BaseButtonEditDelete.vue";
import { useUserStore } from "@/stores/user";
import hotplaces from "@/router/hotplaces";
import * as hotplaceApi from "@/apis/hotplaces/hotplaces";

const router = useRouter();

const props = defineProps(["selectedPlace"]);

// 로그인 유저 확인
const userStore = useUserStore();

const emit = defineEmits(["closeModal", "deletePlace", "likePlace"]);

// 모달 닫기
const closeModal = () => {
  emit("closeModal");
};

// 핫플레이스 수정
const editPlace = () => {
  router.push({ name: "HotplaceEdit", params: { id: props.selectedPlace.id } });
};

// 핫플레이스 삭제
const confirmDeletePlace = () => {
  if (confirm(`정말로 "${props.selectedPlace.name}"을(를) 삭제하시겠습니까?`)) {
    emit("deletePlace", props.selectedPlace);
  }
};

// 좋아요
const likeHotplace = async () => {
  if (!userStore.isLogined) {
    alert("로그인 후 이용해주세요");
    return;
  }
  try {
    if (props.selectedPlace?.isLiked) {
      await hotplaceApi.likeHotplace(props.selectedPlace.id);
      emit("likePlace", props.selectedPlace, false);
    } else {
      await hotplaceApi.deletelikeHotplace(props.selectedPlace.id);
      emit("likePlace", props.selectedPlace, true);
    }
  } catch (e) {
    alert("좋아요 업데이트 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// 카카오 맵
const mapContainer = useTemplateRef("mapContainer");

// 지도 초기화
const initMap = () => {
  if (!props.selectedPlace) return;

  const position = new window.kakao.maps.LatLng(props.selectedPlace.latitude, props.selectedPlace.longitude);

  const options = {
    center: position,
    level: 3,
  };

  // 지도 생성
  const map = new window.kakao.maps.Map(mapContainer.value, options);

  // 마커 생성
  const marker = new window.kakao.maps.Marker({
    position: position,
    map: map,
  });

  // 지도 크기 조정 (모달이 완전히 렌더링된 후)
  setTimeout(() => {
    map.relayout();
    map.setCenter(position); // center 재설정도 중요
  }, 100);
};

watch(
  () => props.selectedPlace,
  () => {
    initMap();
  }
);
</script>

<style scoped>
.modal-container {
  position: relative;
  width: 100%;
  max-width: 1000px;
  max-height: 90vh;
  background-color: white;
  border-radius: 0.5rem;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-close-button {
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
  width: 2rem;
  height: 2rem;
  border-radius: 9999px;
  background-color: rgba(255, 255, 255, 0.8);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: background-color 0.2s;
}

.modal-close-button:hover {
  background-color: rgba(255, 255, 255, 1);
}

.modal-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

@media (min-width: 768px) {
  .modal-content {
    flex-direction: row;
  }
}

.modal-image-container {
  position: relative;
  height: 200px;
  flex-shrink: 0;
}

@media (min-width: 768px) {
  .modal-image-container {
    width: 40%;
    height: auto;
  }
}

.modal-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.modal-category {
  position: absolute;
  top: 0.5rem;
  left: 0.5rem;
  padding: 0.25rem 0.5rem;
  color: white;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.modal-details {
  padding: 1.5rem;
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 0.5rem;
}

.modal-info-grid {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;
}

@media (min-width: 640px) {
  .modal-info-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

.modal-info-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.info-label {
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--text-light);
}

.info-value {
  font-size: 0.875rem;
  color: var(--text-color);
}

.modal-description {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.description-text {
  font-size: 0.875rem;
  line-height: 1.5;
  color: var(--text-color);
}

.modal-map {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.map-container {
  width: 100%;
  height: 200px;
  border-radius: 0.25rem;
  overflow: hidden;
  border: 1px solid var(--border-color);
}

/* 좋아요 버튼 */
.title-like-container {
  display: flex;
  justify-content: space-between;
  margin-right: 10px;
}

.like-button {
  align-self: center;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: var(--text-light);
  border-radius: var(--border-radius);
  font-weight: 500;
  cursor: pointer;
  border: none;
  background-color: white;
}

.heart-icon {
  color: var(--heart-color);
}
</style>
