<template>
    <div class="content-panel">

        <el-row class="query-form">
            <el-col :span="3">
                <el-input v-model="query.eventName" placeholder="仓库名称"></el-input>
            </el-col>
            <el-col :span="2" :offset="1">
                <el-input v-model="query.eventAddition" placeholder="版本"></el-input>
            </el-col>
            <el-col :span="2" :offset="1">
                <el-input v-model="query.optionType" placeholder="操作类型"></el-input>
            </el-col>
            <el-col :span="2" :offset="1">
                <el-input v-model="query.operator" placeholder="操作人"></el-input>
            </el-col>
            <el-col :span="4" :offset="1">
                <div class="block">
                    <el-date-picker
                            v-model="query.operateTime"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd HH:mm:ss">
                    </el-date-picker>
                </div>
            </el-col>

            <el-col :span="4" :offset="3">
                <el-button @click="resetQueryForm">重置</el-button>
                <el-button type="primary" @click="refreshLogs">查询</el-button>
            </el-col>
        </el-row>


        <el-table :data="logs" style="width: 100%" border fit>
            <el-table-column label="ID" prop="id" align="center" width="80"></el-table-column>
            <el-table-column label="仓库名称" prop="eventName" align="center"></el-table-column>
            <el-table-column label="版本" prop="eventAddition" align="center"></el-table-column>
            <el-table-column label="操作类型" prop="optionType" align="center"></el-table-column>
            <el-table-column label="操作人" prop="operator" align="center"></el-table-column>
            <el-table-column label="操作时间" prop="operateTime" align="center" :formatter="dateFormatter"></el-table-column>
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
                query: {
                    eventName: '',
                    eventAddition: '',
                    optionType: '',
                    operator: '',
                    operateTime: ''
                },
                pickerOptions2: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                value6: '',
                value7: ''
            }
        },
        created() {
            this.refreshLogs();
        },
        computed: {
            ...mapGetters({
                logs: 'getLogs',
                total: 'getLogCount'
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
            handleSizeChange(data) {
                this.pageSize = data;
                this.refreshLogs();
            },
            handleCurrentChange(data) {
                this.currentPage = data;
                this.refreshLogs();
            },
            refreshLogs() {
                let data = {
                    page: this.currentPage - 1,
                    size: this.pageSize,
                    eventName: this.query.eventName,
                    eventAddition: this.query.eventAddition,
                    optionType: this.query.optionType,
                    operator: this.query.operator,
                    operateTime: this.query.operateTime
                };
                this.$store.dispatch('fetchLogsByPage', data);
            },
            resetQueryForm() {
                this.query.eventName = '';
                this.query.eventAddition = '';
                this.query.optionType = '';
                this.query.operator = '';
                this.query.operateTime = '';
                this.currentPage = 1;
                this.refreshLogs();
            },
            onCloseDialog() {
                this.dialogVisible = false;
            }
        }
    }

</script>

<style>
    .query-form {
        margin-bottom: 10px;
    }
</style>
