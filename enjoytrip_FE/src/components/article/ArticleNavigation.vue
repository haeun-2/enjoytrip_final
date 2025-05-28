<template>
  <div class="board-navigation-container">
    <div class="board-navigation">
      <router-link
        v-for="board in boards"
        :key="board.type"
        :to="{ name: 'ArticleList', params: { type: board.type } }"
        class="nav-item"
        :class="{ active: isActive(board.type) }"
      >
        <span class="nav-text">{{ board.title }}</span>
      </router-link>
    </div>

    <!-- 모바일 드롭다운 메뉴 -->
    <div class="mobile-navigation">
      <div class="mobile-dropdown" @click="toggleDropdown">
        <span class="current-board">{{ currentBoard.title }}</span>
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
          class="chevron-icon"
          :class="{ open: isDropdownOpen }"
        >
          <path d="m6 9 6 6 6-6"></path>
        </svg>
      </div>

      <div class="dropdown-menu" v-if="isDropdownOpen">
        <router-link
          v-for="board in boards"
          :key="board.id"
          :to="{ name: 'ArticleList', params: { type: board.type } }"
          class="dropdown-item"
          :class="{ active: isActive(board.id) }"
          @click="closeDropdown"
        >
          <span class="nav-text">{{ board.title }}</span>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useRoute } from "vue-router";
const route = useRoute();

// 현재 활성화된 게시판 ID
const currentBoard = ref({});

// 게시판 목록
const boards = [
  { type: "notice", title: "공지사항" },
  { type: "free", title: "자유게시판" },
  { type: "qna", title: "QNA" },
];

// 현재 게시판이 활성화되었는지 확인
const isActive = (type) => {
  return currentBoard.value.type == type;
};

// 모바일 드롭다운 메뉴 상태
const isDropdownOpen = ref(false);

// 드롭다운 메뉴 토글
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

// 드롭다운 메뉴 닫기
const closeDropdown = () => {
  isDropdownOpen.value = false;
};

watch(
  () => route.params.type,
  () => {
    currentBoard.value = boards.find((board) => board.type == route.params.type);
  },
  { immediate: true }
);
</script>

<style scoped>
.board-navigation-container {
  margin-bottom: 2rem;
  position: relative;
}

/* 데스크톱 네비게이션 */
.board-navigation {
  display: flex;
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  color: var(--text-light);
  text-decoration: none;
  transition: all 0.2s ease;
  position: relative;
  flex: 1;
  justify-content: center;
  border-bottom: 3px solid transparent;
}

.nav-item:hover {
  color: var(--primary-color);
  background-color: rgba(45, 212, 191, 0.05);
}

.nav-item.active {
  color: var(--primary-color);
  border-bottom-color: var(--primary-color);
  font-weight: 500;
}

.nav-icon {
  margin-right: 0.5rem;
  display: flex;
  align-items: center;
}

.nav-text {
  font-size: 0.9rem;
}

/* 모바일 네비게이션 */
.mobile-navigation {
  display: none;
  position: relative;
}

.mobile-dropdown {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1rem;
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

.chevron-icon {
  transition: transform 0.2s ease;
}

.chevron-icon.open {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 0.5rem);
  left: 0;
  right: 0;
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  z-index: 10;
  overflow: hidden;
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: var(--text-light);
  text-decoration: none;
  transition: background-color 0.2s ease;
}

.dropdown-item:hover {
  background-color: rgba(45, 212, 191, 0.05);
  color: var(--primary-color);
}

.dropdown-item.active {
  background-color: rgba(45, 212, 191, 0.1);
  color: var(--primary-color);
  font-weight: 500;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .board-navigation {
    display: none;
  }

  .mobile-navigation {
    display: block;
  }
}
</style>
