import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listStudent(query) {
  return request({
    url: '/system/student/list',
    method: 'get',
    params: query
  })
}

// 查询【根据id查询学生信息】详细
export function getStudent(id) {
  return request({
    url: '/system/student/' + id,
    method: 'get'
  })
}

// 新增【新增学生信息】
export function addStudent(data) {
  return request({
    url: '/system/student',
    method: 'post',
    data: data
  })
}

// 修改【修改学生信息】
export function updateStudent(data) {
  return request({
    url: '/system/student',
    method: 'put',
    data: data
  })
}

// 删除【根据id删除学生信息】
export function delStudent(id) {
  return request({
    url: '/system/student/' + id,
    method: 'delete'
  })
}

// 导出【封装查询对象查询学生信息】
export function exportStudent(query) {
  return request({
    url: '/system/student/export',
    method: 'get',
    params: query
  })
}

// 导出 【置顶学生数据】
export function toTop(id, sort) {
  return request({
    url: '/system/student/top/' + id + '/' + sort,
    method: 'get',
    params: {
      id,
      sort
    }
  })
}

export function uploadStuHeadPic(data) {
  return request({
    url: '/system/student/uploadHeadPic',
    method: 'post',
    data: data
  })
}
