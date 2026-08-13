<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

import { useAuthStore } from '../stores/auth'
import { getMyProfile } from '../api/userApi'

const router = useRouter()
const authStore = useAuthStore()

const showProfileMenu = ref(false)
const loading = ref(true)
const errorMessage = ref('')

async function loadProfile() {
  try {
    const response = await getMyProfile()

    authStore.setUser(response.data)
  } catch (error) {
    console.error('取得使用者資料失敗:', error)

    errorMessage.value = error.response?.data?.message || '無法取得使用者資料'
  } finally {
    loading.value = false
  }
}

function toggleProfileMenu() {
  showProfileMenu.value = !showProfileMenu.value
}

function goToProfile() {
  showProfileMenu.value = false

  router.push('/profile')
}

function logout() {
  showProfileMenu.value = false

  authStore.logout()

  router.push('/login')
}

onMounted(() => {
  loadProfile()
})
</script>

<template>
  <div class="home-page">
    <nav class="navbar">
      <div class="brand">
        <h2>Social Media</h2>
      </div>

      <div class="profile-area">
        <button class="profile-button" @click="toggleProfileMenu">
          <!-- 頭像 -->
          <div class="avatar">
            <img v-if="authStore.coverImage" :src="authStore.coverImage" alt="使用者頭像" />

            <span v-else>
              {{ authStore.userName ? authStore.userName.charAt(0) : '?' }}
            </span>
          </div>

          <!-- 使用者名稱 -->
          <span class="user-name">
            {{ loading ? '載入中...' : authStore.userName || '使用者' }}
          </span>

          <!-- 下拉箭頭 -->
          <span class="arrow"> ▾ </span>
        </button>

        <!-- Dropdown -->
        <div v-if="showProfileMenu" class="profile-menu">
          <div class="menu-user-info">
            <div class="menu-avatar">
              <img v-if="authStore.coverImage" :src="authStore.coverImage" alt="使用者頭像" />

              <span v-else>
                {{ authStore.userName ? authStore.userName.charAt(0) : '?' }}
              </span>
            </div>

            <div>
              <strong>
                {{ authStore.userName }}
              </strong>

              <small>
                {{ authStore.user?.email }}
              </small>
            </div>
          </div>

          <div class="menu-divider"></div>

          <button class="menu-item" @click="goToProfile">👤 個人資料</button>

          <button class="menu-item logout-item" @click="logout">🚪 登出</button>
        </div>
      </div>
    </nav>

    <main>
      <h1>首頁</h1>

      <p>
        歡迎回來，
        <strong>
          {{ authStore.userName }}
        </strong>
        ！
      </p>

      <p v-if="errorMessage">
        {{ errorMessage }}
      </p>
    </main>
  </div>
</template>

<style scoped>
.home-page {
  min-height: 100vh;
  background: #f5f5f5;
}

/* Navbar */

.navbar {
  height: 64px;

  padding: 0 30px;

  display: flex;

  justify-content: space-between;

  align-items: center;

  background: white;

  border-bottom: 1px solid #ddd;
}

.brand h2 {
  margin: 0;
}

/* Profile */

.profile-area {
  position: relative;
}

.profile-button {
  display: flex;

  align-items: center;

  gap: 10px;

  padding: 6px 10px;

  background: transparent;

  border: none;

  cursor: pointer;

  border-radius: 8px;
}

.profile-button:hover {
  background: #f0f0f0;
}

.avatar {
  width: 40px;

  height: 40px;

  border-radius: 50%;

  overflow: hidden;

  background: #ddd;

  display: flex;

  justify-content: center;

  align-items: center;

  font-weight: bold;

  font-size: 18px;
}

.avatar img {
  width: 100%;

  height: 100%;

  object-fit: cover;
}

.user-name {
  font-size: 15px;

  font-weight: 500;
}

.arrow {
  font-size: 14px;
}

/* Dropdown */

.profile-menu {
  position: absolute;

  top: 55px;

  right: 0;

  width: 250px;

  background: white;

  border-radius: 10px;

  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);

  overflow: hidden;

  z-index: 1000;
}

/* Menu User */

.menu-user-info {
  display: flex;

  align-items: center;

  gap: 12px;

  padding: 15px;
}

.menu-avatar {
  width: 45px;

  height: 45px;

  border-radius: 50%;

  overflow: hidden;

  background: #ddd;

  display: flex;

  align-items: center;

  justify-content: center;

  font-weight: bold;
}

.menu-avatar img {
  width: 100%;

  height: 100%;

  object-fit: cover;
}

.menu-user-info strong {
  display: block;
}

.menu-user-info small {
  display: block;

  margin-top: 4px;

  color: #777;
}

.menu-divider {
  border-top: 1px solid #eee;
}

/* Menu Button */

.menu-item {
  width: 100%;

  padding: 13px 16px;

  border: none;

  background: white;

  text-align: left;

  cursor: pointer;

  font-size: 14px;
}

.menu-item:hover {
  background: #f5f5f5;
}

.logout-item {
  color: #d32f2f;
}

/* Main */

main {
  max-width: 900px;

  margin: 0 auto;

  padding: 40px 30px;
}
</style>
