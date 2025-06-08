<template>
  <div class="login-container">
    <!-- 背景图左侧 -->
    <div class="left-box">
      <img src="/img/login-bg.png" alt="bg" />
    </div>

    <!-- 登录、注册、列表在右侧 -->
    <div class="form-box">
      <h2 class="title">内蒙古大学智慧教务系统</h2>

      <!-- 登录表单 -->
      <form @submit.prevent="handleLogin" class="form">
        <input v-model="loginForm.username" placeholder="请输入学号或工号" required />
        <input v-model="loginForm.password" type="password" placeholder="密码" required />
        <button type="submit">登录系统</button>
      </form>

      <!-- 注册表单 -->
      <form @submit.prevent="handleRegister" class="form register-form">
        <input v-model="registerForm.username" placeholder="注册用户名" required />
        <input v-model="registerForm.password" type="password" placeholder="注册密码" required />
        <input v-model="registerForm.email" placeholder="注册邮箱" />
        <button type="submit">注册管理员</button>
        <p class="message">{{ registerMessage }}</p>
      </form>

      <!-- 管理员信息表 -->
      <div class="manager-page" v-if="adminList.length">
        <h3>管理员信息列表</h3>
        <div v-if="loading">加载中...</div>
        <div v-else>
          <table border="1" cellpadding="6">
            <thead>
            <tr>
              <th v-for="key in headers" :key="key">{{ key }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in adminList" :key="item.id">
              <td v-for="key in headers" :key="key">{{ item[key] }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- 修改信息区域 -->
      <div v-if="editForm.username" class="update-box">
        <h3>修改当前管理员信息</h3>
        <form @submit.prevent="updateAdminInfo" class="form">
          <input v-model="editForm.username" placeholder="用户名" required />
          <input v-model="editForm.password" type="password" placeholder="密码" required />
          <input v-model="editForm.email" placeholder="邮箱" />
          <button type="submit">保存修改</button>
          <p class="message">{{ updateMessage }}</p>
        </form>
      </div>

      <footer>© 2025 内蒙古大学智慧教务系统<br>JAVA项目作业</footer>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// 定义响应式数据（靠前，防止 template 报错）
const loginForm = ref({ username: '', password: '' })
const registerForm = ref({ username: '', password: '', email: '' })
const editForm = ref({ username: '', password: '', email: '' })
const adminList = ref([])
const headers = ref([])
const loading = ref(true)
const registerMessage = ref('')
const updateMessage = ref('')

async function handleLogin() {
  const res = await axios.post('http://localhost:8080/api/admin/login', loginForm.value)
  if (res.data === '登录成功') {
    alert('登录成功')
    await loadAdmins()
    setEditFormFromLoggedInUser()
    router.push('/dashboard')
  } else {
    alert('登录失败')
  }
}

async function handleRegister() {
  try {
    const res = await axios.post('http://localhost:8080/api/admin/register', registerForm.value)
    registerMessage.value = res.data
    await loadAdmins()
  } catch (e) {
    registerMessage.value = '注册失败'
  }
}

async function updateAdminInfo() {
  try {
    await axios.put('http://localhost:8080/api/admin/update', editForm.value)
    updateMessage.value = '更新成功'
    await loadAdmins()
  } catch (e) {
    updateMessage.value = '更新失败'
  }
}

async function loadAdmins() {
  try {
    loading.value = true
    const res = await axios.get('http://localhost:8080/api/admin')
    adminList.value = res.data
    if (adminList.value.length > 0) {
      headers.value = Object.keys(adminList.value[0])
    }
  } catch (e) {
    console.error('加载管理员失败', e)
  } finally {
    loading.value = false
  }
}

function setEditFormFromLoggedInUser() {
  const user = adminList.value.find(a => a.username === loginForm.value.username)
  if (user) {
    editForm.value = { ...user }
  }
}
</script>

<style scoped>
.login-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: auto;
  background-color: #f4f6f9;
}

.left-box {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to right, #6cc9ff, #3cd3ad);
  display: flex;
  justify-content: flex-start;
  align-items: center;
  z-index: 1;
}

.left-box img {
  height: 80%;
  margin-left: 5%;
  object-fit: contain;
  opacity: 0.95;
}

.form-box {
  position: absolute;
  right: 10%;
  top: 50%;
  transform: translateY(-50%);
  background-color: #fff;
  z-index: 2;
  padding: 40px;
  border-radius: 10px;
  width: 500px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.form button {
  background: linear-gradient(to right, #2196f3, #4caf50);
  border: none;
  padding: 10px;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}

.register-form {
  margin-top: 30px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.manager-page {
  margin-top: 40px;
  background: #fafafa;
  padding: 20px;
  border-radius: 8px;
  text-align: left;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 8px;
  border: 1px solid #ddd;
}

.message {
  margin-top: 8px;
  font-size: 12px;
  color: #888;
}

footer {
  margin-top: 30px;
  font-size: 12px;
  color: #aaa;
}

.update-box {
  margin-top: 30px;
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #eee;
}
</style>
