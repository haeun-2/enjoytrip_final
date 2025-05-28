<template>
  <!-- Toast UI 에디터 -->
  <div class="form-group">
    <label for="content" class="form-label">내용</label>
    <div class="editor" ref="editor"></div>
  </div>
</template>

<script setup>
import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css";
import { useTemplateRef, onMounted, defineExpose } from "vue";
import { uploadImage } from "@/apis/common/upload.js";

const editorContainer = useTemplateRef("editor");

let editorInstance;

onMounted(() => {
  editorInstance = new Editor({
    el: editorContainer.value,
    height: "800px",
    hooks: {
      addImageBlobHook: async (blob, callback) => {
        const url = await uploadImage(blob);
        callback(url, blob.name);
      },
    },
  });
});

defineExpose({
  getContent: () => editorInstance.getHTML(),
  setContent: (content) => editorInstance.setHTML(content),
});
</script>

<style scoped></style>
