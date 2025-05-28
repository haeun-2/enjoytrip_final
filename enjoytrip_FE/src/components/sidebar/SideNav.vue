<template>
  <header class="header">
    <div class="header-container">
      <div>
        <div class="logo" @click="goHome">
          <!-- <h1>TravelSketch</h1> -->
          <img src="@/assets/logo.png" alt="logo" class="logo-image" />
        </div>

        <nav class="main-nav">
          <ul>
            <RouterLink :to="{ name: 'AttractionList' }" :class="{ active: isActive('/attraction') }"
              >여행지</RouterLink
            >
            <RouterLink :to="{ name: 'PlanList' }" :class="{ active: isActive('/plan') }">여행계획</RouterLink>
            <RouterLink :to="{ name: 'HotplaceList' }" :class="{ active: isActive('/hotplace') }"
              >핫플레이스</RouterLink
            >
            <RouterLink
              :to="{ name: 'ArticleList', params: { type: 'free' } }"
              :class="{ active: isActive('/article') }"
              >게시판</RouterLink
            >
            <RouterLink
              v-if="isAdmin"
              :to="{ name: 'AdminDashboard' }"
              :class="{ active: isActive('/admin/dashboard') }"
              >관리자</RouterLink
            >
          </ul>
        </nav>
      </div>
      <div class="user-menu">
        <div class="user-profile">
          <img v-show="isLogined" :src="userStore.user?.profileImage" alt="사용자 프로필" />
          <img v-show="!isLogined" :src="ProfileDefault" alt="사용자 프로필" />
          <div class="user-dropdown">
            <ul>
              <li v-if="!isLogined">
                <RouterLink :to="{ name: 'Signup' }" :class="{ active: isActive('/signup') }"> 회원가입 </RouterLink>
              </li>
              <li v-if="!isLogined">
                <RouterLink :to="{ name: 'Login' }" :class="{ active: isActive('/login') }"> 로그인 </RouterLink>
              </li>
              <li v-if="isLogined">
                <RouterLink :to="{ name: 'Profile' }">프로필</RouterLink>
              </li>
              <li v-if="isLogined">
                <RouterLink :to="{ name: 'PlanList', query: { mine: true } }"> 내 여행계획 </RouterLink>
              </li>
              <li v-if="isLogined">
                <RouterLink :to="{ name: 'HotplaceList', query: { mine: true } }"> 내 핫플레이스 </RouterLink>
              </li>
              <li v-if="isLogined">
                <a class="logout" @click="handleLogout">로그아웃</a>
              </li>
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
  position: sticky;
  top: 0;
  z-index: 100;
  border-right: 1px solid var(--light-gray);
}

.header-container {
  margin: 0 auto;
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  box-sizing: border-box;
  flex-wrap: wrap;
  gap: 16px;
  height: calc(100vh - 30px);
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

.logo-image {
  width: 100px;
}

.main-nav ul {
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  text-align: center;
}

.main-nav a {
  font-size: 15px;
  font-weight: 700;
  color: var(--text-color);
  padding: 30px 0;
  position: relative;
  transition: var(--transition);
  border-bottom: 1px solid var(--light-gray);
}

.main-nav a:hover {
  color: var(--primary-color);
}

.main-nav .active,
.main-nav .router-link-active {
  font-weight: bold;
  color: white;
  background-color: var(--blue-3);
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
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
  border: 2px solid var(--primary-light);
}

.user-dropdown {
  position: absolute;
  bottom: 100%;
  left: 30px;
  width: 180px;
  background-color: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-small);
  padding: 8px 0;
  margin-bottom: 8px;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: opacity 0.3s ease, transform 0.3s ease;
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
