<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

function logout() {
  authStore.logout()

  router.push('/login')
}
</script>

<template>
  <div>
    <nav>
      <h2>Social Media</h2>

      <div>
        <span> 使用者 {{ authStore.userId }} </span>

        <button @click="logout">登出</button>
      </div>
    </nav>

    <main>
      <h1>首頁</h1>

      <p>登入成功！</p>

      <p>JWT Token 已建立。</p>
    </main>
  </div>
</template>

<style scoped>
nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 30px;
  border-bottom: 1px solid #ddd;
}

main {
  padding: 30px;
}

button {
  margin-left: 20px;
  padding: 8px 15px;
}
</style>
