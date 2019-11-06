<template>
  <div>
    <div style="height:111px;background: #96B97D;border:1px #96B97D solid;">
      <h1 style="line-height: 70px;margin-left: 30px;text-shadow: 0 0 2px black;color: white;">OnLine CMM IDE</h1>
    </div>
    <!-- <el-header style="height: 95px;">ZFQ'IDE</el-header> -->
    <el-container>
      <el-card class="box-card">
        <!--border:1px black solid;el-icon-thumb -->
        <div style="height:50px;">
          <el-upload
            style="display: inline"
            :show-file-list="false"
            :on-success="onSuccess"
            :on-error="onError"
            :before-upload="beforeUpload"
            action="http://localhost:8089/api/import"
            accept=".txt,.cmm">
            <el-button >点击上传</el-button>
          </el-upload>
          <el-button style="float:left">源代码:</el-button>
          <el-button type="success" style="float:right" icon="el-icon-thumb" @click = "runCode">点击运行</el-button>
          <el-button style="float:right" @click = "nextStep">下一步</el-button>
          <el-button style="float:right" @click = "_continue">继续</el-button>
          <el-button style="float:right" @click = "debug">调试</el-button>

        </div>

        <Editor ref = "code"/>
      </el-card>
      <el-card class="box-card">
        <div style="height:50px;">
          <el-button style="float:left;margin-right: -10px;" @click = "clickButton1" v-if="whichButton===0" type="primary"> cmd:</el-button>
          <el-button style="float:left;margin-right: -10px;" @click = "clickButton1" v-else>cmd:</el-button>
          <el-button style="float:left;margin-right: -10px;" @click = "clickButton2" v-if="whichButton===1" type="primary"> input:</el-button>
          <el-button style="float:left;margin-right: -10px;" @click = "clickButton2" v-else> input:</el-button>
          <el-button style="float:left" @click = "clickButton3" v-if="whichButton===2" type="primary">output:</el-button>
          <el-button style="float:left;margin-right: -10px;" @click = "clickButton3" v-else >output:</el-button>
        </div>
        <Editor ref = "output"/>
      </el-card>
    </el-container>
  </div>
</template>

<script>
import Editor from '@/components/Editor.vue'
export default {
  name: 'Home',
  components: { Editor },
  data () {
    return {
      whichButton: 0,
      cmdStr: '',
      inputStr: '',
      outputStr: ''
    }
  },
  methods: {
    changeInput (code) {
      this.code = code
    },
    runCode () {
      if (this.whichButton === 0) {
        this.cmdStr = this.$refs.output.data
      } else if (this.whichButton === 1) {
        this.inputStr = this.$refs.output.data
      } else {
        this.outputStr = this.$refs.output.data
      }
      this.$axios
        .post('/code', {
          code: this.$refs.code.data,
          input: this.inputStr,
          cmd: this.cmdStr
        })
        .then(successResponse => {
          console.log(successResponse)
          if (successResponse.data.status === 200) {
            console.log('运行成功')
            console.log(successResponse.data.output)
            this.whichButton = 2
            this.outputStr = successResponse.data.output
            this.$refs.output.data = successResponse.data.output
          } else {
            console.log('连接失败')
          }
        })
        .catch(failResponse => {
        })
    },
    nextStep () {
      this.$axios
        .post('/nextStep', {})
        .then(successResponse => {
          console.log(successResponse)
          if (successResponse.data.status === 200) {
            console.log('下一步')
          }
        })
        .catch(failResponse => {
        })
    },
    _continue () {
      this.$axios
        .post('/_continue', {})
        .then(successResponse => {
          console.log(successResponse)
          if (successResponse.data.status === 200) {
            console.log('继续')
          }
        })
        .catch(failResponse => {
        })
    },
    debug () {
      this.$axios
        .post('/debug', {
          code: this.$refs.code.data,
          cmd: this.cmdStr,
          input: this.inputStr
          })
        .then(successResponse => {
          console.log(successResponse)
          if (successResponse.data.status === 200) {
            console.log('继续')
          }
        })
        .catch(failResponse => {
        })
    },
    clickButton1 () {
      if (this.whichButton === 1) {
        this.inputStr = this.$refs.output.data
      } else if (this.whichButton === 2) {
        this.outputStr = this.$refs.output.data
      }
      this.whichButton = 0
      this.$refs.output.data = this.cmdStr
    },
    clickButton2 () {
      if (this.whichButton === 0) {
        this.cmdStr = this.$refs.output.data
      } else if (this.whichButton === 2) {
        this.outputStr = this.$refs.output.data
      }
      this.whichButton = 1
      this.$refs.output.data = this.inputStr
    },
    clickButton3 () {
      if (this.whichButton === 1) {
        this.inputStr = this.$refs.output.data
      } else if (this.whichButton === 0) {
        this.cmdStr = this.$refs.output.data
      }
      this.whichButton = 2
      this.$refs.output.data = this.outputStr
    },
    onSuccess (response, file, fileList) {
      console.log('上传成功')
      console.log(response)
      this.$refs.code.data = response.code
    },
    onError (erorr, file, fileList) {
      console.log('上传失败')
    },
    beforeUpload (file) {
      /* this.enabledUploadBtn = false
      this.uploadBtnIcon = 'el-icon-loading'
      this.btnText = '正在导入' */
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 200px;
}
.el-header{
  background-color: darkseagreen;
  text-align: left;
  margin: 0;
  font-size: 20px;
}
.box-card {
  width: 700px;
  height: 580px;
  margin: 35px;
}
.el-upload{
  float:left
}
</style>
