<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

import { verifyOtp } from '../api/authApi'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const phone = sessionStorage.getItem('loginPhone')

if (!phone) {
  router.push('/login')
}

const otp = ref('')

const loading = ref(false)
const errorMessage = ref('')

async function handleVerifyOtp() {
  errorMessage.value = ''
  loading.value = true

  try {
    const response = await verifyOtp({
      phone: phone,
      otp: otp.value,
    })

    const data = response.data

    /*
     * OTP 驗證成功
     * 後端應該在這裡回傳 JWT
     */
    authStore.setAuth(data.userId, data.token)

    sessionStorage.removeItem('loginPhone')
    sessionStorage.removeItem('loginUserId')

    router.push('/home')
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '驗證碼錯誤或已過期'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1>手機驗證</h1>

      <p>
        驗證碼已發送至：
        <strong>{{ phone }}</strong>
      </p>

      <form @submit.prevent="handleVerifyOtp">
        <input
          v-model="otp"
          type="text"
          inputmode="numeric"
          maxlength="6"
          placeholder="請輸入6位數驗證碼"
          required
        />

        <p v-if="errorMessage" class="error">
          {{ errorMessage }}
        </p>

        <button type="submit" :disabled="loading">
          {{ loading ? '驗證中...' : '確認驗證' }}
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  min-height: 100vh;

  display: flex;
  justify-content: center;
  align-items: center;

  background: #f5f5f5;
}

.auth-card {
  width: 400px;

  padding: 40px;

  background: white;

  border-radius: 12px;

  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

p {
  text-align: center;
}

input {
  width: 100%;
  box-sizing: border-box;

  padding: 12px;

  margin-top: 20px;
  margin-bottom: 15px;

  border: 1px solid #ccc;
  border-radius: 6px;
}

button {
  width: 100%;

  padding: 12px;

  border: none;
  border-radius: 6px;

  cursor: pointer;
}

button:disabled {
  cursor: not-allowed;
}

.error {
  color: #d32f2f;
}
</style>
