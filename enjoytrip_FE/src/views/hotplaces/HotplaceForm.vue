<template>
  <div class="container">
    <BaseBackButton :back="{ name: 'HotplaceList' }" />

    <div class="hotplace-form-container">
      <h1 class="form-title">
        {{ isEditPage ? "핫플레이스 수정" : "핫플레이스 등록" }}
      </h1>

      <!-- 핫플레이스 폼 -->
      <form @submit.prevent="submitHotplace" class="hotplace-form">
        <!-- 이름 입력 -->
        <div class="form-group">
          <label for="name" class="form-label">장소명</label>
          <input
            type="text"
            id="name"
            v-model="hotplace.name"
            class="form-input"
            placeholder="장소명을 입력하세요"
            required
          />
        </div>

        <!-- 주소 입력 -->
        <div class="form-group">
          <label for="address" class="form-label">주소</label>
          <div class="address-input-container">
            <input
              type="text"
              id="address"
              v-model="hotplace.address"
              class="form-input"
              placeholder="주소를 입력하세요"
              required
            />
            <button type="button" @click="searchAddress" class="search-address-button">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="search-icon"
              >
                <circle cx="11" cy="11" r="8"></circle>
                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
              </svg>
              주소 검색
            </button>
          </div>
        </div>

        <!-- 지도 표시 -->
        <div class="form-group">
          <label class="form-label">위치</label>
          <div id="map" class="map-container" ref="mapContainer"></div>
        </div>

        <!-- 시/도 선택 -->
        <div class="form-row">
          <div class="form-group half">
            <label for="sido" class="form-label">시/도</label>
            <select id="sido" v-model="hotplace.sidoId" class="form-select" @change="changeSido">
              <option value="" disabled selected>시/도 선택</option>
              <option v-for="sido in sidoList" :key="sido.id" :value="sido.id">
                {{ sido.sidoName }}
              </option>
            </select>
          </div>

          <div class="form-group half">
            <label for="gugun" class="form-label">구/군</label>
            <select
              id="gugun"
              v-model="hotplace.gugunId"
              class="form-select"
              :disabled="!hotplace.sidoId"
              @change="changeGugun"
            >
              <option value="" disabled selected>구/군 선택</option>
              <option v-for="gugun in gugunList" :key="gugun.id" :value="gugun.id">
                {{ gugun.gugunName }}
              </option>
            </select>
          </div>
        </div>

        <!-- 카테고리 선택 -->
        <div class="form-group">
          <label for="category" class="form-label">카테고리</label>
          <select id="category" v-model="hotplace.categoryId" class="form-select" required @change="changeCategory">
            <option value="" disabled selected>카테고리 선택</option>
            <option v-for="category in categoryList" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>

        <!-- 방문 날짜 -->
        <div class="form-group">
          <label for="visitedDate" class="form-label">다녀온 날짜</label>
          <input type="date" id="visitedDate" v-model="hotplace.visitedAt" class="form-input" required />
        </div>

        <!-- 이미지 업로드 -->
        <div class="form-group">
          <label class="form-label">이미지 업로드</label>
          <div class="image-upload-container">
            <label for="image-upload" class="image-upload-label">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="upload-icon"
              >
                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                <polyline points="17 8 12 3 7 8"></polyline>
                <line x1="12" y1="3" x2="12" y2="15"></line>
              </svg>
              <span>이미지 선택 또는 드래그 앤 드롭</span>
            </label>
            <input
              type="file"
              id="image-upload"
              @change="handleImageUpload"
              accept="image/*"
              class="image-upload-input"
            />
          </div>

          <!-- 이미지 미리보기 -->
          <div v-if="imagePreviewUrl" class="image-preview-container">
            <div class="image-preview-item">
              <img :src="imagePreviewUrl" alt="Preview" class="image-preview" />
              <button type="button" @click="removeImage" class="image-remove-button">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="remove-icon"
                >
                  <line x1="18" y1="6" x2="6" y2="18"></line>
                  <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
              </button>
            </div>
          </div>
        </div>

        <!-- 설명 입력 -->
        <div class="form-group">
          <label for="description" class="form-label">설명</label>
          <textarea
            id="description"
            v-model="hotplace.description"
            class="form-textarea"
            placeholder="장소에 대한 설명을 입력하세요"
            rows="5"
            required
          ></textarea>
        </div>

        <!-- 버튼 그룹 -->
        <div class="form-actions">
          <button type="button" @click="goBack" class="cancel-button">취소</button>
          <button type="submit" class="submit-button">저장하기</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import BaseBackButton from "@/components/common/BaseBackButton.vue";
import { ref, onMounted, computed, watch, useTemplateRef } from "vue";
import { useRouter, useRoute } from "vue-router";
import * as hotplaceApi from "@/apis/hotplaces/hotplaces.js";
import { useCategoryStore } from "@/stores/category.js";
import { useRegionStore } from "@/stores/region.js";
import { uploadImage } from "@/apis/common/upload.js";
import { useUserStore } from "@/stores/user";

// 수정 페이지
const route = useRoute();
const isEditPage = computed(() => route.name === "HotplaceEdit");
const id = isEditPage ? route.params.id : null;

// 핫플레이스 데이터
const hotplace = ref({
  name: "",
  address: "",
  sidoId: null,
  gugunId: null,
  categoryId: null,
  visitedAt: null,
  description: "",
  image: null,
  latitude: null,
  longitude: null,
});

const originalAddress = ref("");
const imageOriginalUrl = ref("");
// 이미지 미리보기 URL
const imagePreviewUrl = ref("");

// 뒤로 가기
const router = useRouter();
const goBack = () => {
  router.push({ name: "HotplaceList" });
};

// 시도, 구군, 카테고리 목록
const categoryStore = useCategoryStore();
const regionStore = useRegionStore();

const sidoList = ref([]);
const gugunList = ref([]);
const categoryList = ref(categoryStore.getCategoryList());

// 시도 변경 시 구군 목록 업데이트
watch(
  () => hotplace.value.sidoId,
  async () => {
    gugunList.value = await regionStore.getGugunList(hotplace.value.sidoId);
  }
);

const userStore = useUserStore();

// 이미지 업로드 처리
const handleImageUpload = (event) => {
  const file = event.target.files[0];
  if (!file) return;

  if (!file.type.match("image.*")) {
    alert("이미지 파일만 업로드 가능합니다.");
    return;
  }

  hotplace.value.image = file;

  const reader = new FileReader();
  reader.onload = (e) => {
    imagePreviewUrl.value = e.target.result;
  };
  reader.readAsDataURL(file);
};

// 이미지 제거
const removeImage = () => {
  imagePreviewUrl.value = "";
  hotplace.value.image = null;

  // 파일 입력 초기화
  const fileInput = document.getElementById("image-upload");
  if (fileInput) {
    fileInput.value = "";
  }
};

// 핫플레이스 등록
const submitHotplace = async () => {
  console.log("핫플레이스 :", hotplace.value);

  // 유효성 검사
  if (!hotplace.value.name.trim()) {
    alert("장소명을 입력해주세요.");
    return;
  }

  if (!hotplace.value.address.trim()) {
    alert("주소를 입력해주세요.");
    return;
  }

  if (hotplace.value.address.trim() != originalAddress.value.trim()) {
    searchAddress();
  }

  if (!hotplace.value.sidoId || !hotplace.value.gugunId) {
    alert("지역을 입력해주세요.");
    return;
  }

  if (!hotplace.value.categoryId) {
    alert("카테고리를 입력해주세요.");
    return;
  }

  if (!hotplace.value.visitedAt) {
    alert("다녀온 날짜를 입력해주세요.");
    return;
  }

  if (!hotplace.value.description.trim()) {
    alert("설명을 입력해주세요.");
    return;
  }

  if (imageOriginalUrl.value != imagePreviewUrl.value && hotplace.value.image) {
    hotplace.value.image = await uploadImage(hotplace.value.image);
  }

  if (!isEditPage.value) await createHotplace();
  else await editHotplace();
};

const createHotplace = async () => {
  try {
    const response = await hotplaceApi.createHotplace(hotplace.value);
    router.push({ name: "HotplaceList", query: { mine: true } });
  } catch (e) {
    alert("핫플 등록 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

const editHotplace = async () => {
  try {
    const response = await hotplaceApi.editHotplace(id, hotplace.value);
    router.push({ name: "HotplaceList", query: { mine: true } });
  } catch (e) {
    alert("핫플 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

// 카카오 맵
const mapContainer = useTemplateRef("mapContainer");
let map = null;
let marker = null;

// 지도 초기화
const initMap = () => {
  let position = null;
  if (isEditPage.value) position = new window.kakao.maps.LatLng(hotplace.value.latitude, hotplace.value.longitude);
  else position = new window.kakao.maps.LatLng(37.5662952, 126.9779451);

  const options = {
    center: position,
    level: 3,
  };

  // 지도 생성
  map = new window.kakao.maps.Map(mapContainer.value, options);

  // 마커 생성
  if (isEditPage.value) {
    marker = new window.kakao.maps.Marker({
      position: position,
      map: map,
    });
  }

  // 지도 크기 조정 (모달이 완전히 렌더링된 후)
  setTimeout(() => {
    map.relayout();
    map.setCenter(position); // center 재설정도 중요
  }, 100);
};

// 주소 검색
const searchAddress = () => {
  if (!hotplace.value.address.trim()) {
    alert("주소를 입력해주세요.");
    return;
  }

  const geocoder = new window.kakao.maps.services.Geocoder();
  geocoder.addressSearch(hotplace.value.address, (result, status) => {
    if (status === window.kakao.maps.services.Status.OK) {
      const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);

      // 좌표 저장
      hotplace.value.latitude = result[0].y;
      hotplace.value.longitude = result[0].x;

      // 지도 이동 및 마커 표시
      map.setCenter(coords);

      // 기존 마커 제거
      if (marker) {
        marker.setMap(null);
      }

      // 새 마커 생성
      marker = new window.kakao.maps.Marker({
        map: map,
        position: coords,
      });

      // 지도 확대 레벨 조정
      map.setLevel(3);
    } else {
      alert("주소를 찾을 수 없습니다. 다시 확인해주세요.");
    }
  });
};

onMounted(async () => {
  sidoList.value = await regionStore.getSidoList();

  if (isEditPage.value) {
    try {
      const { name, address, sido, gugun, category, visitedAt, description, image, latitude, longitude, member } =
        await hotplaceApi.detailHotplace(id);
      // 게시글 작성자인지 확인
      if (userStore.user.id != member.id) router.push({ name: "HotplaceList" });
      hotplace.value = {
        name,
        address,
        sidoId: sido.id,
        gugunId: gugun.id,
        categoryId: category.categoryId,
        visitedAt,
        description,
        image,
        latitude,
        longitude,
      };
      originalAddress.value = address;
      imageOriginalUrl.value = image;
      imagePreviewUrl.value = image;
    } catch (e) {
      alert("핫플 조회 중 오류가 발생했습니다.  다시 시도해주세요.");
    }
  }

  initMap();
});
</script>

<style>
/* 기본 스타일 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 16px;
  overflow-y: auto;
}

.hotplace-form-container {
  margin-top: 0;
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.hotplace-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 주소 검색 스타일 */
.address-input-container {
  display: flex;
  gap: 8px;
}

.address-input-container .form-input {
  width: 90%;
}

.search-address-button {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 0 16px;
  background-color: var(--background-color);
  color: var(--text-light);
  border: 1px solid var(--border-color);
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.2s;
  white-space: nowrap;
}

.search-icon {
  color: inherit;
}

/* 지도 스타일 */
.map-container {
  width: 100%;
  height: 300px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  overflow: hidden;
}

/* 이미지 업로드 스타일 */
.image-upload-container {
  border: 2px dashed var(--border-color);
  border-radius: 4px;
  padding: 24px;
  text-align: center;
  transition: border-color 0.2s;
  cursor: pointer;
}

.image-upload-container:hover {
  border-color: var(--primary-light);
}

.image-upload-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: var(--text-light);
}

.upload-icon {
  color: var(--primary-color);
}

.image-upload-input {
  display: none;
}

.image-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 16px;
}

.image-preview-item {
  position: relative;
  width: 200px;
  height: 200px;
  border-radius: 4px;
  overflow: hidden;
}

.image-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-remove-button {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: var(--background-color);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
}

.image-remove-button:hover {
  background-color: var(--background-color);
}

.remove-icon {
  color: var(--error-color);
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .address-input-container {
    flex-direction: column;
  }

  .address-input-container .form-input {
    width: 100%;
  }

  .search-address-button {
    width: 100%;
    padding: 10px;
    justify-content: center;
  }
}

@media (max-width: 640px) {
  .container {
    padding: 16px;
  }
}
</style>
