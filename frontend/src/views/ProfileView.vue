<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

import { useAuthStore } from '../stores/auth'
import { getMyProfile } from '../api/userApi'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)

async function loadProfile() {
  try {
    const response = await getMyProfile()

    authStore.setUser(response.data)
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

function goBack() {
  router.push('/home')
}

onMounted(() => {
  loadProfile()
})
</script>

<template>
  <div class="profile-page">
    <header>
      <button @click="goBack">← 返回首頁</button>

      <h1>個人資料</h1>
    </header>

    <main v-if="!loading">
      <div class="profile-card">
        <div class="profile-avatar">
          <img v-if="authStore.coverImage" :src="authStore.coverImage" alt="使用者頭像" />

          <span v-else>
            {{ authStore.userName ? authStore.userName.charAt(0) : '?' }}
          </span>
        </div>

        <h2>
          {{ authStore.userName }}
        </h2>

        <div class="profile-info">
          <p>
            <strong> 手機號碼 </strong>

            {{ authStore.user?.phone }}
          </p>

          <p>
            <strong> Email </strong>

            {{ authStore.user?.email }}
          </p>

          <p>
            <strong> 個人簡介 </strong>

            {{ authStore.user?.biography || '尚未填寫個人簡介' }}
          </p>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.profile-page {
  min-height: 100vh;

  background: #f5f5f5;
}

header {
  padding: 20px 30px;

  background: white;

  border-bottom: 1px solid #ddd;

  display: flex;

  align-items: center;

  gap: 30px;
}

header h1 {
  margin: 0;
}

header button {
  padding: 8px 15px;
}

main {
  display: flex;

  justify-content: center;

  padding: 40px;
}

.profile-card {
  width: 500px;

  padding: 40px;

  background: white;

  border-radius: 12px;

  text-align: center;
}

.profile-avatar {
  width: 100px;

  height: 100px;

  margin: 0 auto 20px;

  border-radius: 50%;

  overflow: hidden;

  background: #ddd;

  display: flex;

  justify-content: center;

  align-items: center;

  font-size: 40px;

  font-weight: bold;
}

.profile-avatar img {
  width: 100%;

  height: 100%;

  object-fit: cover;
}

.profile-info {
  margin-top: 30px;

  text-align: left;
}

.profile-info p {
  padding: 12px 0;

  border-bottom: 1px solid #eee;
}

.profile-info strong {
  display: block;

  margin-bottom: 5px;
}
</style>
