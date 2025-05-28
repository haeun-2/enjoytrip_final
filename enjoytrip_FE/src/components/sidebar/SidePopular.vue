<template>
  <div>
    <div class="sidebar-section">
      <h3>인기 여행지</h3>
      <div class="popular-destinations">
        <a href="#" class="popular-destination">
          <span class="destination-name">제주도</span>
          <span class="destination-count">2,543</span>
        </a>
        <a href="#" class="popular-destination">
          <span class="destination-name">도쿄</span>
          <span class="destination-count">1,876</span>
        </a>
        <a href="#" class="popular-destination">
          <span class="destination-name">방콕</span>
          <span class="destination-count">1,654</span>
        </a>
        <a href="#" class="popular-destination">
          <span class="destination-name">파리</span>
          <span class="destination-count">1,432</span>
        </a>
        <a href="#" class="popular-destination">
          <span class="destination-name">부산</span>
          <span class="destination-count">1,298</span>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.sidebar {
  width: 260px;
  flex-shrink: 0;
}

.sidebar-section {
  background-color: white;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  padding: 16px;
  margin-bottom: 24px;
}

.sidebar-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--text-color);
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border-color);
}

.popular-destinations {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.popular-destination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background-color: var(--background-color);
  border-radius: var(--border-radius);
  transition: var(--transition);
}

.popular-destination:hover {
  background-color: var(--primary-light);
}

.destination-name {
  font-size: 14px;
  font-weight: 500;
}

.destination-count {
  font-size: 12px;
  color: var(--text-light);
  background-color: white;
  padding: 2px 6px;
  border-radius: 12px;
}
</style>
