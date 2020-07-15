<template>
    <div class="content-panel">
        <el-button style="margin-bottom: 10px" @click="syncOrgInfo">同步组织信息</el-button>
        <el-table :data="orgs" style="width: 100%" border fit>
            <el-table-column label="ID" prop="id" align="center" width="80"></el-table-column>
            <el-table-column label="组织名称" prop="name" align="center"></el-table-column>
            <el-table-column label="组织代码" prop="shortCode" align="center"></el-table-column>
            <el-table-column label="创建时间" prop="insertTime" align="center"
                             :formatter="dateFormatter"></el-table-column>
            <!--
            <el-table-column label="操作" align="center" width="200">
                <template slot-scope="props">
                    <el-button @click="handleEdit(props.row)" size="small">编辑</el-button>
                    <el-button @click="handleDelete(props.row.id)" type="danger" size="small">删除</el-button>
                </template>
            </el-table-column>
            -->
        </el-table>

        <el-dialog title="组织信息" :visible.sync="dialogVisible">
            <el-form label-width="80px" label-position="top" :model="inEditOrg">
                <el-form-item label="组织名称" prop="name">
                    <el-input v-model="inEditOrg.name"></el-input>
                </el-form-item>
                <el-form-item label="组织代码" prop="shortCode">
                    <el-input v-model="inEditOrg.shortCode"></el-input>
                </el-form-item>
                <el-button type="primary" @click="onSubmit">保存</el-button>
                <el-button style="float: right" @click="onCloseDialog">取消</el-button>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>

    import {mapActions, mapGetters} from "vuex";
    import dateUtil from '../../utils/dateUtil';

    export default {
        data() {
            return {
                dialogVisible: false,
                inEditOrg: {
                    id: null,
                    name: null,
                    shortCode: null
                }
            }
        },
        created() {
            this.$store.dispatch('fetchAllOrganizations');
        },
        computed: {
            ...mapGetters({
                orgs: 'getOrganizations'
            })
        },
        methods: {
            dateFormatter(row, column, cellValue) {
                return dateUtil.formatDate(cellValue);
            },
            handleEdit(org) {
                this.dialogVisible = true;
                this.inEditOrg = {
                    id: org.id,
                    name: org.name,
                    shortCode: org.shortCode
                };
            },
            onCloseDialog() {
                this.dialogVisible = false;
            },
            onSubmit() {
                this.dialogVisible = false;
                this.$store.dispatch('updateOrganization', this.inEditOrg);
            },
            handleDelete(orgId) {
                this.$confirm('确认删除？').then(() => {
                    this.$store.dispatch('deleteOrganizationById', orgId);
                }).catch(() => {
                });
            },
            syncOrgInfo() {
                this.$store.dispatch('syncOrgInfo');
            }
        }
    }

</script>

<style>

</style>