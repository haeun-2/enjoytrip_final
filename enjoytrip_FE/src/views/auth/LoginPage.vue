<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <img class="logo-image" :src="Logo" />
        <p class="login-subtitle">
          계정이 없으신가요?
          <router-link :to="{ name: 'Signup' }" class="text-link">회원가입</router-link>
        </p>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="email">이메일</label>
          <div class="input-container">
            <span class="input-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="18" height="18">
                <path
                  fill="currentColor"
                  d="M20,4H4C2.9,4,2,4.9,2,6v12c0,1.1,0.9,2,2,2h16c1.1,0,2-0.9,2-2V6C22,4.9,21.1,4,20,4z M20,8l-8,5L4,8V6l8,5l8-5V8z"
                />
              </svg>
            </span>
            <input id="email" type="email" v-model="email" placeholder="이메일 주소" required @blur="validateEmail" />
          </div>
          <p v-if="emailError" class="error-message">{{ emailError }}</p>
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <div class="input-container">
            <span class="input-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="18" height="18">
                <path
                  fill="currentColor"
                  d="M18,8h-1V6c0-2.76-2.24-5-5-5S7,3.24,7,6v2H6c-1.1,0-2,0.9-2,2v10c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V10C20,8.9,19.1,8,18,8z M9,6c0-1.66,1.34-3,3-3s3,1.34,3,3v2H9V6z M18,20H6V10h12V20z M12,17c1.1,0,2-0.9,2-2c0-1.1-0.9-2-2-2c-1.1,0-2,0.9-2,2C10,16.1,10.9,17,12,17z"
                />
              </svg>
            </span>
            <input
              id="password"
              :type="showPassword ? 'text' : 'password'"
              v-model="password"
              placeholder="비밀번호"
              required
              @blur="validatePassword"
            />
            <button type="button" class="password-toggle" @click="showPassword = !showPassword">
              <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="18" height="18">
                <path
                  fill="currentColor"
                  d="M12,4.5C7,4.5,2.73,7.61,1,12c1.73,4.39,6,7.5,11,7.5s9.27-3.11,11-7.5C21.27,7.61,17,4.5,12,4.5z M12,17c-2.76,0-5-2.24-5-5s2.24-5,5-5s5,2.24,5,5S14.76,17,12,17z M12,9c-1.66,0-3,1.34-3,3s1.34,3,3,3s3-1.34,3-3S13.66,9,12,9z"
                />
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="18" height="18">
                <path
                  fill="currentColor"
                  d="M12,7c2.76,0,5,2.24,5,5c0,0.65-0.13,1.26-0.36,1.83l2.92,2.92c1.51-1.26,2.7-2.89,3.43-4.75c-1.73-4.39-6-7.5-11-7.5c-1.4,0-2.74,0.25-3.98,0.7l2.16,2.16C10.74,7.13,11.35,7,12,7z M2,4.27l2.28,2.28l0.46,0.46C3.08,8.3,1.78,10.02,1,12c1.73,4.39,6,7.5,11,7.5c1.55,0,3.03-0.3,4.38-0.84l0.42,0.42L19.73,22L21,20.73L3.27,3L2,4.27z M7.53,9.8l1.55,1.55c-0.05,0.21-0.08,0.43-0.08,0.65c0,1.66,1.34,3,3,3c0.22,0,0.44-0.03,0.65-0.08l1.55,1.55c-0.67,0.33-1.41,0.53-2.2,0.53c-2.76,0-5-2.24-5-5C7,11.21,7.2,10.47,7.53,9.8z M11.84,9.02l3.15,3.15l0.02-0.16c0-1.66-1.34-3-3-3L11.84,9.02z"
                />
              </svg>
            </button>
          </div>
          <p v-if="passwordError" class="error-message">{{ passwordError }}</p>
        </div>

        <div class="form-options">
          <div class="remember-me">
            <input type="checkbox" id="remember" v-model="rememberMe" />
            <label for="remember">로그인 상태 유지</label>
          </div>
          <a href="#" class="text-link">비밀번호를 잊으셨나요?</a>
        </div>

        <button type="submit" class="login-button" :disabled="isLoading">
          <span v-if="isLoading" class="spinner"></span>
          <span>{{ isLoading ? "로그인 중..." : "로그인" }}</span>
        </button>
      </form>

      <div class="divider">
        <span>또는 소셜 계정으로 로그인</span>
      </div>

      <div class="social-login">
        <button class="kakao-button" @click="handleKakaoLogin">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="20" height="20">
            <path
              fill="currentColor"
              d="M12 3C7.03125 3 3 6.03125 3 9.75C3 12.3125 4.79688 14.5312 7.39062 15.7188C7.03125 16.9062 6.23438 19.5 6.16875 19.7812C6.16875 19.7812 6.13125 20.0625 6.32812 20.1562C6.525 20.25 6.7125 20.1094 6.7125 20.1094C7.03125 19.9688 10.0312 17.9062 10.9219 17.2969C11.2781 17.3438 11.6344 17.3906 12 17.3906C16.9688 17.3906 21 14.3594 21 9.75C21 6.03125 16.9688 3 12 3Z"
            />
          </svg>
          카카오 로그인
        </button>
        <button class="naver-button" @click="handleNaverLogin">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="20" height="20">
            <path fill="currentColor" d="M16.273 12.845L7.376 0H0V24H7.727V11.155L16.624 24H24V0H16.273V12.845Z" />
          </svg>
          네이버 로그인
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import Logo from "@/assets/logo.png";
import { login } from "@/apis/auth/auth.js";
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useUserStore } from "@/stores/user";

// 상태 관리
const email = ref("");
const password = ref("");
const rememberMe = ref(false);
const isLoading = ref(false);
const emailError = ref("");
const passwordError = ref("");
const showPassword = ref(false);

// 로그인 후 처리를
const router = useRouter();
const userStore = useUserStore();
const route = useRoute();

// oauth url
const KAKAO_AUTH_URL =
  "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=7dc2af94b5f5b12995c77596d1154ac7&redirect_uri=http://localhost:8080/api/auth/kakao/callback";
const NAVER_AUTH_URL =
  "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=4hDUFOG28CJuXsREBskK&state=STATE_STRING&redirect_uri=http://localhost:8080/api/auth/naver/callback";

// 이메일 유효성 검사
const validateEmail = () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!email.value) {
    emailError.value = "이메일을 입력해주세요.";
    return false;
  } else if (!emailRegex.test(email.value)) {
    emailError.value = "유효한 이메일 주소를 입력해주세요.";
    return false;
  }
  emailError.value = "";
  return true;
};

// 비밀번호 유효성 검사
const validatePassword = () => {
  if (!password.value) {
    passwordError.value = "비밀번호를 입력해주세요.";
    return false;
  } else if (password.value.length < 4) {
    passwordError.value = "비밀번호는 최소 4자 이상이어야 합니다.";
    return false;
  }
  passwordError.value = "";
  return true;
};

// 이메일/비밀번호 로그인 처리
const handleLogin = async () => {
  const isEmailValid = validateEmail();
  const isPasswordValid = validatePassword();

  if (!isEmailValid || !isPasswordValid) {
    return;
  }

  try {
    isLoading.value = true;
    // 로그인 API 호출
    const response = await login(email.value, password.value);
    // 로그인 성공 시 처리 (사용자 정보 관리, 홈페이지로 리다이렉트)
    userStore.setUser(response.data);
    router.push({ name: "Home" });
  } catch (error) {
    // 오류 처리 로직
    alert("이메일 또는 비밀번호를 확인해주세요.");
    password.value = "";
  } finally {
    isLoading.value = false;
  }
};

// 카카오 로그인 처리
const handleKakaoLogin = () => {
  // 카카오 OAuth 로그인 로직 구현
  window.location.href = KAKAO_AUTH_URL;
};

// 네이버 로그인 처리
const handleNaverLogin = () => {
  // 네이버 OAuth 로그인 로직 구현
  window.location.href = NAVER_AUTH_URL;
};

onMounted(() => {
  if (userStore.user) {
    router.push({ name: "Home" });
    return;
  }

  const { id, email, name, profileImage, role } = route.query;

  if (id && email && name && profileImage) {
    userStore.setUser({
      id: Number(id),
      email: decodeURIComponent(email),
      name: decodeURIComponent(name),
      profileImage: decodeURIComponent(profileImage),
      role: decodeURIComponent(role),
    });
    router.push({ name: "Home" }); // 사용자 정보 세팅 후 홈으로 이동
  }
});
</script>

<style>
:root {
  --primary-color: var(--blue-2);
  --primary-hover: var(--blue-1);
  --primary-light: var(--blue-5);
  --text-color: #333333;
  --text-light: #757575;
  --background-color: #f5f5f5;
  --card-color: #ffffff;
  --border-color: #e0e0e0;
  --error-color: #f44336;
  --kakao-color: #fee500;
  --kakao-hover: #f2d900;
  --naver-color: #03c75a;
  --naver-hover: #02b350;
  --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  --border-radius: 8px;
  --transition: all 0.3s ease;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: "Noto Sans KR", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell,
    "Open Sans", "Helvetica Neue", sans-serif;
  color: var(--text-color);
  line-height: 1.6;
}

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--background-color);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 450px;
  background-color: var(--card-color);
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  padding: 32px;
}

.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.logo-image {
  width: 180px;
}
.login-subtitle {
  font-size: 14px;
  color: var(--text-light);
}

.text-link {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
  transition: var(--transition);
  font-size: 15px;
}

.text-link:hover {
  color: var(--primary-hover);
  text-decoration: underline;
}

.login-form {
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 6px;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 12px;
  color: var(--text-light);
  display: flex;
  align-items: center;
}

.input-container input {
  width: 100%;
  padding: 12px 12px 12px 40px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  font-size: 16px;
  transition: var(--transition);
}

.input-container input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px var(--primary-light);
}

.password-toggle {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  color: var(--text-light);
  cursor: pointer;
  display: flex;
  align-items: center;
}

.error-message {
  color: var(--error-color);
  font-size: 12px;
  margin-top: 4px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.remember-me {
  display: flex;
  align-items: center;
}

.remember-me input[type="checkbox"] {
  margin-right: 8px;
  accent-color: var(--primary-color);
}

.remember-me label {
  font-size: 14px;
  color: var(--text-light);
}

.login-button {
  width: 100%;
  padding: 12px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius);
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-button:hover:not(:disabled) {
  background-color: var(--primary-hover);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.spinner {
  display: inline-block;
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
  margin-right: 8px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.divider {
  display: flex;
  align-items: center;
  margin: 24px 0;
  color: var(--text-light);
  font-size: 14px;
}

.divider::before,
.divider::after {
  content: "";
  flex: 1;
  border-bottom: 1px solid var(--border-color);
}

.divider span {
  padding: 0 10px;
}

.social-login {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.kakao-button,
.naver-button {
  padding: 12px;
  border: none;
  border-radius: var(--border-radius);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  justify-content: center;
  align-items: center;
}

.kakao-button svg,
.naver-button svg {
  margin-right: 8px;
}

.kakao-button {
  background-color: var(--kakao-color);
  color: #3c1e1e;
}

.kakao-button:hover {
  background-color: var(--kakao-hover);
}

.naver-button {
  background-color: var(--naver-color);
  color: white;
}

.naver-button:hover {
  background-color: var(--naver-hover);
}

/* 반응형 디자인 */
@media (max-width: 480px) {
  .login-card {
    padding: 24px 16px;
  }

  .social-login {
    grid-template-columns: 1fr;
  }
}
</style>
