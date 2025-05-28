<template>
  <div class="container">
    <!-- 프로필 헤더 -->
    <ProfileHeader :userProfile="userProfile" @changeProfileImage="changeProfileImage" />

    <!-- 탭 네비게이션 -->
    <div class="tab-navigation">
      <button
        v-for="tab in tabs"
        :key="tab.id"
        @click="activeTab = tab.id"
        :class="['tab-button', { 'tab-active': activeTab === tab.id }]"
      >
        <component :is="tab.icon" class="tab-icon" />
        <span>{{ tab.label }}</span>
      </button>
    </div>

    <!-- 탭 컨텐츠 -->
    <div class="tab-content">
      <!-- 프로필 설정 탭 -->
      <ProfileInfo
        v-if="activeTab === 'profile'"
        :userProfile="userProfile"
        @changeProfile="changeProfileText"
        @changePassword="changePassword"
      />

      <!-- 좋아요한 항목 탭 -->
      <ProfileLike v-if="activeTab === 'likes'" />
    </div>
  </div>
</template>

<script setup>
import ProfileHeader from "@/components/profile/ProfileHeader.vue";
import ProfileInfo from "@/components/profile/ProfileInfo.vue";
import ProfileLike from "@/components/profile/ProfileLike.vue";
import { ref, computed, onMounted } from "vue";
import * as memberApi from "@/apis/member/member";
import { useUserStore } from "@/stores/user";
const userStore = useUserStore();

const userId = userStore.user.id;

// 사용자 프로필 데이터
const userProfile = ref({
  name: null,
  email: null,
  birthDate: null,
  createdAt: null,
  profileImage: null,
});

// 탭 상태
const activeTab = ref("profile");
// 탭 정의
const tabs = [
  {
    id: "profile",
    label: "프로필 설정",
    icon: "UserIcon",
  },
  {
    id: "likes",
    label: "좋아요한 항목",
    icon: "HeartIcon",
  },
];

const changeProfileImage = (profileImageUrl) => {
  changeProfile({ profileImage: profileImageUrl });
};

const changeProfileText = ({ name, birthDate }) => {
  changeProfile({ name, birthDate });
};

const changeProfile = async ({
  name = userProfile.value.name,
  profileImage = userProfile.value.profileImage,
  birthDate = userProfile.value.birthDate,
}) => {
  try {
    const response = await memberApi.editMemberProfile(userId, {
      name,
      profileImage,
      birthDate,
    });
    userProfile.value.name = name;
    userProfile.value.profileImage = profileImage;
    userProfile.value.birthDate = birthDate;
  } catch (e) {
    alert("회원 정보 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

const changePassword = async (currentPassword, newPassword) => {
  try {
    const response = await memberApi.changePassword(userId, currentPassword, newPassword);
    alert(response);
  } catch (e) {
    console.log(e);
    alert("비밀번호 변경 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
};

onMounted(async () => {
  try {
    userProfile.value = await memberApi.getMemberProfile(userId);
  } catch (e) {
    alert("회원 정보 조회 중 오류가 발생했습니다. 다시 시도해주세요.");
  }
});
</script>

<style scoped>
/* 탭 네비게이션 */
.tab-navigation {
  display: flex;
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin-bottom: 2rem;
}

.tab-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 1.5rem;
  background: none;
  border: none;
  color: var(--text-light);
  cursor: pointer;
  transition: all 0.2s;
  flex: 1;
  justify-content: center;
  border-bottom: 3px solid transparent;
}

.tab-button:hover {
  color: var(--primary-color);
  background-color: rgba(45, 212, 191, 0.05);
}

.tab-button.tab-active {
  color: var(--primary-color);
  border-bottom-color: var(--primary-color);
  font-weight: 500;
}

.tab-icon {
  flex-shrink: 0;
}

/* 탭 컨텐츠 */
.tab-content {
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .container {
    padding: 1rem;
  }

  .profile-header {
    flex-direction: column;
    text-align: center;
    gap: 1.5rem;
  }

  .profile-meta {
    justify-content: center;
  }

  .tab-navigation {
    flex-direction: column;
  }

  .tab-button {
    justify-content: flex-start;
    border-bottom: none;
    border-left: 3px solid transparent;
  }

  .tab-button.tab-active {
    border-bottom: none;
    border-left-color: #2dd4bf;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .likes-navigation,
  .content-navigation {
    flex-direction: column;
  }

  .like-tab-button,
  .content-tab-button {
    text-align: left;
  }

  .content-grid,
  .content-grid-container {
    grid-template-columns: 1fr;
  }

  .list-item {
    flex-direction: column;
    gap: 1rem;
  }

  .item-actions {
    align-self: flex-end;
  }

  .list-header,
  .grid-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .create-button {
    align-self: stretch;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .profile-image {
    width: 100px;
    height: 100px;
  }

  .profile-name {
    font-size: 1.5rem;
  }

  .profile-email {
    font-size: 1rem;
  }

  .profile-meta {
    flex-direction: column;
    gap: 0.75rem;
  }

  .item-stats,
  .item-meta {
    flex-direction: column;
    gap: 0.5rem;
  }

  .modal-container {
    margin: 1rem;
    max-width: none;
  }

  .modal-content {
    padding: 1.5rem;
  }
}
</style>
