<template>
  <div>
    <footer class="footer">
      <div class="footer-container">
        <div class="footer-logo">
          <!-- <img src="@/assets/logo.png" alt="" /> -->
          <h3>TravelSketch</h3>
          <p>여행의 모든 순간을 함께</p>
        </div>
        <div class="footer-content">
          <div class="footer-links">
            <a href="#">이용약관</a>
            <span>|</span>
            <a href="#">개인정보처리방침</a>
          </div>
          <div>
            <p>본 사이트의 콘텐츠는 저작권법의 보호를 받는 바, 무단 전재, 복사, 배포 등을 금합니다.</p>
            <p class="footer-copy">Copyright &copy; 2025 TravelSketch All rights reserved.</p>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup></script>

<style scoped>
/* 푸터 스타일 */
.footer {
  background-color: #212529;
  color: rgba(255, 255, 255, 0.7);
  padding: 30px 0;
  /* margin-top: 48px; */
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 48px;
}

.footer-logo img {
  width: 120px;
  opacity: 0.5;
}

.footer-logo h3 {
  color: white;
}

.footer-logo p {
  font-size: small;
}

.footer-content {
  font-size: small;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.footer-links a {
  color: #61a1ff;
  text-decoration: none;
  color: rgba(255, 255, 255, 0.7);
}

.footer-links a:hover {
  text-decoration: underline;
}

.footer-copy {
  color: rgba(255, 255, 255, 0.6);
  line-height: 1.6;
  color: white;
}
</style>
