import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true, // 세션 쿠키 등을 자동 포함
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터 (예: 토큰 자동 첨부)
axiosInstance.interceptors.request.use(
  (config) => {
    console.log('[요청 발신]: ', config.method, config.url, config.data)
    // 로직
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터 (예: 에러 공통 처리)
axiosInstance.interceptors.response.use(
  (response) => {
    console.log('[응답 수신]: ', response.status, response.data)
    return response.data
  },
  (error) => {
    // 예: 401 에러 시 로그아웃 처리 등
    return Promise.reject(error);
  }
);

export default axiosInstance;