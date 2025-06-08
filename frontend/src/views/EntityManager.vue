<template>
  <div class="manager-page">
    <h2>{{ title }}</h2>

    <!-- 搜索框和数据过滤 -->
    <div class="search-container">
      <input v-model="searchQuery" type="text" placeholder="请输入学号或姓名进行查询" class="search-input" @input="searchData"/>
      <button @click="openAddModal" class="add-btn">添加</button>
    </div>

    <!-- 数据加载中 -->
    <div v-if="loading">加载中...</div>

    <!-- 数据表格 -->
    <!-- 数据表格 -->
    <div v-else>
      <table class="table">
        <thead>
        <tr>
          <th v-for="key in headers" :key="key">{{ fieldLabel(key) }}</th>
          <!-- 只在学生管理页面显示选课情况列 -->
          <th v-if="page === 'students'">选课情况</th>
          <th v-if="formFields.length > 0">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in filteredData" :key="item.id">
          <td v-for="key in headers" :key="key">{{ item[key] }}</td>
          <!-- 只在学生管理页面显示选课情况 -->
          <td v-if="page === 'students'">
            <span v-if="item.selectedCourse">{{ item.selectedCourse.name }}</span>
            <span v-else>尚未选课</span>
          </td>
          <td v-if="formFields.length > 0">
            <button class="edit-btn" @click="edit(item)">编辑</button>
            <button class="delete-btn" @click="remove(item.id)">删除</button>
            <!-- 只在学生管理页面显示选课按钮 -->
            <button v-if="page === 'students' && !item.selectedCourse" class="select-btn" @click="openSelectCourseModal(item)">选课</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- 弹出选课窗口 -->
  <div v-if="showSelectCourseModal" class="modal-overlay">
    <div class="modal-content">
      <h3>为 {{ selectedStudent.name }} 选课</h3>
      <form @submit.prevent="handleSelectCourse">
        <div class="form-group">
          <label for="course">选择课程</label>
          <select v-model="selectedCourse" required id="course">
            <option value="" disabled>请选择课程</option>
            <option v-for="course in courses" :key="course.id" :value="course.id">{{ course.name }}</option>
          </select>
        </div>
        <button type="submit" class="submit-btn">确认选课</button>
        <button type="button" @click="closeSelectCourseModal" class="cancel-btn">取消</button>
      </form>
    </div>
  </div>

  <!-- 弹出编辑或添加信息窗口 -->
  <div v-if="showModal" class="modal-overlay">
    <div class="modal-content">
      <h3>{{ isEdit ? '编辑信息' : '添加信息' }}</h3>
      <form @submit.prevent="handleSubmit" class="editor-form">
        <div v-for="field in formFields" :key="field" class="form-group">
          <label :for="field">{{ fieldLabel(field) }}</label>
          <input
              v-if="field !== 'gender'"
              v-model="form[field]"
              :placeholder="fieldLabel(field)"
              required
              :id="field"
          />
          <select v-if="field === 'gender'" v-model="form.gender" required :id="field">
            <option value="" disabled>请选择性别</option>
            <option value="男">男</option>
            <option value="女">女</option>
          </select>
        </div>
        <button type="submit" class="submit-btn">{{ isEdit ? '保存修改' : '添加' }}</button>
        <button type="button" @click="cancelModal" class="cancel-btn">取消</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted, watch } from 'vue'

const props = defineProps({
  page: String,
  title: String,
  api: String,
  editable: Boolean,
  formFields: {
    type: Array,
    default: () => []
  }
})

const dataList = ref([]) // 存储所有数据
const headers = ref([]) // 表头
const filteredData = ref([]) // 过滤后的数据
const loading = ref(true) // 加载状态
const isEdit = ref(false)
const showModal = ref(false)
const showSelectCourseModal = ref(false) // 控制选课窗口的显示
const form = ref({}) // 表单数据
const searchQuery = ref('') // 搜索框值
const selectedStudent = ref(null) // 当前选择的学生
const selectedCourse = ref(null) // 当前选择的课程
const courses = ref([]) // 所有课程列表

async function load() {
  loading.value = true
  try {
    const res = await axios.get(props.api)
    dataList.value = res.data
    filteredData.value = dataList.value
    if (dataList.value.length > 0) {
      headers.value = Object.keys(dataList.value[0]) // 动态获取表头
    }
  } catch (e) {
    console.error('加载失败', e)
  } finally {
    loading.value = false
  }

  // 加载课程列表
  const courseRes = await axios.get('http://localhost:8080/api/course') // 假设课程数据来自这个API
  courses.value = courseRes.data
}

// 编辑功能
function edit(row) {
  form.value = { ...row }
  isEdit.value = true
  showModal.value = true
}

// 取消弹出框
function cancelModal() {
  showModal.value = false
}

// 提交表单
async function handleSubmit() {
  const url = props.api + (isEdit.value ? `/${form.value.id}` : '')
  const method = isEdit.value ? 'put' : 'post'
  await axios[method](url, form.value)
  form.value = {}
  isEdit.value = false
  showModal.value = false
  load()
}

// 删除数据
async function remove(id) {
  await axios.delete(`${props.api}/${id}`)
  load()
}

// 根据搜索框过滤数据
function searchData() {
  filteredData.value = dataList.value.filter(item =>
      item.name.includes(searchQuery.value) || item.id.toString().includes(searchQuery.value)
  )
}

// 打开选课弹窗
function openSelectCourseModal(student) {
  selectedStudent.value = student
  showSelectCourseModal.value = true
}

// 关闭选课弹窗
function closeSelectCourseModal() {
  showSelectCourseModal.value = false
  selectedCourse.value = null
}

// 处理选课
async function handleSelectCourse() {
  if (selectedCourse.value) {
    await axios.post('http://localhost:8080/api/selection', {
      studentId: selectedStudent.value.id,
      courseId: selectedCourse.value
    })
    closeSelectCourseModal()
    load()
  } else {
    alert('请选择课程')
  }
}

// 字段标签
function fieldLabel(field) {
  switch (field) {
    case 'id': return '序号';
    case 'name': return '姓名';
    case 'gender': return '性别';
    case 'age': return '年龄';
    case 'className': return '班级';
    case 'department': return '部门';
    case 'credits': return '学分';
    case 'description': return '备注';
    default: return field;
  }
}
// 打开添加信息的弹窗
function openAddModal() {
  form.value = {}
  isEdit.value = false
  showModal.value = true
}

onMounted(load)
</script>

<style scoped>
.manager-page {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
}

.editor-form {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.editor-form input,
.editor-form select {
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 200px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-size: 14px;
  margin-bottom: 4px;
}

button {
  padding: 6px 12px;
  border: none;
  background-color: #3f51b5;
  color: #fff;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #2196f3;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.table th,
.table td {
  padding: 12px 15px;
  border: 1px solid #ddd;
  text-align: center;
}

.table th {
  background-color: #f4f7fc;
  font-weight: bold;
}

button.edit-btn {
  background-color: #4caf50;
}

button.edit-btn:hover {
  background-color: #45a049;
}

button.delete-btn {
  background-color: #f44336;
}

button.delete-btn:hover {
  background-color: #e53935;
}

/* 弹出框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  width: 400px;
}

.modal-content form input,
.modal-content form select {
  margin-bottom: 15px;
  width: 100%;
}

.modal-content button {
  background-color: #2196f3;
  margin-right: 10px;
}

.modal-content button:hover {
  background-color: #1976d2;
}

.modal-content button[type="button"] {
  background-color: #f44336;
}

.modal-content button[type="button"]:hover {
  background-color: #e53935;
}

/* 搜索框样式 */
.search-container {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.search-input {
  padding: 10px;
  width: 250px;
  border-radius: 5px;
  border: 1px solid #ccc;
  margin-right: 10px;
}

.add-btn {
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.add-btn:hover {
  background-color: #45a049;
}
</style>
