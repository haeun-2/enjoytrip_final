<template>
  <div class="profile-header">
    <div class="profile-image-container">
      <div class="profile-image">
        <img
          v-if="userProfile.profileImage"
          :src="userProfile.profileImage"
          alt="프로필 이미지"
        />
        <img v-else src="@/assets/profile-default.jpg" />
      </div>
      <button @click="showImageUpload = true" class="change-image-button">
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
          class="camera-icon"
        >
          <path
            d="M14.5 4h-5L7 7H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-3l-2.5-3z"
          ></path>
          <circle cx="12" cy="13" r="3"></circle>
        </svg>
      </button>
    </div>

    <div class="profile-info">
      <h1 class="profile-name">{{ userProfile.name }}</h1>
      <p class="profile-email">{{ userProfile.email }}</p>
      <div class="profile-meta">
        <div class="meta-item">
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
            class="calendar-icon"
          >
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
            <line x1="16" y1="2" x2="16" y2="6"></line>
            <line x1="8" y1="2" x2="8" y2="6"></line>
            <line x1="3" y1="10" x2="21" y2="10"></line>
          </svg>
          <span>가입일: {{ formatDate(userProfile.createdAt) }}</span>
        </div>
        <div class="meta-item">
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
            class="gift-icon"
          >
            <polyline points="20 12 20 22 4 22 4 12"></polyline>
            <rect x="2" y="7" width="20" height="5"></rect>
            <line x1="12" y1="22" x2="12" y2="7"></line>
            <path d="M12 7H7.5a2.5 2.5 0 0 1 0-5C11 2 12 7 12 7z"></path>
            <path d="M12 7h4.5a2.5 2.5 0 0 0 0-5C13 2 12 7 12 7z"></path>
          </svg>
          <span>생년월일: {{ userProfile.birthDate }}</span>
        </div>
      </div>
    </div>

    <!-- 프로필 이미지 업로드 모달 -->
    <div
      v-if="showImageUpload"
      class="modal-overlay"
      @click="showImageUpload = false"
    >
      <div class="modal-container" @click.stop>
        <button class="modal-close-button" @click="showImageUpload = false">
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
          <h2 class="modal-title">프로필 이미지 변경</h2>

          <div class="image-upload-area">
            <input
              type="file"
              ref="fileInput"
              @change="handleImageUpload"
              accept="image/*"
              style="display: none"
            />
            <div class="upload-placeholder" @click="$refs.fileInput.click()">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="48"
                height="48"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="upload-icon"
              >
                <path
                  d="M14.5 4h-5L7 7H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-3l-2.5-3z"
                ></path>
                <circle cx="12" cy="13" r="3"></circle>
              </svg>
              <p>클릭하여 이미지를 선택하세요</p>
              <p class="upload-hint">JPG, PNG 파일만 업로드 가능합니다</p>
            </div>
          </div>

          <div class="modal-actions">
            <button @click="showImageUpload = false" class="cancel-button">
              취소
            </button>
            <button @click="onUploadImage" class="upload-button">업로드</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { formatDate } from "@/utils/date";
import { uploadImage } from "@/apis/common/upload.js";

const props = defineProps({ userProfile: Object });

const emit = defineEmits(["changeProfileImage"]);

// 프로필 이미지 업로드 모달
const showImageUpload = ref(false);

let file = null;

// 이미지 업로드 처리
const handleImageUpload = (event) => {
  file = event.target.files[0];
  if (file) {
    // 파일 크기 검사 (5MB 제한)
    if (file.size > 5 * 1024 * 1024) {
      alert("파일 크기는 5MB 이하여야 합니다.");
      return;
    }

    // 파일 타입 검사
    if (!file.type.startsWith("image/")) {
      alert("이미지 파일만 업로드 가능합니다.");
      return;
    }

    // 미리보기 생성
    const reader = new FileReader();
    reader.onload = (e) => {
      props.userProfile.profileImage = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

// 이미지 업로드
const onUploadImage = async () => {
  // 실제로는 서버에 이미지 업로드
  const url = await uploadImage(file);
  emit("changeProfileImage", url);
  showImageUpload.value = false;
};
</script>

<style scoped>
/* 프로필 헤더 */
.profile-header {
  display: flex;
  align-items: center;
  gap: 2rem;
  padding: 2rem;
  background: linear-gradient(135deg, var(--blue-2) 0%, var(--blue-3) 100%);
  border-radius: 1rem;
  color: white;
  margin-bottom: 2rem;
}

.profile-image-container {
  position: relative;
}

.profile-image {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  background-color: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4px solid rgba(255, 255, 255, 0.3);
}

.profile-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.default-avatar {
  color: rgba(255, 255, 255, 0.8);
}

.change-image-button {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
  background-color: white;
  color: var(--primary-color);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.change-image-button:hover {
  transform: scale(1.1);
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.profile-email {
  font-size: 1.125rem;
  opacity: 0.9;
  margin-bottom: 1rem;
}

.profile-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  opacity: 0.9;
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
  padding: 1rem;
}

.modal-container {
  position: relative;
  width: 100%;
  max-width: 500px;
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.modal-close-button {
  position: absolute;
  top: 1rem;
  right: 1rem;
  width: 2rem;
  height: 2rem;
  border-radius: 9999px;
  background-color: #f1f5f9;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: background-color 0.2s;
}

.modal-close-button:hover {
  background-color: #e2e8f0;
}

.modal-content {
  padding: 2rem;
}

.modal-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  color: var(--text-color);
}

.image-upload-area {
  margin-bottom: 1.5rem;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 2rem;
  border: 2px dashed var(--border-color);
  border-radius: 0.5rem;
  cursor: pointer;
  transition: border-color 0.2s, background-color 0.2s;
}

.upload-placeholder:hover {
  border-color: var(--primary-light);
}

.upload-icon {
  color: var(--text-light);
  margin-bottom: 1rem;
}

.upload-placeholder p {
  margin: 0;
  color: var(--text-light);
  font-weight: 500;
}

.upload-hint {
  font-size: 0.875rem;
  color: var(--text-lighter);
  margin-top: 0.5rem;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}

.upload-button {
  padding: 0.75rem 1.5rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.upload-button:hover {
  background-color: var(--primary-hover);
}
</style>
