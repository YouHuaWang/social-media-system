import api from './axios'

export function register(data) {
  return api.post('/auth/register', data)
}

export function login(data) {
  return api.post('/auth/login', data)
}

export function verifyOtp(data) {
  return api.post('/auth/verify-otp', data)
}

// 忘記密碼
// export function forgotPassword(data) {
//     return api.post("/auth/forgot-password", data);
// }

// export function resetPassword(data) {
//     return api.post("/auth/reset-password", data);
// }
