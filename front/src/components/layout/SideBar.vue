<template>
    <div class="sidebar">
        <el-menu :default-openeds="['admin', 'image', 'dashboard']" router>
            <el-submenu index="admin" v-if="userRoles!=null && userRoles.includes('admin')">
                <template slot="title"><i class="el-icon-menu"></i>管理员</template>
                <el-menu-item index="/admin/env"><i class="el-icon-caret-right"></i>环境列表</el-menu-item>
                <el-menu-item index="/admin/org"><i class="el-icon-caret-right"></i>组织列表</el-menu-item>
                <el-menu-item index="/admin/user"><i class="el-icon-caret-right"></i>用户列表</el-menu-item>
            </el-submenu>
            <el-submenu index="image">
                <template slot="title"><i class="el-icon-menu"></i>镜像管理</template>
                <el-menu-item index="/image/repo"><i class="el-icon-caret-right"></i>仓库列表</el-menu-item>
                <el-menu-item index="/image/list"><i class="el-icon-caret-right"></i>镜像列表</el-menu-item>
            </el-submenu>
            <el-submenu index="dashboard">
                <template slot="title"><i class="el-icon-menu"></i>数据看板</template>
                <el-menu-item index="/dashboard/log"><i class="el-icon-caret-right"></i>操作日志</el-menu-item>
                <!--<el-menu-item index="/dashboard/statistics"><i class="el-icon-caret-right"></i>数据统计</el-menu-item>-->
            </el-submenu>
        </el-menu>
    </div>
</template>
<script>
    import {mapGetters, mapActions} from 'vuex'
    export default {
        computed: {
            ...mapGetters({
                userRoles: 'getUserRoles'
            })
        }
    }
</script>
<style>
    .sidebar {
        display: block;
        position: absolute;
        width: 250px;
        left: 0;
        top: 70px;
        bottom: 0;
        background: #2E363F;
    }

    .sidebar > ul {
        height: 100%;
    }

    .sidebar i {
        margin-top: -3px;
    }
</style>
