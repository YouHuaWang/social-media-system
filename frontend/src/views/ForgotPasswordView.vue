<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = reactive({
  email: '',
})

const loading = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

async function handleForgotPassword() {
  errorMessage.value = ''
  successMessage.value = ''

  loading.value = true

  try {
    // Frontend Only Now.

    successMessage.value = '如果此 Email 已註冊，系統將寄送密碼重設驗證信。'
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '處理失敗，請稍後再試。'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1>忘記密碼</h1>

      <p class="description">請輸入註冊時使用的 Email， 系統將寄送密碼重設驗證信。</p>

      <form @submit.prevent="handleForgotPassword">
        <div class="form-group">
          <label for="email"> Email </label>

          <input
            id="email"
            v-model="form.email"
            type="email"
            placeholder="example@gmail.com"
            maxlength="100"
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
          {{ loading ? '處理中...' : '寄送驗證信' }}
        </button>
      </form>

      <div class="links">
        <RouterLink to="/login"> 返回登入 </RouterLink>
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

  margin-bottom: 20px;
}

.description {
  color: #666;

  line-height: 1.6;

  margin-bottom: 25px;
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

  opacity: 0.6;
}

.error {
  color: #d32f2f;

  margin-bottom: 15px;
}

.success {
  color: #2e7d32;

  margin-bottom: 15px;
}

.links {
  text-align: center;

  margin-top: 20px;
}
</style>
