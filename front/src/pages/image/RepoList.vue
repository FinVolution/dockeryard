<template>
    <div class="content-panel">
        <el-row class="query-form">
            <el-select class="query-box" v-model="queryForm.orgId" placeholder="选择组织" v-if="userRoles!=null && userRoles.includes('admin')">
                <el-option v-for="org in orgs" :key="org.id" :label="org.name"
                           :value="org.id"></el-option>
            </el-select>

            <el-input class="query-box" v-model="queryForm.name" placeholder="输入仓库名"></el-input>

            <el-button @click="resetQueryForm">重置</el-button>
            <el-button type="primary" @click="refreshRepos">查询</el-button>
        </el-row>

        <el-table :data="repos" style="width: 100%" border fit>
            <el-table-column label="ID" prop="id" align="center" width="80"></el-table-column>
            <el-table-column label="仓库名" prop="name" align="center"></el-table-column>
            <el-table-column label="所属组织" prop="orgName" align="center"></el-table-column>
            <el-table-column label="创建时间" prop="insertTime" align="center" :formatter="dateFormatter"></el-table-column>
            <el-table-column label="操作" align="center" width="200" v-if="isLogin">
                <template slot-scope="props">
                    <el-button @click="handleDelete(props.row.id)" type="danger" size="small">删除</el-button>
                </template>
            </el-table-column>
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
    import dateUtil from '../../utils/dateUtil';

    export default {
        data() {
            return {
                currentPage: 1,
                pageSize: 10,
                queryForm: {
                    name: '',
                    orgId: '',
                    orgName: ''
                }
            }
        },
        created() {
            if (this.userRoles!=null && this.userRoles.includes('admin')) {
                this.$store.dispatch('fetchAllOrganizations');
            } else {
                if(this.isLogin){
                    this.queryForm.orgName = this.userOrg;
                }
            }
            this.refreshRepos();
        },
        computed: {
            ...mapGetters({
                isLogin: 'getLoginState',
                userRoles: 'getUserRoles',
                userOrg: 'getUserOrg',
                repos: 'getRepositories',
                orgs: 'getOrganizations',
                total: 'getRepoCount'
            })
        },
        methods: {
            dateFormatter(row, column, cellValue) {
                return dateUtil.formatDate(cellValue);
            },
            handleDelete(repoId){
                let queryParam = {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    query: this.queryForm
                };
                let data = {
                    repoId: repoId,
                    queryParam: queryParam
                };
                this.$confirm('确认删除？').then(() => {
                    this.$store.dispatch('deleteRepositoryById', data);
                }).catch(() => {
                });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.refreshRepos();
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.refreshRepos();
            },
            resetQueryForm() {
                this.queryForm.name = '';
                this.queryForm.orgId = '';
                this.currentPage = 1;
                this.refreshRepos();
            },
            refreshRepos() {
                this.$store.dispatch('fetchReposByPage', {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    query: this.queryForm
                })
            }
        }
    }

</script>

<style>
    .query-form {
        margin-bottom: 10px;
    }
</style>