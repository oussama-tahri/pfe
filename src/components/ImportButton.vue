<template>
  <div class="import-button">
    <input type="file" @change="handleFileChange" />
    <button @click="handleImportClick">Import</button>
  </div>
</template>

<script>
export default {
  methods: {
    handleFileChange(event) {
      this.file = event.target.files[0];
    },
    handleImportClick() {
      if (!this.file) {
        alert('Please select a file to import.');
        return;
      }

      // Call the importExcel method from your apiService passing this.file
      this.$apiService.importExcel(this.file)
        .then(() => {
          // Redirect to the appropriate route after successful import
          this.$router.push('/choose');
        })
        .catch(error => {
          console.error('Error importing file:', error);
          alert('An error occurred while importing the file.');
        });
    },
  },
};
</script>

<style scoped>
.import-button {
  margin-top: 20px;
}

.input-file {
  display: none;
}

button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

button:hover {
  background-color: #0056b3;
}
</style>