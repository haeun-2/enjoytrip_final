<template>
  <header class="header">
    <div class="header-container">
      <div class="logo" @click="goHome">
        <!-- <h1>TravelSketch</h1> -->
        <img src="@/assets/logo.png" alt="logo" class="logo-image" />
        <span class="logo-tagline">여행의 모든 순간을 함께</span>
      </div>

      <nav class="main-nav">
        <ul>
          <RouterLink :to="{ name: 'AttractionList' }" :class="{ active: isActive('/attraction') }">여행지</RouterLink>
          <RouterLink :to="{ name: 'PlanList' }" :class="{ active: isActive('/plan') }">여행계획</RouterLink>
          <RouterLink :to="{ name: 'HotplaceList' }" :class="{ active: isActive('/hotplace') }">핫플레이스</RouterLink>
          <RouterLink :to="{ name: 'ArticleList', params: { type: 'free' } }" :class="{ active: isActive('/article') }"
            >게시판</RouterLink
          >
          <RouterLink v-if="isAdmin" :to="{ name: 'AdminDashboard' }" :class="{ active: isActive('/admin/dashboard') }"
            >관리자 페이지</RouterLink
          >
        </ul>
      </nav>
      <div class="user-menu">
        <div class="user-profile">
          <img v-if="isLogined" :src="userStore.user.profileImage" alt="사용자 프로필" />
          <img v-else :src="ProfileDefault" alt="사용자 프로필" />
          <div v-if="isLogined" class="user-dropdown">
            <ul>
              <RouterLink :to="{ name: 'Profile' }">프로필</RouterLink>
              <RouterLink :to="{ name: 'PlanMine' }">내 여행계획</RouterLink>
              <RouterLink :to="{ name: 'HotplaceList', query: { mine: true } }">내 핫플레이스</RouterLink>
              <li><a class="logout" @click="handleLogout">로그아웃</a></li>
            </ul>
          </div>
          <div v-if="!isLogined" class="user-dropdown">
            <ul>
              <RouterLink :to="{ name: 'Signup' }" v-if="!isLogined" :class="{ active: isActive('/signup') }"
                >회원가입</RouterLink
              >
              <RouterLink :to="{ name: 'Login' }" v-if="!isLogined" :class="{ active: isActive('/sogin') }"
                >로그인</RouterLink
              >
            </ul>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed } from "vue";
import { useUserStore } from "@/stores/user";
import { RouterLink, useRouter, useRoute } from "vue-router";
import { logout } from "@/apis/auth/auth";
import ProfileDefault from "@/assets/profile-default.jpg";

const userStore = useUserStore();

const isLogined = computed(() => !!userStore.user);
const isAdmin = computed(() => userStore.user?.role === "ADMIN");

const router = useRouter();
const goHome = () => {
  router.push({ name: "Home" });
};

const handleLogout = async () => {
  try {
    const response = await logout();
    const userStore = useUserStore();
    userStore.$reset();
    router.push({ name: "Home" });
  } catch (e) {
    console.log("로그아웃 오류 발생", e);
  }
};

const route = useRoute();
const isActive = (prefix) => {
  return route.path.startsWith(prefix);
};
</script>

<style scoped>
/* 헤더 스타일 */
.header {
  background-color: white;
  box-shadow: var(--shadow);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 16px;
}

.logo {
  display: flex;
  flex-direction: column;
  justify-content: center;
  cursor: pointer;
}

.logo h1 {
  font-size: 24px;
  font-weight: 700;
  color: var(--primary-color);
  margin: 0;
}

.logo-tagline {
  font-size: 12px;
  color: var(--text-lighter);
}

.logo-image {
  width: 100px;
}

.main-nav ul {
  display: flex;
  gap: 24px;
}

.main-nav a {
  font-size: 15px;
  font-weight: 700;
  color: var(--text-color);
  padding: 8px 0;
  position: relative;
  transition: var(--transition);
}

.main-nav a:hover {
  color: var(--primary-color);
}

/* 활성화된 페이지 링크 */

.main-nav .active,
.main-nav .router-link-active {
  font-weight: bold;
  color: var(--primary-color);
}

.main-nav .active::after,
.main-nav .router-link-active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: var(--primary-color);
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 16px;
}
.user-profile {
  position: relative;
  cursor: pointer;
}

.user-profile img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid var(--primary-light);
}

.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  width: 180px;
  background-color: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-medium);
  padding: 8px 0;
  margin-top: 8px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(10px);
  transition: var(--transition);
  z-index: 10;
}

.user-profile:hover .user-dropdown {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.user-dropdown ul {
  display: flex;
  flex-direction: column;
}

.user-dropdown a {
  padding: 8px 16px;
  font-size: 14px;
  color: var(--text-color);
  transition: var(--transition);
}

.user-dropdown a:hover {
  background-color: var(--background-color);
  color: var(--primary-color);
}

.user-dropdown a.logout {
  display: block;
  color: var(--error-color);
}
</style>
