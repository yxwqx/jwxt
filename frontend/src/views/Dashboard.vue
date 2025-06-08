<template>
  <div class="dashboard">
    <aside class="sidebar">
      <h2 class="logo">内大智慧教务系统</h2>
      <nav class="nav">
        <ul>
          <li :class="{ active: page === '' }" @click="navigate('')">首页</li>
          <li :class="{ active: page === 'admin' }" @click="navigate('admin')">管理员信息</li>
          <li :class="{ active: page === 'students' }" @click="navigate('students')">学生管理</li>
          <li :class="{ active: page === 'teachers' }" @click="navigate('teachers')">教师管理</li>
          <li :class="{ active: page === 'courses' }" @click="navigate('courses')">课程管理</li>
          <li :class="{ active: page === 'classes' }" @click="navigate('classes')">班级管理</li>
          <li :class="{ active: page === 'schedule' }" @click="navigate('schedule')">课表查看</li>
          <li :class="{ active: page === 'dormitories' }" @click="navigate('dormitories')">宿舍管理</li>
          <li :class="{ active: page === 'teacherDormitories' }" @click="navigate('teacherDormitories')">教师宿舍管理</li>
        </ul>
      </nav>
    </aside>

    <main class="content">
      <div v-if="page === ''">
        <h2>欢迎使用智慧教务系统</h2>
        <div v-if="isEditMode" class="announcement-board">
          <h3>通知公告板</h3>
          <textarea v-model="announcementContent" rows="8" cols="80" class="announcement-textarea"></textarea>
          <button @click="saveAnnouncement" class="save-btn">保存</button>
        </div>
        <div v-else class="announcement-board">
          <h3>通知公告板</h3>
          <p>{{ announcementContent }}</p>
          <button @click="editAnnouncement" class="edit-btn">编辑公告</button>
        </div>
      </div>
      <div v-else-if="page === 'dormitories'">
        <h2>宿舍管理</h2>
        <div class="search-container">
          <input v-model="searchQuery" class="search-input" placeholder="搜索宿舍" />
          <button @click="searchDormitoryData" class="search-btn">搜索</button>
          <button @click="openDormitoryAddModal" class="add-btn">添加宿舍</button>
        </div>
        <table class="data-table">
          <thead>
          <tr>
            <th>ID</th>
            <th>学生姓名</th>
            <th>宿舍编号</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="dorm in dormitoryFilteredData" :key="dorm.id">
            <td>{{ dorm.id }}</td>
            <td>{{ dorm.studentName }}</td>
            <td>{{ dorm.dormId }}</td>
            <td>
              <button @click="editDormitoryItem(dorm)" class="edit-btn">编辑</button>
              <button @click="removeDormitoryItem(dorm.id)" class="delete-btn">删除</button>
            </td>
          </tr>
          </tbody>
        </table>
        <!-- 添加宿舍的弹出窗口 -->
        <div v-if="isDormitoryAddModalOpen" class="modal-overlay">
          <div class="modal-content">
            <h3>添加宿舍</h3>
            <form @submit.prevent="addDormitoryItem">
              <div>
                <label>学生姓名：</label>
                <input v-model="newDormitory.studentName" type="text" required />
              </div>
              <div>
                <label>宿舍编号：</label>
                <input v-model="newDormitory.dormId" type="text" required />
              </div>
              <button type="submit" class="save-btn">添加</button>
              <button @click="closeDormitoryAddModal" class="cancel-btn">取消</button>
            </form>
          </div>
        </div>
        <!-- 编辑宿舍的弹出窗口 -->
        <div v-if="isDormitoryEditModalOpen" class="modal-overlay">
          <div class="modal-content">
            <h3>编辑宿舍</h3>
            <form @submit.prevent="updateDormitoryItem">
              <div>
                <label>学生姓名：</label>
                <input v-model="currentDormitory.studentName" type="text" required />
              </div>
              <div>
                <label>宿舍编号：</label>
                <input v-model="currentDormitory.dormId" type="text" required />
              </div>
              <button type="submit" class="save-btn">保存</button>
              <button @click="closeDormitoryEditModal" class="cancel-btn">取消</button>
            </form>
          </div>
        </div>
      </div>
      <div v-else-if="page === 'teacherDormitories'">
        <h2>教师宿舍管理</h2>
        <div class="search-container">
          <input v-model="searchQuery" class="search-input" placeholder="搜索教师宿舍" />
          <button @click="searchTeacherDormitoryData" class="search-btn">搜索</button>
          <button @click="openTeacherDormitoryAddModal" class="add-btn">添加教师宿舍</button>
        </div>
        <table class="data-table">
          <thead>
          <tr>
            <th>ID</th>
            <th>教师姓名</th>
            <th>宿舍编号</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="dorm in teacherDormitoryFilteredData" :key="dorm.id">
            <td>{{ dorm.id }}</td>
            <td>{{ dorm.teacherName }}</td>
            <td>{{ dorm.dormId }}</td>
            <td>
              <button @click="editTeacherDormitoryItem(dorm)" class="edit-btn">编辑</button>
              <button @click="removeTeacherDormitoryItem(dorm.id)" class="delete-btn">删除</button>
            </td>
          </tr>
          </tbody>
        </table>
        <!-- 添加教师宿舍的弹出窗口 -->
        <div v-if="isTeacherDormitoryAddModalOpen" class="modal-overlay">
          <div class="modal-content">
            <h3>添加教师宿舍</h3>
            <form @submit.prevent="addTeacherDormitoryItem">
              <div>
                <label>教师姓名：</label>
                <input v-model="newTeacherDormitory.teacherName" type="text" required />
              </div>
              <div>
                <label>宿舍编号：</label>
                <input v-model="newTeacherDormitory.dormId" type="text" required />
              </div>
              <button type="submit" class="save-btn">添加</button>
              <button @click="closeTeacherDormitoryAddModal" class="cancel-btn">取消</button>
            </form>
          </div>
        </div>
        <!-- 编辑教师宿舍的弹出窗口 -->
        <div v-if="isTeacherDormitoryEditModalOpen" class="modal-overlay">
          <div class="modal-content">
            <h3>编辑教师宿舍</h3>
            <form @submit.prevent="updateTeacherDormitoryItem">
              <div>
                <label>教师姓名：</label>
                <input v-model="currentTeacherDormitory.teacherName" type="text" required />
              </div>
              <div>
                <label>宿舍编号：</label>
                <input v-model="currentTeacherDormitory.dormId" type="text" required />
              </div>
              <button type="submit" class="save-btn">保存</button>
              <button @click="closeTeacherDormitoryEditModal" class="cancel-btn">取消</button>
            </form>
          </div>
        </div>
      </div>
      <component v-else :is="componentName" v-bind="componentProps" :page="page" />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import EntityManager from './EntityManager.vue';
import AdminProfile from './AdminProfile.vue';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const searchQuery = ref(''); // 定义搜索框的值
const dataList = ref([]); // 存储所有数据
const filteredData = ref([]); // 存储过滤后的数据
const page = computed(() => route.params.page || '');
const announcementContent = ref('');
const isEditMode = ref(false);

// 监听点击编辑公告
const editAnnouncement = () => {
  isEditMode.value = true;
};

// 获取公告内容
const fetchAnnouncement = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/announcement');
    announcementContent.value = response.data.content;
  } catch (error) {
    console.error('获取公告失败', error);
  }
};

// 保存公告
const saveAnnouncement = async () => {
  try {
    await axios.put('http://localhost:8080/api/announcement', { content: announcementContent.value });
    isEditMode.value = false;
    await fetchAnnouncement();
  } catch (error) {
    console.error('保存公告失败', error);
  }
};

// 加载数据
const loadData = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/student');
    dataList.value = response.data; // 存储从服务器获取的学生数据
    filteredData.value = dataList.value; // 初始时显示所有数据
  } catch (error) {
    console.error('加载数据失败', error);
  }
};

// 搜索功能
const searchData = () => {
  if (!searchQuery.value) {
    filteredData.value = dataList.value;  // 如果搜索框为空，显示所有数据
  } else {
    filteredData.value = dataList.value.filter(item =>
        item.name.includes(searchQuery.value) || item.id.toString().includes(searchQuery.value)
    );
  }
};

const openAddModal = () => {
  isAddModalOpen.value = true;
};

const closeAddModal = () => {
  isAddModalOpen.value = false;
  resetNewForm();
};

const resetNewForm = () => {
  newStudent.value = {
    name: '',
    gender: '',
    age: null,
    className: ''
  };
};

const addItem = async () => {
  await axios.post('http://localhost:8080/api/student', newStudent.value);
  loadData();
  closeAddModal();
};

const editItem = (item) => {
  console.log('编辑项:', item);
};

const removeItem = async (id) => {
  await axios.delete(`http://localhost:8080/api/student/${id}`);
  loadData();
};

onMounted(fetchAnnouncement);
onMounted(loadData);

// 宿舍管理逻辑
const dormitoryDataList = ref([]);
const dormitoryFilteredData = ref([]);
const isDormitoryAddModalOpen = ref(false);
const isDormitoryEditModalOpen = ref(false);
const newDormitory = ref({
  studentName: '',
  dormId: ''
});
const currentDormitory = ref({ studentName: '', dormId: '' });

const loadDormitoryData = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/dormitory');
    dormitoryDataList.value = response.data;
    dormitoryFilteredData.value = dormitoryDataList.value;
  } catch (error) {
    console.error('加载宿舍数据失败', error);
  }
};

const searchDormitoryData = () => {
  if (!searchQuery.value) {
    dormitoryFilteredData.value = dormitoryDataList.value;
  } else {
    dormitoryFilteredData.value = dormitoryDataList.value.filter(item =>
        item.studentName.includes(searchQuery.value) || item.dormId.includes(searchQuery.value)
    );
  }
};

const addDormitoryItem = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/dormitory', newDormitory.value);
    loadDormitoryData();
    closeDormitoryAddModal();
    newDormitory.value = { studentName: '', dormId: '' }; // 重置表单
  } catch (error) {
    console.error('添加宿舍失败', error);
  }
};

const editDormitoryItem = (dorm) => {
  currentDormitory.value = { ...dorm };
  isDormitoryEditModalOpen.value = true;
};

const updateDormitoryItem = async () => {
  try {
    await axios.put(`http://localhost:8080/api/dormitory/${currentDormitory.value.id}`, currentDormitory.value);
    loadDormitoryData();
    closeDormitoryEditModal();
  } catch (error) {
    console.error('更新宿舍失败', error);
  }
};

const removeDormitoryItem = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/api/dormitory/${id}`);
    loadDormitoryData();
  } catch (error) {
    console.error('删除宿舍失败', error);
  }
};

const openDormitoryAddModal = () => {
  newDormitory.value = { studentName: '', dormId: '' };
  isDormitoryAddModalOpen.value = true;
};

const closeDormitoryAddModal = () => {
  isDormitoryAddModalOpen.value = false;
};

const openDormitoryEditModal = () => {
  isDormitoryEditModalOpen.value = true;
};

const closeDormitoryEditModal = () => {
  isDormitoryEditModalOpen.value = false;
};

// 教师宿舍管理逻辑
const teacherDormitoryDataList = ref([]);
const teacherDormitoryFilteredData = ref([]);
const isTeacherDormitoryAddModalOpen = ref(false);
const isTeacherDormitoryEditModalOpen = ref(false);
const newTeacherDormitory = ref({
  teacherName: '',
  dormId: ''
});
const currentTeacherDormitory = ref({ teacherName: '', dormId: '' });

const loadTeacherDormitoryData = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/teacherDormitory');
    teacherDormitoryDataList.value = response.data;
    teacherDormitoryFilteredData.value = teacherDormitoryDataList.value;
  } catch (error) {
    console.error('加载教师宿舍数据失败', error);
  }
};

const searchTeacherDormitoryData = () => {
  if (!searchQuery.value) {
    teacherDormitoryFilteredData.value = teacherDormitoryDataList.value;
  } else {
    teacherDormitoryFilteredData.value = teacherDormitoryDataList.value.filter(item =>
        item.teacherName.includes(searchQuery.value) || item.dormId.includes(searchQuery.value)
    );
  }
};

const addTeacherDormitoryItem = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/teacherDormitory', newTeacherDormitory.value);
    loadTeacherDormitoryData();
    closeTeacherDormitoryAddModal();
    newTeacherDormitory.value = { teacherName: '', dormId: '' }; // 重置表单
  } catch (error) {
    console.error('添加教师宿舍失败', error);
  }
};

const editTeacherDormitoryItem = (dorm) => {
  currentTeacherDormitory.value = { ...dorm };
  isTeacherDormitoryEditModalOpen.value = true;
};

const updateTeacherDormitoryItem = async () => {
  try {
    await axios.put(`http://localhost:8080/api/teacherDormitory/${currentTeacherDormitory.value.id}`, currentTeacherDormitory.value);
    loadTeacherDormitoryData();
    closeTeacherDormitoryEditModal();
  } catch (error) {
    console.error('更新教师宿舍失败', error);
  }
};

const removeTeacherDormitoryItem = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/api/teacherDormitory/${id}`);
    loadTeacherDormitoryData();
  } catch (error) {
    console.error('删除教师宿舍失败', error);
  }
};

const openTeacherDormitoryAddModal = () => {
  newTeacherDormitory.value = { teacherName: '', dormId: '' };
  isTeacherDormitoryAddModalOpen.value = true;
};

const closeTeacherDormitoryAddModal = () => {
  isTeacherDormitoryAddModalOpen.value = false;
};

const openTeacherDormitoryEditModal = () => {
  isTeacherDormitoryEditModalOpen.value = true;
};

const closeTeacherDormitoryEditModal = () => {
  isTeacherDormitoryEditModalOpen.value = false;
};

onMounted(loadDormitoryData);
onMounted(loadTeacherDormitoryData);

// 组件选择：根据点击的菜单项动态加载对应的组件
const componentName = computed(() => {
  switch (page.value) {
    case 'admin':
      return AdminProfile;
    case 'students':
      return EntityManager;
    case 'teachers':
      return EntityManager;
    case 'courses':
      return EntityManager;
    case 'classes':
      return EntityManager;
    case 'schedule':
      return EntityManager;
    case 'dormitories':
      return EntityManager;
    case 'teacherDormitories':
      return EntityManager;
    default:
      return null;
  }
});

const componentProps = computed(() => {
  const configs = {
    students: {
      title: '学生管理',
      api: 'http://localhost:8080/api/student',
      formFields: ['id', 'name', 'gender', 'age', 'className']
    },
    teachers: {
      title: '教师管理',
      api: 'http://localhost:8080/api/teacher',
      formFields: ['id', 'name', 'gender', 'department']
    },
    courses: {
      title: '课程管理',
      api: 'http://localhost:8080/api/course',
      formFields: ['id', 'name', 'credits', 'description']
    },
    classes: {
      title: '班级管理',
      api: 'http://localhost:8080/api/class',
      formFields: ['id', 'name', 'department']
    },
    schedule: {
      title: '课表查看',
      api: 'http://localhost:8080/api/schedule',
      formFields: ['id', 'course_id', 'teacher_id', 'time', 'location']
    },
    dormitories: {
      title: '宿舍管理',
      api: 'http://localhost:8080/api/dormitory',
      formFields: ['id', 'studentName', 'dormId']
    },
    teacherDormitories: {
      title: '教师宿舍管理',
      api: 'http://localhost:8080/api/teacherDormitory',
      formFields: ['id', 'teacherName', 'dormId']
    }
  };
  return configs[page.value] || {};
});

// 页面切换
const navigate = (path) => {
  router.push(`/dashboard/${path}`);
};

// 监听路由变化，动态更新数据
watch(page, (newPage) => {
  if (newPage) {
    if (newPage === 'dormitories') {
      loadDormitoryData();
    } else if (newPage === 'teacherDormitories') {
      loadTeacherDormitoryData();
    } else {
      loadData();
    }
  }
});
</script>

<style scoped>
/* 样式 */
.dashboard {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 220px;
  background: linear-gradient(to bottom, #2196f3, #3f51b5);
  color: #fff;
  padding: 30px 20px;
  box-shadow: 4px 0 10px rgba(0, 0, 0, 0.1);
}

.logo {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
}

.nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav li {
  padding: 12px 18px;
  margin-bottom: 10px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s;
}

.nav li:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

.nav li.active {
  background-color: #ffffff;
  color: #3f51b5;
  font-weight: bold;
}

.content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #fff;
  box-shadow: 4px 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.announcement-board {
  margin-top: 20px;
  padding: 20px;
  background-color: #f4f7fc;
  border-radius: 10px;
}

h3 {
  font-size: 24px;
  margin-bottom: 15px;
  color: #3f51b5;
}

.announcement-textarea {
  width: 100%;
  height: 200px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  resize: vertical;
}

.save-btn, .edit-btn, .add-btn, .search-btn, .delete-btn, .cancel-btn {
  padding: 10px 20px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.save-btn:hover, .add-btn:hover, .search-btn:hover, .delete-btn:hover, .cancel-btn:hover {
  background-color: #1976d2;
}

.edit-btn:hover {
  background-color: #2196f3;
}

.delete-btn {
  background-color: #f44336;
}

.delete-btn:hover {
  background-color: #d32f2f;
}

.cancel-btn {
  background-color: #9e9e9e;
}

.cancel-btn:hover {
  background-color: #757575;
}

/* 搜索框、添加按钮等 */
.search-container {
  margin-bottom: 20px;
}

.search-input {
  padding: 10px;
  width: 250px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

/* 弹出窗口样式 */
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

.modal-content form input {
  margin-bottom: 15px;
  width: 100%;
}

.modal-content button {
  margin-right: 10px;
}

/* 数据表格样式 */
.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.data-table th,
.data-table td {
  padding: 12px;
  border: 1px solid #ddd;
  text-align: left;
}

.data-table th {
  background-color: #f4f7fc;
}
</style>