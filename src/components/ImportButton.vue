<template>
    <div>
      <input
        ref="fileInput"
        type="file"
        style="display: none"
        @change="handleFileChange"
      />
      <button @click="handleImportClick">Import Excel</button>
    </div>
  </template>
  
  <script>
  export default {
    methods: {
      // Method to handle click event and trigger file input dialog
      handleImportClick() {
        this.$refs.fileInput.click();
      },
      // Method to handle file change event
      handleFileChange(event) {
        const file = event.target.files[0];
        if (file) {
          this.$emit('file-selected', file);
        }
      },
    },
  };
  </script>
  
  <style scoped>
  /* Add custom styles here if needed */
  </style>  