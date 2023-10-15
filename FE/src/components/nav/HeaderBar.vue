<template>
  <div id="header-bar">
    <el-row class="block-col-1"><el-col>租房决策树</el-col></el-row>
    <el-divider />
    <el-row class="block-col-2">
      <el-col :span="8">
        <el-select
          v-model="selectedForm"
          placeholder="请选择表单"
          @change="handleFormChange"
        >
          <el-option
            v-for="form in historyForms"
            :key="form.id"
            :label="form.name"
            :value="form.id"
          ></el-option>
        </el-select>
      </el-col>
      <el-col :span="10">
        <el-input v-model="formName" placeholder="请输入表单名称"></el-input>
      </el-col>
      <el-col :span="6">
        <el-select
          v-model="selectedVersion"
          placeholder="请选择版本"
          @change="handleVersionChange"
        >
          <el-option
            v-for="version in historyVersions"
            :key="version.version"
            :label="version.name"
            :value="version.version"
          ></el-option>
        </el-select>
      </el-col>
    </el-row>
    <el-divider />
  </div>
</template>

<script setup>
import {
  ref,
  computed,
  inject,
  defineEmits,
  defineExpose,
  onMounted,
  nextTick,
} from "vue";
import { useStore } from "vuex";

const store = useStore();

const historyForms = computed(() => store.getters.getHistoryForms);
const historyVersions = computed(() => store.getters.getHistoryVersions);

const selectedForm = inject("selectedForm");
const formName = inject("formName");
const selectedVersion = ref(0);

const emit = defineEmits(["reset-form", "refresh-form-content", "load-data-json"]);

// 切换表单
const handleFormChange = (value) => {
  if (value === "new") {
    formName.value = "";
    store.dispatch("resetFormData").then(() => {
      emit("reset-form");
      selectedVersion.value = historyVersions.value[0].version;
    });
  } else {
    let form = historyForms.value.find((form) => form.id === value);
    formName.value = form.name;
    store.dispatch("fetchHistoryVersions", value).then((versionList) => {
      selectedVersion.value = versionList[0].version;
      store
        .dispatch("refreshFormContent", {
          formId: value,
          versionId: versionList[0].version - 1,
        })
        .then(() => {
          emit("refresh-form-content");
        });
    });
  }
};

// 切换版本
const handleVersionChange = (value) => {
  store
    .dispatch("refreshFormContent", { formId: selectedForm.value, versionId: value - 1 })
    .then(() => {
      emit("reset-form");
      emit("refresh-form-content");
    });
};

const init = () => {
  store.dispatch("init").then((val) => {
    selectedForm.value = val;
    let form = historyForms.value.find((form) => form.id === val);
    formName.value = form.name;
    selectedVersion.value = historyVersions.value[0].version;

    emit("reset-form");
    emit("load-data-json");
    nextTick(() => {
      emit("refresh-form-content");
    });
  });
};

onMounted(() => {
  init();
});

defineExpose({ init });
</script>

<style lang="less" scoped>
.block-col-2 {
  .el-dropdown-link {
    cursor: pointer;
    color: var(--el-color-primary);
    display: flex;
    align-items: center;
  }

  .header-stration {
    display: block;
    color: var(--el-text-color-secondary);
    font-size: 14px;
    margin-bottom: 20px;
  }
}
</style>
