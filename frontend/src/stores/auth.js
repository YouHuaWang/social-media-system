import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token'),
    userId: localStorage.getItem('userId'),

    user: null,
  }),

  getters: {
    isLoggedIn: (state) => {
      return !!state.token
    },

    userName: (state) => {
      return state.user?.userName || ''
    },

    coverImage: (state) => {
      return state.user?.coverImage || ''
    },
  },

  actions: {
    setAuth(userId, token) {
      this.userId = userId
      this.token = token

      localStorage.setItem('userId', userId)

      localStorage.setItem('token', token)
    },

    setUser(user) {
      this.user = user
    },

    logout() {
      this.userId = null
      this.token = null
      this.user = null

      localStorage.removeItem('token')
      localStorage.removeItem('userId')
    },
  },
})
