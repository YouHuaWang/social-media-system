import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token'),
    userId: localStorage.getItem('userId'),
  }),

  getters: {
    isLoggedIn: (state) => {
      return !!state.token
    },
  },

  actions: {
    setAuth(userId, token) {
      this.userId = userId
      this.token = token

      localStorage.setItem('userId', userId)

      localStorage.setItem('token', token)
    },

    logout() {
      this.userId = null
      this.token = null

      localStorage.removeItem('userId')
      localStorage.removeItem('token')
    },
  },
})
