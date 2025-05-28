<template>
  <div class="profile-settings">
    <div class="settings-section">
      <h2 class="section-title">기본 정보</h2>
      <div class="form-grid">
        <div class="form-group">
          <label for="name" class="form-label">이름</label>
          <input
            type="text"
            id="name"
            v-model="editProfile.name"
            class="form-input"
            :disabled="!isEditing"
          />
        </div>
        <div class="form-group">
          <label for="birthDate" class="form-label">생년월일</label>
          <input
            type="date"
            id="birthDate"
            v-model="editProfile.birthDate"
            class="form-input"
            :disabled="!isEditing"
          />
        </div>
      </div>

      <div class="form-actions">
        <button v-if="!isEditing" @click="startEditing" class="edit-button">
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
            class="edit-icon"
          >
            <path
              d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"
            ></path>
            <path
              d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"
            ></path>
          </svg>
          정보 수정
        </button>
        <div v-else class="edit-actions">
          <button @click="cancelEditing" class="cancel-button">취소</button>
          <button @click="saveProfile" class="save-button">저장</button>
        </div>
      </div>
    </div>

    <div class="settings-section">
      <h2 class="section-title">비밀번호 변경</h2>
      <div class="password-form">
        <div class="form-group pass-form">
          <label for="currentPassword" class="form-label">현재 비밀번호</label>
          <input
            type="password"
            id="currentPassword"
            v-model="passwordForm.currentPassword"
            class="form-input"
            placeholder="현재 비밀번호를 입력하세요"
          />
        </div>
        <div class="form-group pass-form">
          <label for="newPassword" class="form-label">새 비밀번호</label>
          <input
            type="password"
            id="newPassword"
            v-model="passwordForm.newPassword"
            class="form-input"
            placeholder="새 비밀번호를 입력하세요"
          />
        </div>
        <div class="form-group pass-form">
          <label for="confirmPassword" class="form-label"
            >새 비밀번호 확인</label
          >
          <input
            type="password"
            id="confirmPassword"
            v-model="passwordForm.confirmPassword"
            class="form-input"
            placeholder="새 비밀번호를 다시 입력하세요"
          />
        </div>
        <button @click="changePassword" class="change-password-button">
          비밀번호 변경
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({ userProfile: Object });

const emit = defineEmits(["changeProfile", "changePassword"]);

// 프로필 편집 상태
const isEditing = ref(false);
const editProfile = ref({ ...props.userProfile });
watch(
  () => props.userProfile,
  (newVal) => {
    editProfile.value = { ...props.userProfile };
  }
);

// 비밀번호 변경 폼
const passwordForm = ref({
  currentPassword: "",
  newPassword: "",
  confirmPassword: "",
});

// 프로필 편집 시작
const startEditing = () => {
  isEditing.value = true;
  editProfile.value = { ...props.userProfile };
};

// 프로필 편집 취소
const cancelEditing = () => {
  isEditing.value = false;
  editProfile.value = { ...props.userProfile };
};

// 프로필 저장
const saveProfile = () => {
  // 유효성 검사
  if (!editProfile.value.name.trim()) {
    alert("이름을 입력해주세요.");
    return;
  }
  emit("changeProfile", editProfile.value);
  isEditing.value = false;
};

// 비밀번호 변경
const changePassword = () => {
  // 유효성 검사
  if (!passwordForm.value.currentPassword) {
    alert("현재 비밀번호를 입력해주세요.");
    return;
  }

  if (!passwordForm.value.newPassword) {
    alert("새 비밀번호를 입력해주세요.");
    return;
  }

  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    alert("새 비밀번호가 일치하지 않습니다.");
    return;
  }

  if (passwordForm.value.newPassword.length < 8) {
    alert("새 비밀번호는 8자 이상이어야 합니다.");
    return;
  }

  emit(
    "changePassword",
    passwordForm.value.currentPassword,
    passwordForm.value.newPassword
  );

  // 폼 초기화
  passwordForm.value = {
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  };
};
</script>

<style scoped>
/* 프로필 설정 */
.profile-settings {
  padding: 2rem;
}

.settings-section {
  margin-bottom: 3rem;
}

.settings-section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  color: var(--text-color);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
}

.edit-button {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
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

.edit-button:hover {
  background-color: var(--primary-hover);
}

.edit-actions {
  display: flex;
  gap: 0.75rem;
}

.cancel-button {
  padding: 0.75rem 1.5rem;
  background-color: #f3f4f6;
  color: #374151;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.cancel-button:hover {
  background-color: #e5e7eb;
}

.save-button {
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

.save-button:hover {
  background-color: var(--primary-hover);
}

/* 비밀번호 폼 */
.password-form {
  max-width: 400px;
}

.change-password-button {
  width: 100%;
  padding: 0.75rem;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-top: 1rem;
}

.change-password-button:hover {
  background-color: var(--primary-hover);
}

.pass-form {
  margin: 10px 0;
}
</style>
