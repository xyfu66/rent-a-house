<template>
  <div id="form-body">
    <HeaderBar
      @refresh-form-content="refreshFormContent"
      @reset-form="resetForm"
      @load-data-json="loadDataJson"
      ref="headerBar"
    />
    <FormMain ref="formMain" @re-init="reInit" />
  </div>
</template>

<script setup>
import HeaderBar from "./nav/HeaderBar.vue";
import FormMain from "./main/FormMain.vue";
import { provide, ref } from "vue";

const selectedForm = ref("new");
const formName = ref("");

provide("formName", formName);
provide("selectedForm", selectedForm);

const formMain = ref(null);
const headerBar = ref(null);

const refreshFormContent = () => {
  formMain.value.refreshFormContent();
};
const resetForm = () => {
  formMain.value.resetFormData();
};
const loadDataJson = () => {
  formMain.value.loadDataJson();
};
const reInit = () => {
  headerBar.value.init();
};
</script>
<style scoped lang="less"></style>
