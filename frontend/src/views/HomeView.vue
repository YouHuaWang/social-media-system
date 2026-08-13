<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getPosts, createPost, updatePost, deletePost, createComment } from '../api/postApi'

const router = useRouter()
const authStore = useAuthStore()

// Post
const posts = ref([])

const postContent = ref('')
const postImage = ref('')

const loading = ref(false)
const submitting = ref(false)

const errorMessage = ref('')
const successMessage = ref('')

const editingPostId = ref(null)
const editContent = ref('')
const editImage = ref('')

// Comment
const commentContents = ref({})
const commentSubmitting = ref({})

async function loadPosts() {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await getPosts()

    posts.value = response.data
  } catch (error) {
    console.error(error)

    errorMessage.value = error.response?.data?.message || '取得貼文失敗'
  } finally {
    loading.value = false
  }
}

async function submitPost() {
  if (!postContent.value.trim()) {
    errorMessage.value = '請輸入貼文內容'
    return
  }

  submitting.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const response = await createPost({
      content: postContent.value,
      image: postImage.value || null,
    })

    await loadPosts()

    postContent.value = ''
    postImage.value = ''

    successMessage.value = '發文成功'
  } catch (error) {
    console.error(error)

    errorMessage.value = error.response?.data?.message || '發文失敗'
  } finally {
    submitting.value = false
  }
}

function startEdit(post) {
  editingPostId.value = post.postId
  editContent.value = post.content
  editImage.value = post.image || ''
}

function cancelEdit() {
  editingPostId.value = null
  editContent.value = ''
  editImage.value = ''
}

async function saveEdit(postId) {
  if (!editContent.value.trim()) {
    errorMessage.value = '貼文內容不可為空白'
    return
  }

  try {
    await updatePost(postId, {
      content: editContent.value,
      image: editImage.value || null,
    })

    await loadPosts()

    cancelEdit()

    successMessage.value = '貼文修改成功'
  } catch (error) {
    console.error(error)

    errorMessage.value = error.response?.data?.message || '修改貼文失敗'
  }
}

async function removePost(postId) {
  const confirmed = window.confirm('確定要刪除這篇貼文嗎？')

  if (!confirmed) {
    return
  }

  try {
    await deletePost(postId)
    await loadPosts()
    successMessage.value = '貼文刪除成功'
  } catch (error) {
    console.error(error)
    errorMessage.value = error.response?.data?.message || '刪除貼文失敗'
  }
}

async function submitComment(postId) {
  const content = commentContents.value[postId]?.trim()

  if (!content) {
    errorMessage.value = '請輸入留言內容'
    return
  }

  commentSubmitting.value[postId] = true

  errorMessage.value = ''

  try {
    await createComment(postId, {
      content,
    })

    commentContents.value[postId] = ''

    successMessage.value = '留言成功'
  } catch (error) {
    console.error(error)

    errorMessage.value = error.response?.data?.message || '留言失敗'
  } finally {
    commentSubmitting.value[postId] = false
  }
}

function isMyPost(post) {
  return String(post.userId) === String(authStore.userId)
}

function logout() {
  authStore.logout()

  router.push('/login')
}

// Init
onMounted(() => {
  loadPosts()
})
</script>

<template>
  <div class="page">
    <!-- NavBar -->
    <nav class="navbar">
      <h2>Social Media</h2>

      <div class="navbar-right">
        <span> 使用者 {{ authStore.userId }} </span>

        <button class="logout-button" @click="logout">登出</button>
      </div>
    </nav>

    <!-- Main -->
    <main class="main">
      <!-- <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>

      <div v-if="successMessage" class="success-message">
        {{ successMessage }}
      </div> -->

      <!-- Create Post -->
      <section class="create-post">
        <h2>發布貼文</h2>

        <textarea v-model="postContent" placeholder="分享一些事情吧..." maxlength="2000" rows="5" />

        <div class="character-count">{{ postContent.length }} / 2000</div>

        <input v-model="postImage" type="text" placeholder="圖片網址（選填）" maxlength="500" />

        <button class="primary-button" :disabled="submitting" @click="submitPost">
          {{ submitting ? '發布中...' : '發布貼文' }}
        </button>
      </section>

      <!-- Post List -->
      <section class="post-section">
        <h2>所有貼文</h2>

        <div v-if="loading" class="loading">載入貼文中...</div>

        <div v-else-if="posts.length === 0" class="empty">目前暫無貼文</div>

        <article v-for="post in posts" :key="post.postId" class="post-card">
          <!-- Post Header -->
          <div class="post-header">
            <div>
              <strong>
                {{ post.userName }}
              </strong>

              <small>
                {{ post.createdAt }}
              </small>
            </div>

            <div v-if="isMyPost(post)" class="post-actions">
              <button @click="startEdit(post)">編輯</button>

              <button class="delete-button" @click="removePost(post.postId)">刪除</button>
            </div>
          </div>

          <!-- Edit -->
          <div v-if="editingPostId === post.postId" class="edit-area">
            <textarea v-model="editContent" maxlength="2000" rows="5" />

            <input v-model="editImage" type="text" maxlength="500" placeholder="圖片網址" />

            <div>
              <button class="primary-button" @click="saveEdit(post.postId)">儲存</button>

              <button @click="cancelEdit">取消</button>
            </div>
          </div>

          <!-- Post Content -->
          <div v-else class="post-content">
            <p>
              {{ post.content }}
            </p>

            <img v-if="post.image" :src="post.image" class="post-image" alt="貼文圖片" />
          </div>

          <!-- Comment -->
          <div class="comment-area">
            <input
              v-model="commentContents[post.postId]"
              type="text"
              maxlength="1000"
              placeholder="寫下留言..."
              @keyup.enter="submitComment(post.postId)"
            />

            <button :disabled="commentSubmitting[post.postId]" @click="submitComment(post.postId)">
              {{ commentSubmitting[post.postId] ? '送出中...' : '留言' }}
            </button>
          </div>
        </article>
      </section>
    </main>
  </div>
</template>

<style scoped>
* {
  box-sizing: border-box;
}

.page {
  min-height: 100vh;
  background: #f5f5f5;
}

/* Navbar */

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 30px;
  background: white;
  border-bottom: 1px solid #ddd;
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logout-button {
  padding: 8px 15px;
  border: 1px solid #ccc;
  background: white;
  cursor: pointer;
}

/* Main */

.main {
  width: 700px;
  max-width: calc(100% - 30px);
  margin: 30px auto;
}

/* Message */

.error-message {
  padding: 12px;
  margin-bottom: 15px;
  color: #b00020;
  background: #ffe5e5;
  border-radius: 5px;
}

.success-message {
  padding: 12px;
  margin-bottom: 15px;
  color: #176b32;
  background: #e5f7ea;
  border-radius: 5px;
}

/* Create Post */

.create-post {
  padding: 20px;
  margin-bottom: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

.create-post textarea,
.create-post input {
  width: 100%;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 15px;
}

.character-count {
  margin-bottom: 10px;
  text-align: right;
  color: #888;
  font-size: 13px;
}

.primary-button {
  padding: 9px 18px;
  border: none;
  border-radius: 5px;
  background: #333;
  color: white;
  cursor: pointer;
}

.primary-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Post */

.post-section h2 {
  margin-bottom: 15px;
}

.post-card {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.post-header strong {
  display: block;
  font-size: 16px;
}

.post-header small {
  display: block;
  margin-top: 4px;
  color: #888;
}

.post-actions {
  display: flex;
  gap: 8px;
}

.post-actions button {
  padding: 5px 10px;
  border: 1px solid #ccc;
  background: white;
  cursor: pointer;
}

.delete-button {
  color: #c62828;
}

.post-content p {
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.post-image {
  display: block;
  max-width: 100%;
  max-height: 500px;
  margin-top: 10px;
  border-radius: 5px;
}

/* Edit */

.edit-area textarea,
.edit-area input {
  width: 100%;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.edit-area button {
  margin-right: 8px;
  padding: 8px 15px;
}

/* Comment */

.comment-area {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.comment-area input {
  flex: 1;
  padding: 9px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.comment-area button {
  padding: 8px 15px;
  border: 1px solid #ccc;
  background: white;
  border-radius: 5px;
  cursor: pointer;
}

/* Other */

.loading,
.empty {
  padding: 30px;
  text-align: center;
  color: #777;
}
</style>
