import api from './axios'

export function getMyProfile() {
  return api.get('/users/me')
}
