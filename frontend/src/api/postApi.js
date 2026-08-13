import api from './axios'

export function getPosts() {
  return api.get('/posts')
}

export function createPost(data) {
  return api.post('/posts', data)
}

export function updatePost(postId, data) {
  return api.put(`/posts/${postId}`, data)
}

export function deletePost(postId) {
  return api.delete(`/posts/${postId}`)
}

export function createComment(postId, data) {
  return api.post(`/posts/${postId}/comments`, data)
}
