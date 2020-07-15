<template>
    <div class="content-panel">
        <el-button style="margin-bottom: 10px" @click="syncEnvList">同步环境列表</el-button>
        <el-table :data="environments" style="width: 100%" border fit>
            <el-table-column label="ID" prop="id" align="center" width="80"></el-table-column>
            <el-table-column label="Name" prop="name" align="center"></el-table-column>
            <el-table-column label="Url" prop="url" align="center"></el-table-column>
            <el-table-column label="注册时间" prop="insertTime" align="center"
                             :formatter="dateFormatter"></el-table-column>
            <el-table-column label="操作" align="center" width="200">
                <template slot-scope="props">
                    <el-button @click="handleEdit(props.row)" size="small">编辑</el-button>
                    <!--
                    <el-button @click="handleDelete(props.row.id)" type="danger" size="small">删除</el-button>
                    -->
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="环境信息" :visible.sync="dialogVisible">
            <el-form label-width="80px" label-position="top" :model="inEditEnvironment">
                <el-form-item label="环境名" prop="name">
                    <el-input v-model="inEditEnvironment.name" disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="环境地址" prop="url">
                    <el-input v-model="inEditEnvironment.url"></el-input>
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
                inEditEnvironment: {
                    id: null,
                    name: null,
                    url: null
                }
            }
        },
        created() {
            this.$store.dispatch('fetchAllEnvironments');
        },
        computed: {
            ...mapGetters({
                environments: 'getEnvironments'
            })
        },
        methods: {
            dateFormatter(row, column, cellValue) {
                return dateUtil.formatDate(cellValue);
            },
            handleEdit(env) {
                this.dialogVisible = true;
                this.inEditEnvironment = {
                    id: env.id,
                    name: env.name,
                    url: env.url
                };
            },
            onCloseDialog() {
                this.dialogVisible = false;
            },
            onSubmit() {
                this.dialogVisible = false;
                this.$store.dispatch('updateEnvironment', this.inEditEnvironment);
            },
            handleDelete(envId) {
                this.$confirm('确认删除？').then(() => {
                    this.$store.dispatch('deleteEnvironmentById', envId);
                }).catch(() => {
                });
            },
            syncEnvList() {
                this.$store.dispatch('syncEnvList');
            }
        }
    }

</script>

<style>

</style>