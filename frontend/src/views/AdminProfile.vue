<template>
  <div class="profile-container">
    <h2>管理员账号信息修改</h2>
    <form @submit.prevent="updateInfo" class="form">
      <lable>用户名：</lable>
      <input v-model="admin.username" placeholder="用户名" />
      <lable>密码：</lable>
      <input v-model="admin.password" placeholder="密码" />
      <lable>邮箱：</lable>
      <input v-model="admin.email" placeholder="邮箱" />
      <button type="submit">保存修改</button>
      <p class="message">{{ message }}</p>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const admin = ref({ username: '', password: '', email: '' })
const message = ref('')

onMounted(async () => {
  const res = await axios.get('http://localhost:8080/api/admin')
  if (res.data && res.data.length > 0) {
    admin.value = res.data[0]
  }
})

async function updateInfo() {
  try {
    await axios.put('http://localhost:8080/api/admin/update', admin.value)
    message.value = '修改成功'
  } catch (e) {
    message.value = '修改失败'
  }
}
</script>

<style scoped>
.profile-container {
  padding: 30px;
  max-width: 500px;
  margin: auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0,0,0,0.05);
}
.form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
}
button {
  background: #3c8dbc;
  color: #fff;
  padding: 10px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.message {
  color: #666;
  margin-top: 8px;
}
</style>
