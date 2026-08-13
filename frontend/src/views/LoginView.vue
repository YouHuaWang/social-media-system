<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

import { login } from '../api/authApi'

const router = useRouter()

const form = reactive({
  phone: '',
  password: '',
})

const loading = ref(false)
const errorMessage = ref('')

async function handleLogin() {
  errorMessage.value = ''
  loading.value = true

  try {
    const response = await login(form)
    const data = response.data

    sessionStorage.setItem('loginPhone', form.phone)
    sessionStorage.setItem('loginUserId', data.userId)

    router.push('/verify-otp')
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '登入失敗'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1>登入</h1>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>手機號碼</label>

          <input v-model="form.phone" type="tel" placeholder="09xxxxxxxx" maxlength="10" required />
        </div>

        <div class="form-group">
          <label>密碼</label>

          <input v-model="form.password" type="password" placeholder="請輸入密碼" required />
        </div>

        <p v-if="errorMessage" class="error">
          {{ errorMessage }}
        </p>

        <button type="submit" :disabled="loading">
          {{ loading ? '登入中...' : '登入' }}
        </button>
      </form>

      <div class="forgot">
        <RouterLink to="/forgot-password"> 忘記密碼？ </RouterLink>
      </div>

      <div class="link">
        還沒有帳號？

        <RouterLink to="/register"> 註冊 </RouterLink>
      </div>
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
}

h1 {
  text-align: center;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 6px;
}

input {
  width: 100%;
  box-sizing: border-box;
  padding: 10px;
}

button {
  width: 100%;
  padding: 12px;
}

.error {
  color: #d32f2f;
}

.forgot {
  text-align: right;
  margin-top: 15px;
}

.link {
  text-align: center;
  margin-top: 20px;
}
</style>
