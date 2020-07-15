<template>
    <div class="content-panel">
        <el-row class="query-form">
            <el-select class="query-box" v-model="queryForm.orgId" placeholder="选择组织" @change="findReposByOrg" v-if="userRoles!=null && userRoles.includes('admin')">
                <el-option v-for="org in orgs" :key="org.id" :label="org.name"
                           :value="org.id"></el-option>
            </el-select>

            <el-select class="query-box" v-model="queryForm.repoId" placeholder="选择仓库" filterable>
                <el-option v-for="repo in queryRepos" :key="repo.id" :label="repo.name"
                           :value="repo.id"></el-option>
            </el-select>

            <el-button @click="resetQueryForm">重置</el-button>
            <el-button type="primary" @click="refreshImages">查询</el-button>
        </el-row>

        <el-table :data="images" style="width: 100%" border fit>
            <el-table-column label="ID" prop="id" align="center" width="80"></el-table-column>
            <el-table-column label="镜像名" align="center">
                <template slot-scope="props">
                    {{props.row.repoName + ':' + props.row.tag}}
                </template>
            </el-table-column>
            <el-table-column label="所属组织" prop="orgName" align="center"></el-table-column>
            <el-table-column label="所在仓库" prop="repoName" align="center"></el-table-column>
            <el-table-column label="版本" prop="tag" align="center"></el-table-column>
            <el-table-column label="创建时间" prop="insertTime" align="center"
                             :formatter="dateFormatter"></el-table-column>
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
                    orgId: '',
                    orgName: '',
                    repoId: ''
                }
            }
        },
        created() {
            if (this.userRoles!=null && this.userRoles.includes('admin')) {
                this.$store.dispatch('fetchAllOrganizations');
                this.$store.dispatch('fetchReposByOrg', this.queryForm.repoId);
            } else {
                if(this.isLogin){
                    this.queryForm.orgName = this.userOrg;
                    this.$store.dispatch('fetchReposByOrgName', this.userOrg);
                }
            }
            this.refreshImages();
        },
        computed: {
            ...mapGetters({
                isLogin: 'getLoginState',
                userRoles: 'getUserRoles',
                userOrg: 'getUserOrg',
                images: 'getImages',
                orgs: 'getOrganizations',
                repos: 'getRepositories',
                queryRepos: 'getQueryRepositories',
                total: 'getImageCount'
            })
        },
        methods: {
            dateFormatter(row, column, cellValue) {
                return dateUtil.formatDate(cellValue);
            },
            handleDelete(imageId) {
                let queryParam = {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    query: this.queryForm
                };
                let data = {
                    imageId: imageId,
                    queryParam: queryParam
                };
                this.$confirm('确认删除？').then(() => {
                    this.$store.dispatch('deleteImageById', data);
                }).catch(() => {
                });
            },
            handleSizeChange(data) {
                this.pageSize = data;
                this.refreshImages();
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.refreshImages();
            },
            refreshImages() {
                this.$store.dispatch('fetchImagesByPage', {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    query: this.queryForm
                })
            },
            resetQueryForm() {
                this.queryForm.orgId = '';
                this.queryForm.repoId = '';
                this.currentPage = 1;
                this.refreshImages();
                if (this.userRoles!=null && this.userRoles.includes('admin')) {
                    this.$store.dispatch('fetchReposByOrg', this.queryForm.orgId);
                }else {
                    this.$store.dispatch('fetchReposByOrgName', this.userOrg);
                }
            },
            findReposByOrg(data) {
                console.log(data)
                this.$store.dispatch('fetchReposByOrg', data);
                this.queryForm.repoId = '';
            }
        }
    }

</script>

<style>
    .query-form {
        margin-bottom: 10px;
    }
</style>