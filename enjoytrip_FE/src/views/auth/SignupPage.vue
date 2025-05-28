<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <h1 class="register-title">회원가입</h1>
        <p class="register-subtitle">
          이미 계정이 있으신가요?
          <router-link :to="{ name: 'Login' }" class="text-link"
            >로그인</router-link
          >
        </p>
      </div>

      <form class="register-form" @submit.prevent="handleRegister">
        <!-- 프로필 이미지 업로드 -->
        <div class="profile-upload">
          <div class="profile-preview" @click="triggerFileInput">
            <img
              v-if="profilePreview"
              :src="profilePreview"
              alt="프로필 미리보기"
              class="preview-image"
            />
            <div v-else class="profile-placeholder">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="32"
                height="32"
              >
                <path
                  fill="currentColor"
                  d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"
                />
              </svg>
            </div>
            <div class="upload-icon">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="20"
                height="20"
              >
                <path
                  fill="currentColor"
                  d="M19 7v2.99s-1.99.01-2 0V7h-3s.01-1.99 0-2h3V2h2v3h3v2h-3zm-3 4V8h-3V5H5c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2v-8h-3zM5 19l3-4 2 3 3-4 4 5H5z"
                />
              </svg>
            </div>
          </div>
          <input
            type="file"
            ref="fileInput"
            accept="image/*"
            @change="handleFileChange"
            class="file-input"
          />
          <p class="profile-upload-text">프로필 이미지 선택</p>
          <p v-if="profileError" class="error-message">{{ profileError }}</p>
        </div>

        <!-- 이름 입력 -->
        <div class="form-group">
          <label for="name">이름</label>
          <div class="input-container">
            <span class="input-icon">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
                <path
                  fill="currentColor"
                  d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"
                />
              </svg>
            </span>
            <input
              id="name"
              type="text"
              v-model="name"
              placeholder="이름을 입력하세요"
              required
              @blur="validateName"
            />
          </div>
          <p v-if="nameError" class="error-message">{{ nameError }}</p>
        </div>

        <!-- 이메일 입력 및 인증 -->
        <div class="form-group">
          <label for="email">이메일</label>
          <div class="input-container">
            <span class="input-icon">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
                <path
                  fill="currentColor"
                  d="M20,4H4C2.9,4,2,4.9,2,6v12c0,1.1,0.9,2,2,2h16c1.1,0,2-0.9,2-2V6C22,4.9,21.1,4,20,4z M20,8l-8,5L4,8V6l8,5l8-5V8z"
                />
              </svg>
            </span>
            <input
              id="email"
              type="email"
              v-model="email"
              placeholder="이메일 주소"
              required
              @blur="validateEmail"
              :disabled="emailVerified || isVerificationSent"
            />
            <button
              type="button"
              class="verification-button"
              @click="sendVerificationCode"
              :disabled="
                !isEmailValid ||
                isVerificationSent ||
                emailVerified ||
                isSendingCode
              "
            >
              {{ getVerificationButtonText() }}
            </button>
          </div>
          <p v-if="emailError" class="error-message">{{ emailError }}</p>

          <!-- 이메일 인증 코드 입력 -->
          <div
            v-if="isVerificationSent && !emailVerified"
            class="verification-section"
          >
            <div class="verification-header">
              <p class="verification-text">
                <span class="verification-email">{{ email }}</span
                >으로 인증 코드가 발송되었습니다.
              </p>
              <p class="verification-timer">
                {{ formatTime(remainingTime) }} 후 만료
              </p>
            </div>

            <div class="verification-code-container">
              <input
                type="text"
                v-model="verificationCode"
                placeholder="인증 코드 6자리 입력"
                maxlength="6"
                class="verification-code-input"
              />
              <button
                type="button"
                class="verify-button"
                @click="verifyCode"
                :disabled="verificationCode.length !== 6 || isVerifying"
              >
                {{ isVerifying ? "확인 중..." : "확인" }}
              </button>
            </div>

            <div class="verification-actions">
              <button
                type="button"
                class="resend-button"
                @click="resendVerificationCode"
                :disabled="!canResend || isSendingCode"
              >
                {{ isSendingCode ? "발송 중..." : "인증 코드 재발송" }}
              </button>
              <button
                type="button"
                class="change-email-button"
                @click="changeEmail"
              >
                이메일 변경
              </button>
            </div>
          </div>

          <!-- 이메일 인증 완료 메시지 -->
          <div v-if="emailVerified" class="verification-success">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              width="18"
              height="18"
            >
              <path
                fill="currentColor"
                d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41L9 16.17z"
              />
            </svg>
            <span>이메일 인증이 완료되었습니다.</span>
          </div>
        </div>

        <!-- 비밀번호 입력 -->
        <div class="form-group">
          <label for="password">비밀번호</label>
          <div class="input-container">
            <span class="input-icon">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
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
            <button
              type="button"
              class="password-toggle"
              @click="showPassword = !showPassword"
            >
              <svg
                v-if="showPassword"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
                <path
                  fill="currentColor"
                  d="M12,4.5C7,4.5,2.73,7.61,1,12c1.73,4.39,6,7.5,11,7.5s9.27-3.11,11-7.5C21.27,7.61,17,4.5,12,4.5z M12,17c-2.76,0-5-2.24-5-5s2.24-5,5-5s5,2.24,5,5S14.76,17,12,17z M12,9c-1.66,0-3,1.34-3,3s1.34,3,3,3s3-1.34,3-3S13.66,9,12,9z"
                />
              </svg>
              <svg
                v-else
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
                <path
                  fill="currentColor"
                  d="M12,7c2.76,0,5,2.24,5,5c0,0.65-0.13,1.26-0.36,1.83l2.92,2.92c1.51-1.26,2.7-2.89,3.43-4.75c-1.73-4.39-6-7.5-11-7.5c-1.4,0-2.74,0.25-3.98,0.7l2.16,2.16C10.74,7.13,11.35,7,12,7z M2,4.27l2.28,2.28l0.46,0.46C3.08,8.3,1.78,10.02,1,12c1.73,4.39,6,7.5,11,7.5c1.55,0,3.03-0.3,4.38-0.84l0.42,0.42L19.73,22L21,20.73L3.27,3L2,4.27z M7.53,9.8l1.55,1.55c-0.05,0.21-0.08,0.43-0.08,0.65c0,1.66,1.34,3,3,3c0.22,0,0.44-0.03,0.65-0.08l1.55,1.55c-0.67,0.33-1.41,0.53-2.2,0.53c-2.76,0-5-2.24-5-5C7,11.21,7.2,10.47,7.53,9.8z M11.84,9.02l3.15,3.15l0.02-0.16c0-1.66-1.34-3-3-3L11.84,9.02z"
                />
              </svg>
            </button>
          </div>
          <p v-if="passwordError" class="error-message">{{ passwordError }}</p>
        </div>

        <!-- 비밀번호 확인 -->
        <div class="form-group">
          <label for="confirmPassword">비밀번호 확인</label>
          <div class="input-container">
            <span class="input-icon">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
                <path
                  fill="currentColor"
                  d="M18,8h-1V6c0-2.76-2.24-5-5-5S7,3.24,7,6v2H6c-1.1,0-2,0.9-2,2v10c0,1.1,0.9,2,2,2h12c1.1,0,2-0.9,2-2V10C20,8.9,19.1,8,18,8z M9,6c0-1.66,1.34-3,3-3s3,1.34,3,3v2H9V6z M18,20H6V10h12V20z M12,17c1.1,0,2-0.9,2-2c0-1.1-0.9-2-2-2c-1.1,0-2,0.9-2,2C10,16.1,10.9,17,12,17z"
                />
              </svg>
            </span>
            <input
              id="confirmPassword"
              :type="showConfirmPassword ? 'text' : 'password'"
              v-model="confirmPassword"
              placeholder="비밀번호 확인"
              required
              @blur="validateConfirmPassword"
            />
            <button
              type="button"
              class="password-toggle"
              @click="showConfirmPassword = !showConfirmPassword"
            >
              <svg
                v-if="showConfirmPassword"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
                <path
                  fill="currentColor"
                  d="M12,4.5C7,4.5,2.73,7.61,1,12c1.73,4.39,6,7.5,11,7.5s9.27-3.11,11-7.5C21.27,7.61,17,4.5,12,4.5z M12,17c-2.76,0-5-2.24-5-5s2.24-5,5-5s5,2.24,5,5S14.76,17,12,17z M12,9c-1.66,0-3,1.34-3,3s1.34,3,3,3s3-1.34,3-3S13.66,9,12,9z"
                />
              </svg>
              <svg
                v-else
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
                <path
                  fill="currentColor"
                  d="M12,7c2.76,0,5,2.24,5,5c0,0.65-0.13,1.26-0.36,1.83l2.92,2.92c1.51-1.26,2.7-2.89,3.43-4.75c-1.73-4.39-6-7.5-11-7.5c-1.4,0-2.74,0.25-3.98,0.7l2.16,2.16C10.74,7.13,11.35,7,12,7z M2,4.27l2.28,2.28l0.46,0.46C3.08,8.3,1.78,10.02,1,12c1.73,4.39,6,7.5,11,7.5c1.55,0,3.03-0.3,4.38-0.84l0.42,0.42L19.73,22L21,20.73L3.27,3L2,4.27z M7.53,9.8l1.55,1.55c-0.05,0.21-0.08,0.43-0.08,0.65c0,1.66,1.34,3,3,3c0.22,0,0.44-0.03,0.65-0.08l1.55,1.55c-0.67,0.33-1.41,0.53-2.2,0.53c-2.76,0-5-2.24-5-5C7,11.21,7.2,10.47,7.53,9.8z M11.84,9.02l3.15,3.15l0.02-0.16c0-1.66-1.34-3-3-3L11.84,9.02z"
                />
              </svg>
            </button>
          </div>
          <p v-if="confirmPasswordError" class="error-message">
            {{ confirmPasswordError }}
          </p>
        </div>

        <!-- 생년월일 입력 -->
        <div class="form-group">
          <label for="birthdate">생년월일</label>
          <div class="input-container">
            <span class="input-icon">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                width="18"
                height="18"
              >
                <path
                  fill="currentColor"
                  d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z"
                />
              </svg>
            </span>
            <input
              id="birthdate"
              type="date"
              v-model="birthdate"
              required
              @blur="validateBirthdate"
            />
          </div>
          <p v-if="birthdateError" class="error-message">
            {{ birthdateError }}
          </p>
        </div>

        <!-- 개인정보 동의 -->
        <div class="form-group">
          <div class="checkbox-container">
            <input type="checkbox" id="agreement" v-model="agreement" />
            <label for="agreement">개인정보 수집 및 이용에 동의합니다.</label>
          </div>
          <p v-if="agreementError" class="error-message">
            {{ agreementError }}
          </p>
        </div>

        <button
          type="submit"
          class="register-button"
          :disabled="isLoading || !emailVerified"
        >
          <span v-if="isLoading" class="spinner"></span>
          <span>{{ isLoading ? "처리 중..." : "회원가입" }}</span>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { signup, sendEmailCode, verifyEmailCode } from "@/apis/auth/auth";
import { getPresignedUrl, uploadImage } from "@/apis/common/upload";
const router = useRouter();

// 상태 관리
const name = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const birthdate = ref("");
const profileImage = ref(null);
const profilePreview = ref("");
const profileImageUrl = ref("");
const defaultProfileImage =
  "https://s3.ap-northeast-2.amazonaws.com/ssafy.enjoytrip/uploads/762fa592-8a09-4748-8438-9eb8f6f73c6b%25EC%258A%25A4%25ED%2581%25AC%25EB%25A6%25B0%25EC%2583%25B7%25202025-05-26%2520220732.png";
const agreement = ref(false);
const isLoading = ref(false);
const fileInput = ref(null);

// 이메일 인증 관련 상태
const isVerificationSent = ref(false);
const isSendingCode = ref(false);
const isVerifying = ref(false);
const verificationCode = ref("");
const emailVerified = ref(false);
const remainingTime = ref(180); // 3분 (초 단위)
const canResend = ref(false);
const verificationTimer = ref(null);
const resendTimer = ref(null);

// 오류 상태
const nameError = ref("");
const emailError = ref("");
const passwordError = ref("");
const confirmPasswordError = ref("");
const birthdateError = ref("");
const profileError = ref("");
const agreementError = ref("");

// 비밀번호 표시 상태
const showPassword = ref(false);
const showConfirmPassword = ref(false);

// 이메일 유효성 계산
const isEmailValid = computed(() => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return email.value && emailRegex.test(email.value);
});

// 인증 버튼 텍스트 계산
const getVerificationButtonText = () => {
  if (emailVerified.value) return "인증완료";
  if (isSendingCode.value) return "발송 중...";
  if (isVerificationSent.value) return "발송됨";
  return "인증하기";
};

// 파일 입력 트리거
const triggerFileInput = () => {
  fileInput.value.click();
};

// 파일 변경 처리
const handleFileChange = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // 파일 유효성 검사
  if (!file.type.match("image.*")) {
    profileError.value = "이미지 파일만 업로드 가능합니다.";
    return;
  }

  if (file.size > 5 * 1024 * 1024) {
    // 5MB 제한
    profileError.value = "이미지 크기는 5MB 이하여야 합니다.";
    return;
  }

  profileError.value = "";
  profileImage.value = file;

  // 이미지 미리보기 생성
  const reader = new FileReader();
  reader.onload = (e) => {
    profilePreview.value = e.target.result;
  };
  reader.readAsDataURL(file);

  try {
    const uploadedUrl = await uploadImage(file);
    profileImageUrl.value = uploadedUrl; // S3 업로드 후 반환된 이미지 URL
    console.log("업로드 성공:", uploadedUrl);
  } catch (err) {
    profileError.value = "이미지 업로드에 실패했습니다.";
    console.error(err);
  }
};

// 이름 유효성 검사
const validateName = () => {
  if (!name.value) {
    nameError.value = "이름을 입력해주세요.";
    return false;
  } else if (name.value.length < 2) {
    nameError.value = "이름은 최소 2자 이상이어야 합니다.";
    return false;
  }
  nameError.value = "";
  return true;
};

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
  } else if (password.value.length < 8) {
    passwordError.value = "비밀번호는 최소 8자 이상이어야 합니다.";
    return false;
  } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(password.value)) {
    passwordError.value = "비밀번호는 대문자, 소문자, 숫자를 포함해야 합니다.";
    return false;
  }
  passwordError.value = "";
  return true;
};

// 비밀번호 확인 유효성 검사
const validateConfirmPassword = () => {
  if (!confirmPassword.value) {
    confirmPasswordError.value = "비밀번호 확인을 입력해주세요.";
    return false;
  } else if (confirmPassword.value !== password.value) {
    confirmPasswordError.value = "비밀번호가 일치하지 않습니다.";
    return false;
  }
  confirmPasswordError.value = "";
  return true;
};

// 생년월일 유효성 검사
const validateBirthdate = () => {
  if (!birthdate.value) {
    birthdateError.value = "생년월일을 입력해주세요.";
    return false;
  }

  const today = new Date();
  const birthDate = new Date(birthdate.value);
  const age = today.getFullYear() - birthDate.getFullYear();

  if (birthDate > today) {
    birthdateError.value = "미래 날짜는 선택할 수 없습니다.";
    return false;
  } else if (age > 120) {
    birthdateError.value = "유효한 생년월일을 입력해주세요.";
    return false;
  }

  birthdateError.value = "";
  return true;
};

// 개인정보 동의 유효성 검사
const validateAgreement = () => {
  if (!agreement.value) {
    agreementError.value = "개인정보 수집 및 이용에 동의해주세요.";
    return false;
  }
  agreementError.value = "";
  return true;
};

// 시간 포맷팅 (초 -> 분:초)
const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${minutes}:${remainingSeconds < 10 ? "0" : ""}${remainingSeconds}`;
};

// 인증 코드 발송
const sendVerificationCode = async () => {
  if (!validateEmail()) return;

  try {
    isSendingCode.value = true;

    // 인증 코드 발송 API 호출
    const response = await sendEmailCode(email.value);
    // 인증 코드 발송 성공 처리
    isVerificationSent.value = true;

    // 타이머 시작
    startVerificationTimer();
    // 60초 후 재발송 버튼 활성화
    startResendTimer();
  } catch (error) {
    console.error("인증 코드 발송 오류:", error);
    alert("이메일 인증 코드 발송에 실패했습니다.");
  } finally {
    isSendingCode.value = false;
  }
};

// 인증 코드 재발송
const resendVerificationCode = async () => {
  if (!canResend.value) return;

  try {
    isSendingCode.value = true;
    canResend.value = false;

    // 인증 코드 발송 API 호출
    const response = await sendEmailCode(email.value);

    // 타이머 재설정
    clearInterval(verificationTimer.value);
    remainingTime.value = 300; // 5분 초기화
    startVerificationTimer();

    // 60초 후 재발송 버튼 활성화
    startResendTimer();
  } catch (error) {
    console.error("인증 코드 재발송 오류:", error);
    alert("이메일 인증 코드 발송에 실패했습니다.");
  } finally {
    isSendingCode.value = false;
  }
};

// 인증 코드 확인
const verifyCode = async () => {
  if (verificationCode.value.length !== 6) return;

  try {
    isVerifying.value = true;

    // 인증 코드 확인 API 호출
    const response = await verifyEmailCode(email.value, verificationCode.value);

    // 인증 성공 처리
    if (response.result === true) {
      emailVerified.value = true;
      clearInterval(verificationTimer.value);
      clearTimeout(resendTimer.value);
    } else {
      alert("인증 코드가 일치하지 않습니다. 다시 확인해주세요.");
    }
  } catch (error) {
    console.error("인증 코드 확인 오류:", error);
    // 오류 처리 로직
  } finally {
    isVerifying.value = false;
  }
};

// 이메일 변경
const changeEmail = () => {
  isVerificationSent.value = false;
  emailVerified.value = false;
  verificationCode.value = "";
  clearInterval(verificationTimer.value);
  clearTimeout(resendTimer.value);
};

// 인증 타이머 시작
const startVerificationTimer = () => {
  remainingTime.value = 300; // 5분 (초 단위)

  verificationTimer.value = setInterval(() => {
    remainingTime.value--;

    if (remainingTime.value <= 0) {
      clearInterval(verificationTimer.value);
      // 시간 초과 처리
      alert("인증 시간이 만료되었습니다. 인증 코드를 다시 발송해주세요.");
      isVerificationSent.value = true;
      canResend.value = true;
    }
  }, 1000);
};

// 재발송 타이머 시작
const startResendTimer = () => {
  canResend.value = false;

  resendTimer.value = setTimeout(() => {
    canResend.value = true;
  }, 60000); // 60초 후 재발송 가능
};

// 회원가입 처리
const handleRegister = async () => {
  // 모든 필드 유효성 검사
  const isNameValid = validateName();
  const isPasswordValid = validatePassword();
  const isConfirmPasswordValid = validateConfirmPassword();
  const isBirthdateValid = validateBirthdate();
  const isAgreementValid = validateAgreement();

  if (
    !isNameValid ||
    !isPasswordValid ||
    !isConfirmPasswordValid ||
    !isBirthdateValid ||
    !isAgreementValid ||
    !emailVerified.value
  ) {
    return;
  }

  try {
    isLoading.value = true;

    // 여기에 실제 회원가입 API 호출 로직 구현
    const response = await signup({
      name: name.value,
      email: email.value,
      password: password.value,
      birthdate: birthdate.value,
      profileImage: profileImageUrl.value || defaultProfileImage,
    });

    // 회원가입 성공 시 처리 (예: 로그인 페이지로 리다이렉트)

    router.push({ name: "Login" });
    // 성공 메시지 표시
    alert("회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.");
  } catch (error) {
    console.error("회원가입 오류:", error);
    alert("회원가입에 실패했습니다.");
    // 오류 처리 로직
  } finally {
    isLoading.value = false;
  }
};

// 컴포넌트 언마운트 시 타이머 정리
onUnmounted(() => {
  if (verificationTimer.value) {
    clearInterval(verificationTimer.value);
  }
  if (resendTimer.value) {
    clearTimeout(resendTimer.value);
  }
});
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: "Noto Sans KR", -apple-system, BlinkMacSystemFont, "Segoe UI",
    Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
  color: var(--text-color);
  line-height: 1.6;
}

.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--background-color);
  padding: 20px;
}

.register-card {
  width: 100%;
  max-width: 500px;
  background-color: var(--card-color);
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  padding: 32px;
  margin: 40px 0;
}

.register-header {
  text-align: center;
  margin-bottom: 24px;
}

.register-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
  color: var(--text-color);
}

.register-subtitle {
  font-size: 14px;
  color: var(--text-light);
}

.text-link {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
  transition: var(--transition);
}

.text-link:hover {
  color: var(--primary-hover);
  text-decoration: underline;
}

.register-form {
  margin-bottom: 24px;
}

/* 프로필 이미지 업로드 스타일 */
.profile-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.profile-preview {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background-color: #f0f0f0;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  margin-bottom: 12px;
  border: 2px dashed var(--border-color);
  transition: var(--transition);
}

.profile-preview:hover {
  border-color: var(--primary-color);
}

.profile-placeholder {
  color: var(--text-light);
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.preview-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-icon {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: var(--primary-color);
  color: white;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.file-input {
  display: none;
}

.profile-upload-text {
  font-size: 14px;
  color: var(--text-light);
  margin-top: 4px;
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

.input-container input[type="date"] {
  padding-right: 12px;
}

.input-container input:disabled {
  background-color: #f9f9f9;
  cursor: not-allowed;
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

.checkbox-container {
  display: flex;
  align-items: center;
}

.checkbox-container input[type="checkbox"] {
  margin-right: 8px;
  accent-color: var(--primary-color);
}

.checkbox-container label {
  font-size: 14px;
  color: var(--text-light);
  margin-bottom: 0;
}

.register-button {
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
  margin-top: 24px;
}

.register-button:hover:not(:disabled) {
  background-color: var(--primary-hover);
}

.register-button:disabled {
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

/* 이메일 인증 관련 스타일 */
.verification-button {
  position: absolute;
  right: 0;
  height: 100%;
  padding: 0 12px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-top-right-radius: var(--border-radius);
  border-bottom-right-radius: var(--border-radius);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition);
}

.verification-button:hover:not(:disabled) {
  background-color: var(--primary-hover);
}

.verification-button:disabled {
  background-color: #e0e0e0;
  color: #9e9e9e;
  cursor: not-allowed;
}

.verification-section {
  margin-top: 16px;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: var(--border-radius);
  border: 1px solid var(--border-color);
}

.verification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.verification-text {
  font-size: 14px;
  color: var(--text-color);
}

.verification-email {
  font-weight: 600;
}

.verification-timer {
  font-size: 14px;
  color: var(--primary-color);
  font-weight: 600;
}

.verification-code-container {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.verification-code-input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  font-size: 16px;
  letter-spacing: 2px;
  text-align: center;
}

.verification-code-input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px var(--primary-light);
}

.verify-button {
  padding: 0 16px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: var(--transition);
}

.verify-button:hover:not(:disabled) {
  background-color: var(--primary-hover);
}

.verify-button:disabled {
  background-color: #e0e0e0;
  color: #9e9e9e;
  cursor: not-allowed;
}

.verification-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
}

.resend-button,
.change-email-button {
  background: none;
  border: none;
  color: var(--primary-color);
  font-size: 14px;
  cursor: pointer;
  transition: var(--transition);
  padding: 0;
}

.resend-button:hover:not(:disabled),
.change-email-button:hover {
  color: var(--primary-hover);
  text-decoration: underline;
}

.resend-button:disabled {
  color: var(--text-light);
  cursor: not-allowed;
}

.verification-success {
  display: flex;
  align-items: center;
  color: var(--success-color);
  font-size: 14px;
  margin-top: 8px;
  font-weight: 500;
}

.verification-success svg {
  margin-right: 4px;
}

/* 반응형 디자인 */
@media (max-width: 576px) {
  .register-card {
    padding: 24px 16px;
  }

  .profile-preview {
    width: 100px;
    height: 100px;
  }

  .verification-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .verification-timer {
    margin-top: 4px;
  }

  .verification-code-container {
    flex-direction: column;
  }

  .verification-actions {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
