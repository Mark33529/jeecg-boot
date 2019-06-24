<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="库名">
              <a-input v-model="queryParam.database" placeholder="请输入数据库名"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="表名">
              <a-input v-model="queryParam.tableName" placeholder="请输入表名"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="表类型">
              <a-select v-model="queryParam.tableType" placeholder="请选择表类型" default-value="0">
                <a-select-option value="0">全部</a-select-option>
                <a-select-option value="1">单表</a-select-option>
                <a-select-option value="2">主表</a-select-option>
                <a-select-option value="3">附表</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="!advanced && 8 || 24" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetSearchForm">重置</a-button>
              <a @click="toggleAdvanced" style="margin-left: 8px">
                {{ advanced ? '收起' : '展开' }}
                <a-icon :type="advanced ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="() => this.handleModalVisible(true)">新建</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1"><a-icon type="delete" />删除</a-menu-item>
          <!-- lock | unlock -->
          <a-menu-item key="2"><a-icon type="lock" />锁定</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <s-table
      ref="table"
      size="default"
      :columns="columns"
      :data="loadData"
      :showAlertInfo="true"
      @onSelect="onChange"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" />
        <a-dropdown>
          <a class="ant-dropdown-link">
            更多 <a-icon type="down" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item>
              <a href="javascript:;">详情</a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;">禁用</a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;">删除</a>
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </span>
    </s-table>

    <a-modal
      title="操作"
      :width="800"
      v-model="visible"
      @ok="handleOk"
    >
      <a-form :autoFormCreate="(form)=>{this.form = form}">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="规则编号"
          hasFeedback
          validateStatus="success"
        >
          <a-input placeholder="规则编号" v-model="mdl.no" id="no" />
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="服务调用次数"
          hasFeedback
          validateStatus="success"
        >
          <a-input-number :min="1" id="callNo" v-model="mdl.callNo" style="width: 100%" />
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="状态"
          hasFeedback
          validateStatus="warning"
        >
          <a-select defaultValue="1" v-model="mdl.status">
            <a-select-option value="1">Option 1</a-select-option>
            <a-select-option value="2">Option 2</a-select-option>
            <a-select-option value="3">Option 3</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="描述"
          hasFeedback
          help="请填写一段描述"
        >
          <a-textarea :rows="5" v-model="mdl.description" placeholder="..." id="description"/>
        </a-form-item>

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="更新时间"
          hasFeedback
          validateStatus="error"
        >
          <a-date-picker
            style="width: 100%"
            showTime
            format="YYYY-MM-DD HH:mm:ss"
            placeholder="Select Time"
          />
        </a-form-item>

      </a-form>
    </a-modal>

    <a-modal title="新增" :width="1200" destroyOnClose :visible="visibleCreateModal" @ok="handleCreateModalOk" @cancel="handleCreateModalCancel">
      <!---->
      <!-- <a-form style="margin-top: 8px" :autoFormCreate="(form)=>{this.createForm = form}">
        <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 15 }" label="描述" fieldDecoratorId="description" :fieldDecoratorOptions="{rules: [{ required: true, message: '请输入至少五个字符的规则描述！', min: 5 }]}">
          <a-input placeholder="请输入" />
        </a-form-item>
      </a-form> -->
      <a-form style="margin-top: 8px" :autoFormCreate="(form)=>{this.createForm = form}">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 15 }" label="库名" fieldDecoratorId="database" :fieldDecoratorOptions="{rules: [{ required: true, message: '请输入至少两个字母的库名！', min: 2 }]}">
              <a-input v-model="queryParam.database" placeholder="请输入数据库名"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 15 }" label="表名" fieldDecoratorId="tableName" :fieldDecoratorOptions="{rules: [{ required: true, message: '请输入至少五个字母的表名！', min: 5 }]}">
              <a-input v-model="queryParam.tableName" placeholder="请输入表名"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 15 }" label="表类型" >
              <a-select v-model="queryParam.tableType" placeholder="请选择表类型" default-value="1">
                <a-select-option value="1">单表</a-select-option>
                <a-select-option value="2">主表</a-select-option>
                <a-select-option value="3">附表</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 15 }" label="主键策略" >
              <a-select v-model="queryParam.uniqueKeyType" placeholder="请选择主键策略" default-value="1">
                <a-select-option value="1">UUID（36位唯一编码）</a-select-option>
                <a-select-option value="2">NATIVE（自增长方式）</a-select-option>
                <a-select-option value="3">SEQUENCE（序列方式）</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 15 }" label="是否分页" >
              <a-select v-model="queryParam.isPaging" placeholder="请选择是否分页" default-value="1">
                <a-select-option value="1">是</a-select-option>
                <a-select-option value="0">否</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-divider />
        <a-tabs>
          <a-tab-pane tab="数据库属性" key="1">
            <a-button type="primary">新增字段</a-button>
            <s-table
              ref="table"
              size="default"
              :columns="columns"
              :data="loadData"
              @onSelect="onChange"
            >
            </s-table>
          </a-tab-pane>
          
          <a-tab-pane tab="页面属性" key="2">Coming Soon～～</a-tab-pane>
          <a-tab-pane tab="校验字段" key="3">Coming Soon～～</a-tab-pane>
          <a-tab-pane tab="外键" key="4">Coming Soon～～</a-tab-pane>
          <a-tab-pane tab="索引" key="5">Coming Soon～～</a-tab-pane>
        </a-tabs>
      </a-form>
    </a-modal>

  </a-card>
</template>

<script>
  import STable from '@/components/table/'
  import ATextarea from "ant-design-vue/es/input/TextArea"
  import AInput from "ant-design-vue/es/input/Input"
  import moment from "moment"
  import axios from 'axios';
  import { getRoleList, getCgformList } from '@/api/manage'

  export default {
    name: "TableList",
    components: {
      AInput,
      ATextarea,
      STable
    },
    data () {
      return {
        visibleCreateModal:false,
        visible: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 12 },
        },
        form: null,
        mdl: {},

        // 高级搜索 展开/关闭
        advanced: true,
        // 查询参数
        queryParam: {},
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'no'
          },
          {
            title: '表类型',
            dataIndex: 'tableType'
          },
          {
            title: '表名',
            dataIndex: 'tableName',
            sorter: true,
            needTotal: true
          },
          {
            title: '表描述',
            dataIndex: 'description',
            needTotal: true
          },
          {
            title: '版本',
            dataIndex: 'version',
            sorter: true
          },
          {
            title: '同步数据库状态',
            dataIndex: 'state',
            sorter: true
          },
          {
            title: '创建时间',
            dataIndex: 'createdAt',
            sorter: true
          },
          {
            table: '操作',
            dataIndex: 'action',
            width: '150px',
            scopedSlots: { customRender: 'action' },
          }
        ],
        // 加载数据方法 必须为 Promise 对象
        loadData: parameter => {
          return getCgformList(Object.assign(parameter, this.queryParam))
            .then(res => {
              return res.result
            })
        },

        selectedRowKeys: [],
        selectedRows: []
      }
    },
    created () {
      getRoleList({ t: new Date()})
    },
    methods: {
      handleEdit (record) {
        this.mdl = Object.assign({}, record)
        console.log(this.mdl)
        this.visible = true
      },
      handleOk () {

      },

      //添加逻辑
      handleModalVisible(isVisible) {
        this.visibleCreateModal = isVisible;
      },
      handleCreateModalOk() {
        this.createForm.validateFields((err, fieldsValue) => {
          if (err) {
            return;
          }
          const description = this.createForm.getFieldValue('description');
          axios.post('/saveRule', {
            desc: description,
          }).then((res) => {
            this.createForm.resetFields();
            this.visibleCreateModal = false;
            this.loadRuleData();
          });
        });
      },
      handleCreateModalCancel() {
        this.visibleCreateModal = false;
      },

      onChange (row) {
        this.selectedRowKeys = row.selectedRowKeys
        this.selectedRows = row.selectedRows

        console.log(this.$refs.table)
      },
      toggleAdvanced () {
        this.advanced = !this.advanced
      },

      resetSearchForm () {
        this.queryParam = {
          date: moment(new Date())
        }
      }
    },
    watch: {
      /*
      'selectedRows': function (selectedRows) {
        this.needTotalList = this.needTotalList.map(item => {
          return {
            ...item,
            total: selectedRows.reduce( (sum, val) => {
              return sum + val[item.dataIndex]
            }, 0)
          }
        })
      }
      */
    }
  }
</script>