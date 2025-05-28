<template>
  <div class="admin-container">
    <div class="main-layout">
      <!-- 사이드바 -->
      <aside class="sidebar">
        <nav class="nav">
          <RouterLink :to="{ name: 'AdminDashboard' }" class="nav-button" active-class="active"
            ><span class="nav-icon">📊</span> 대시보드</RouterLink
          >
          <RouterLink :to="{ name: 'AdminMemberList' }" class="nav-button" active-class="active">
            <span class="nav-icon">👥</span> 멤버 관리</RouterLink
          >
          <RouterLink :to="{ name: 'AdminNoticeList' }" class="nav-button" active-class="active">
            <span class="nav-icon">📄</span> 공지 관리</RouterLink
          >
          <RouterLink :to="{ name: 'AdminQnAList' }" class="nav-button" active-class="active">
            <span class="nav-icon">💬</span>QnA 관리</RouterLink
          >
          <RouterLink :to="{ name: 'AdminReportList' }" class="nav-button" active-class="active">
            <span class="nav-icon">⚠️</span> 신고 관리</RouterLink
          >
        </nav>
      </aside>

      <!-- 메인 컨텐츠 -->
      <main class="content">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background-color: #f8fafc;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

/* 메인 레이아웃 */
.main-layout {
  display: flex;
}

/* 사이드바 */
.sidebar {
  width: 16rem;
  background: white;
  border-right: 1px solid #e2e8f0;
  min-height: calc(100vh - 73px);
}

.nav {
  padding: 1rem;
}

.nav-button {
  padding: 0.75rem 1rem;
  border: none;
  background: transparent;
  text-align: left;
  border-radius: 0.5rem;
  cursor: pointer;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.2s;
}

.nav-button:hover {
  background: #f1f5f9;
}

.active {
  background: var(--primary-color);
  color: white;
}

.nav-icon {
  font-size: 1rem;
}

/* 컨텐츠 */
.content {
  flex: 1;
  padding: 1.5rem;
}

/* 반응형 */
@media (max-width: 768px) {
  .main-layout {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    min-height: auto;
  }

  .nav {
    display: flex;
    overflow-x: auto;
    gap: 0.5rem;
  }

  .nav-button {
    white-space: nowrap;
    margin-bottom: 0;
  }
}
</style>
