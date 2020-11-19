<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="学生姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入学生姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生年龄" prop="age">
        <el-input
          v-model="queryParams.age"
          placeholder="请输入学生年龄"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="选择性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable size="small">
          <el-option
          v-for="sex in sexes"
          :label="sex"
          :value="sex"
          />
        </el-select>
      </el-form-item>
     <!-- <el-form-item label="${comment}" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入${comment}"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:student:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:student:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:student:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:student:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="头像" width="110px" align="center" prop="pic">
        <template slot-scope="scope">
          <div class="student-headPicture">
            <img :src="'/dev-api'+scope.row.pic" height="40px" width="40px">
          </div>
          <el-button size="mini" @click="updateHeadPic(scope.row.id)">更新头像</el-button>
        </template>
      </el-table-column>
      <el-table-column label="学生id" align="center" prop="id" />
      <el-table-column label="学生姓名" align="center" prop="name" />
      <el-table-column label="学生年龄" align="center" prop="age" />
      <el-table-column label="学生性别" align="center" prop="sex" />
      <el-table-column label="排序" align="center" prop="sort" >

      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:student:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:student:remove']"
          >删除</el-button>
          <!--新增置顶操作-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-caret-top"
            @click="handleToTop(scope.row)"
            v-hasPermi="['system:student:remove']"
          >置顶</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改【请填写功能名称】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学生姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入学生姓名" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="form.sex" placeholder="请选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <!--<el-form-item label="" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入${comment}" />
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--上传头像对话框-->
    <el-dialog :title="title" :visible.sync="upload.open" width="800px" append-to-body @opened="modalOpened">
      <el-row>
        <el-col :xs="24" :md="12" :style="{height: '350px'}">
          <vue-cropper
            ref="cropper"
            :img="upload.options.img"
            :info="true"
            :autoCrop="upload.options.autoCrop"
            :autoCropWidth="upload.options.autoCropWidth"
            :autoCropHeight="upload.options.autoCropHeight"
            :fixedBox="upload.options.fixedBox"
            @realTime="realTime"
            v-if="upload.visible"
          />
        </el-col>
        <el-col :xs="24" :md="12" :style="{height: '350px'}">
          <!--<div class="avatar-upload-preview">
            <img :src="upload.previews.url" :style="upload.previews.img" />
          </div>-->
        </el-col>
      </el-row>
      <br />
      <el-row>
        <el-col :lg="2" :md="2">
          <el-upload action="#" :http-request="requestUpload" :show-file-list="false" :before-upload="beforeUpload">
            <el-button size="small">
              上传
              <i class="el-icon-upload el-icon--right"></i>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{span: 1, offset: 2}" :md="2">
          <el-button icon="el-icon-plus" size="small" @click="changeScale(1)"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :md="2">
          <el-button icon="el-icon-minus" size="small" @click="changeScale(-1)"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :md="2">
          <el-button icon="el-icon-refresh-left" size="small" @click="rotateLeft()"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :md="2">
          <el-button icon="el-icon-refresh-right" size="small" @click="rotateRight()"></el-button>
        </el-col>
        <el-col :lg="{span: 2, offset: 6}" :md="2">
          <el-button type="primary" size="small" @click="uploadImg()">提 交</el-button>
        </el-col>
      </el-row>
    </el-dialog>

  </div>
</template>

<script>
  import store from "@/store";
  import {uploadStuHeadPic, listStudent, getStudent, delStudent, addStudent, updateStudent, exportStudent,toTop } from "@/api/system/student";
  import { getToken } from "@/utils/auth";
  import { VueCropper } from "vue-cropper";
export default {
  name: "Student",
  // 注册组件
  components: {
    VueCropper
  },
  data() {
    return {
      // 性别列表
      sexes: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 【请填写功能名称】表格数据
      studentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        age: null,
        sex: null,
        sort: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sort: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ]
      },
      // 用户导入参数
      upload: {
        //需要更改头像的学生id
        stuId: '',
        // 是否显示弹出层
        open: false,
        // 是否显示cropper
        visible: false,
        // 弹出层标题
        title: "修改头像",
        options: {
          img: store.getters.avatar, //裁剪图片的地址
          autoCrop: true, // 是否默认生成截图框
          autoCropWidth: 200, // 默认生成截图框宽度
          autoCropHeight: 200, // 默认生成截图框高度
          fixedBox: true // 固定截图框大小 不允许改变
        },
        previews: {}
      },
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_user_sex").then(response => {
      this.sexes = response.data;
    });
  },
  methods: {
    // 编辑头像
    updateHeadPic(stuId) {
      this.upload.open = true
      this.upload.stuId = stuId
      console.log(stuId);
    },
    // 打开弹出层结束时的回调
    modalOpened() {
      this.upload.visible = true;
    },
    // 覆盖默认的上传行为
    requestUpload() {
    },
    // 向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 图片缩放
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 上传预处理
    beforeUpload(file) {
      if (file.type.indexOf("image/") == -1) {
        this.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.upload.options.img = reader.result;
        };
      }
    },
    // 上传图片
    uploadImg() {
      this.$refs.cropper.getCropBlob(data => {
        let formData = new FormData();
        formData.append("headPicFile", data);
        formData.append("stuId",this.upload.stuId);
        uploadStuHeadPic(formData).then(response => {
          this.upload.open = false;
          this.upload.options.img = process.env.VUE_APP_BASE_API + response.imgUrl;
          console.log('图片地址: ' + this.upload.options.img);
          store.commit('SET_AVATAR', this.upload.options.img);
          this.msgSuccess("修改成功");
          this.upload.visible = false;
          this.getList();
        });
      });

    },
    // 实时预览
    realTime(data) {
      this.upload.previews = data;
    },
    /** 查询【学生信息】列表 */
    getList() {
      this.loading = true;
      listStudent(this.queryParams).then(response => {
        this.studentList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        age: null,
        sex: null,
        sort: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加【请填写功能名称】";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStudent(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改【请填写功能名称】";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStudent(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStudent(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除【请填写功能名称】编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delStudent(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有【请填写功能名称】数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportStudent(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    },
    /*置顶某一条学生数据*/
    handleToTop(row) {
      /**拿到需要置顶的数据的id,和排序字段的值*/
      const id = row.id
      const sort = row.sort
      toTop(id,sort).then(res => {
        this.msgSuccess("置顶成功");
        this.getList()
      }).catch(res => {
        this.msgError("置顶失败")
      })
    },
   /* /!*获取头像图片的URL地址*!/
    getUrl(url) {
      getPicUrl(url).then(res =>{
        console.log(res)
        return res
      })
    },*/
/*
    uploadUrl(id) {
      // 上传新的头像的url地址
     /!* uploadUrl(id)*!/
    },*/
    /*//上传之前的校验
    beforeAvatorUpload(file){
      const isJPGOrPng = (file.type === 'image/jpeg') || (file.type === 'image/png');
      if(!isJPGOrPng){
        this.msgError('上传的文件只能是jpg或者是png的格式')
        return false;
      }
      const isLt2M = (file.size / 1024 / 1024) < 2;
      if(!isLt2M) {
        this.msgError('上传的头像大小不能超过2M')
        return false;
      }
      return true;
    },
    // 上传头像成功之后的处理
    handleAvatorSuccess(res,file){
      /!*let _this = this;*!/
      console.log(res)
      if(res.code == 1) {
        this.getList();
        this.msgSuccess('上传成功')
      }else{
        this.msgError('上传的头像大小不能超过2M')
      }
    }*/
  }
};
</script>
