<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

import { register } from '../api/authApi'

const router = useRouter()

const form = reactive({
  userName: '',
  phone: '',
  email: '',
  password: '',
})

const loading = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

async function handleRegister() {
  errorMessage.value = ''
  successMessage.value = ''

  loading.value = true

  try {
    const response = await register(form)

    successMessage.value = response.data.message || '註冊成功'

    setTimeout(() => {
      router.push('/login')
    }, 1000)
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '註冊失敗'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1>註冊帳號</h1>

      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>使用者名稱</label>

          <input
            v-model="form.userName"
            type="text"
            placeholder="請輸入使用者名稱"
            maxlength="50"
            required
          />
        </div>

        <div class="form-group">
          <label>手機號碼</label>

          <input
            v-model="form.phone"
            type="tel"
            placeholder="0912345678"
            pattern="09[0-9]{8}"
            maxlength="10"
            required
          />
        </div>

        <div class="form-group">
          <label>Email</label>

          <input
            v-model="form.email"
            type="email"
            placeholder="example@gmail.com"
            maxlength="100"
            required
          />
        </div>

        <div class="form-group">
          <label>密碼</label>

          <input
            v-model="form.password"
            type="password"
            placeholder="8-16字元，至少英文與數字"
            minlength="8"
            maxlength="16"
            required
          />
        </div>

        <p v-if="errorMessage" class="error">
          {{ errorMessage }}
        </p>

        <p v-if="successMessage" class="success">
          {{ successMessage }}
        </p>

        <button type="submit" :disabled="loading">
          {{ loading ? '註冊中...' : '註冊' }}
        </button>
      </form>

      <div class="link">
        已經有帳號？

        <RouterLink to="/login"> 登入 </RouterLink>
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
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 18px;
}

label {
  display: block;
  margin-bottom: 6px;
}

input {
  width: 100%;
  box-sizing: border-box;
  padding: 10px;
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

.success {
  color: #2e7d32;
}

.link {
  margin-top: 20px;
  text-align: center;
}
</style>
