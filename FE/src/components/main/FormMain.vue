<template>
  <div>
    <v-form-render
      :form-json="formJson"
      :form-data="formData"
      :option-data="optionData"
      :designer-config="designerConfig"
      ref="vFormRef"
    >
    </v-form-render>
    <el-button type="primary" @click="submitForm">Submit</el-button>
  </div>
</template>

<script setup>
import { ref, reactive, inject, defineExpose, defineEmits } from "vue";
import { ElMessage } from "element-plus";
import axios from "axios";
import { useStore } from "vuex";

const store = useStore();

const formJson = reactive(store.getters.getFormJson);
const formData = reactive(store.getters.getFormData);
const optionData = reactive({});
const vFormRef = ref(null);
const selectedForm = inject("selectedForm");
const formName = inject("formName");

const emit = defineEmits(["re-init"]);

const designerConfig = {
  languageMenu: true,
  //externalLink: false,
  //formTemplates: false,
  //eventCollapse: false,
  //clearDesignerButton: false,
  //previewFormButton: false,

  //presetCssCode: '.abc { font-size: 16px; }',
};

const SERVER_URL = "/api";
const customConfig = {
  headers: {
    "Content-Type": "multipart/form-data; boundary=<calculated when request is sent>",
  },
};

const submitForm = () => {
  vFormRef.value.getFormData().then((res) => {
    var isNew = selectedForm.value == "new";
    var formInfo = {
      id: isNew ? 0 : selectedForm.value,
      newFlag: isNew,
      formName: formName.value,
      createDate: new Date().toISOString(),
      data: JSON.stringify(res),
    };

    const submitData = new FormData();
    submitData.append("form-body", JSON.stringify(formInfo));

    axios.post(SERVER_URL + "/submit", submitData, customConfig).then((res) => {
      if (res.status == 200) {
        ElMessage.success("表单已保存！");

        // 刷新表单
        emit("re-init");
      }
    });
  });
};

// eslint-disable-next-line no-unused-vars
const loadDataJson = () => {
  const dataJson = store.getters.getFormJson;
  if (typeof dataJson === "string") {
    try {
      const parsedData = JSON.parse(dataJson);
      vFormRef.value.setFormJson(parsedData);
    } catch (error) {
      console.error("Error parsing form json:", error);
    }
  } else {
    vFormRef.value.setFormJson(dataJson);
  }
};

// eslint-disable-next-line no-unused-vars
const refreshFormContent = () => {
  const formDataValue = store.getters.getFormData;
  if (typeof formDataValue === "string") {
    try {
      const parsedData = JSON.parse(formDataValue);
      vFormRef.value.setFormData(parsedData);
    } catch (error) {
      console.error("Error parsing form data:", error);
    }
  }
};
// eslint-disable-next-line no-unused-vars
const resetFormData = () => {
  vFormRef.value.resetForm();
};

defineExpose({ refreshFormContent, loadDataJson, resetFormData });
</script>
