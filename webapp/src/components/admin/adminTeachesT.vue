<template>
    <div>
        <h3 class="modal-title" id="myModalLabel2"> 增删课程 </h3> <br>
        <div class="container-fluid">
            <h5> 教师工号 ： {{ searchingT.id }} </h5> <br>
            <h5> 教师姓名 ： {{ searchingT.name }} </h5> <br>
            <table class="table table-hover col-12 col-md-10 offset-md-1 col-lg-8 offset-lg-2 col-xl-6 offset-xl-3">
                <thead>
                <tr>
                    <th> 课程id </th>
                    <th> 课程名称 </th>
                    <th> 增删 </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th> <input type="number" class="form-control"
                                placeholder="在此输入新课程编号"
                                v-model=" newCourseID "
                                @keyup=" searchC() "> </th>
                    <th id="courseName"> {{ newCourseName }} </th>
                    <th> <button class="btn btn-info" @click=" addC "> 新增 </button> </th>
                </tr>
                <tr v-for=" c in searchingT.clist ">
                    <th> {{ c.id }} </th>
                    <th> {{ c.name }} </th>
                    <th> <button class="btn btn-info" @click=" deleteC( c.id ) "> 删除 </button> </th>
                </tr>
                </tbody>
            </table>
        </div> <br> <br>
    </div>

</template>

<script>
    import {info, cError, cSuccess} from "../../myToastr.js";

    export default {
        name: "adminTeachesT",
        data() {
            return {
                searchingT: { id:'', name:'', clist:[] },   // 增删课程的教师
                newCourseID: undefined, // 新增授课ID
                newCourseName: ''   // 新增授课名，需查询
            }
        },
        mounted() {
            let tid = sessionStorage.getItem('searchingTID')
            let tname = sessionStorage.getItem('searchingTNAME')
            // console.log('searchingT.id ', tid)
            // console.log('searchingT.name ', tname)
            if (tid !== undefined) {
                this.searchingT.id = tid
                // this.$set(this.searchingT, "id", tid)
                sessionStorage.removeItem('searchingTID')
                this.searchingT.name = tname
                // this.$set(this.searchingT, "name", tname)
                sessionStorage.removeItem('searchingTNAME')
                this.getByUid(tid)
                return
            }
        },
        methods: {
            getByUid(uid) { // 从教师工号获得数据
                this.$axios.get(
                    'api/teaches/uid/' + uid
                ).then(response => {
                    console.log(response)
                    if (response.data.flag === 'true')
                        this.searchingT.clist = JSON.parse(response.data.clist)
                    else
                        cError(this.$toastr, '无法得到授课数据！', '错误：')
                }).catch(error => {
                    console.log('！！！请求数据失败异常：')
                    console.log(error)
                });
            },
            searchC() {  // 查课程名
                if (!this.newCourseID) {
                    this.newCourseName = '无该课程！'
                    return
                }
                this.$axios.get(
                    'api/course/' + this.newCourseID
                ).then( response => {
                    console.log(response)
                    if (response.data.flag === 'true')
                        this.newCourseName = JSON.parse(response.data.course).name
                    else
                        this.newCourseName = '无该课程！'
                }).catch( error => {
                    console.log('！！！请求数据失败异常：')
                    console.log(error)
                });
            },
            addC() {    // 增加课程
                if (!this.searchingT.id && this.searchingT.id !== 0) {
                    cError(this.$toastr, '无教师工号', '错误：')
                    return
                } else if (!this.newCourseID) {
                    info(this.$toastr, '请先输入课程编号', '提示：')
                    return
                } else if (!this.newCourseName || this.newCourseName === '无该课程！') {
                    info(this.$toastr, '请确认课程编号无误', '提示：')
                    return
                }
                this.$axios.post(
                    'api/teaches/',{
                        uid: this.searchingT.id,
                        cid: this.newCourseID
                    }).then ( response => {
                    console.log(response)
                    if (response.data.flag === 'true')
                        cSuccess(this.$toastr, '添加成功！')
                    else
                        cError(this.$toastr, '添加失败<br>可能已有该授课？')
                    this.getByUid(this.searchingT.id)
                }).catch ( error => {
                    console.log('！！！添加失败异常：')
                    console.log(error)
                });
            },
            deleteC(cid) {    // 删除课程
                if (!this.searchingT.id && this.searchingT.id !== 0) {
                    cError(this.$toastr, '无教师工号', '错误：')
                    return
                } else if (!cid && cid !== 0) {
                    cError(this.$toastr, '无课程编号', '错误：')
                    return
                }
                this.$axios.delete(
                    'api/teaches/' + this.searchingT.id + cid
                ).then ( response => {
                    console.log(response)
                    if (response.data.flag === 'true')
                        cSuccess(this.$toastr, '删除成功！')
                    else
                        cError(this.$toastr, '删除失败<br>可能无该授课？')
                    this.getByUid(this.searchingT.id)
                }).catch ( error => {
                    console.log('！！！删除失败异常：')
                    console.log(error)
                });
            },
        },
    }
</script>

<style scoped>

</style>