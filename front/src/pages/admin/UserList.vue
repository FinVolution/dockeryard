<template>
    <div class="content-panel">
        <el-row class="query-form">
            <el-col :span="3">
                <el-input v-model="query.realName" placeholder="输入姓名"></el-input>
            </el-col>
            <el-col :span="3" :offset="1">
                <el-input v-model="query.name" placeholder="输入域账号"></el-input>
            </el-col>
            <el-col :span="3" :offset="1">
                <el-select v-model="query.shortCode" placeholder="选择组织">
                    <el-option v-for="org in orgs" :key="org.id" :label="org.name"
                               :value="org.shortCode"></el-option>
                </el-select>
            </el-col>
            <el-col :span="6" :offset="1">
                <el-button @click="resetQueryForm">重置</el-button>
                <el-button type="primary" @click="refreshUsers">查询</el-button>
            </el-col>
        </el-row>

        <el-table :data="users" style="width: 100%" border fit>
            <el-table-column label="ID" prop="id" align="center" width="160"></el-table-column>
            <el-table-column label="工号" prop="workNumber" align="center"></el-table-column>
            <el-table-column label="姓名" prop="realName" align="center"></el-table-column>
            <el-table-column label="域账号" prop="name" align="center"></el-table-column>
            <el-table-column label="邮箱" prop="email" align="center"></el-table-column>
            <el-table-column label="组织" prop="orgName" align="center"></el-table-column>
            <el-table-column label="角色" prop="role" align="center"></el-table-column>
        </el-table>

        <div align='center' style="margin-top: 10px">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 30, 40]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

    </div>
</template>

<script>

    import {mapActions, mapGetters} from "vuex";

    export default {
        data() {
            return {
                currentPage: 1,
                pageSize: 10,
                query: {
                    realName: '',
                    name: '',
                    shortCode:''
                },
            }
        },
        created() {
            this.$store.dispatch('fetchAllOrganizations');
            this.refreshUsers();
        },
        computed: {
            ...mapGetters({
                users: 'getUsers',
                total: 'getUserCount',
                orgs: 'getOrganizations'
            })
        },
        methods: {
            handleSizeChange(data) {
                this.pageSize = data;
                this.refreshUsers();
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.refreshUsers();
            },
            refreshUsers() {
                let data = {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    realName: this.query.realName,
                    name: this.query.name,
                    shortCode: this.query.shortCode
                };
                this.$store.dispatch('fetchUsersByPage', data);
            },
            resetQueryForm() {
                this.query.realName = '';
                this.query.name = '';
                this.currentPage = 1;
                this.query.orgId = '';
                this.refreshUsers();
            },
            syncUserList() {
                this.$store.dispatch('syncUserList');
            }
        }
    }

</script>

<style>
    .query-form {
        margin-bottom: 10px;
    }
</style>