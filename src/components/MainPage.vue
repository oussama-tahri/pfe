<template>
  <div>
    <div>
      <ImportButton @file-selected="handleFileSelected" />
    </div>

    <div class="sub-header" v-if="importComplete">
      <button @click="showEmployeesTable">Employees</button>
      <button @click="showPresenceTable">Presence</button>
    </div>

    <EmployeeTable v-if="importComplete && showEmployees" :employees="employees" />
    <PresenceTable v-if="importComplete && showPresence" :presences="presences" />
  </div>
</template>

<script>
import ImportButton from '@/components/ImportButton.vue';
import EmployeeTable from '@/components/EmployeeTable.vue';
import PresenceTable from '@/components/PresenceTable.vue';
import apiService from '@/services/apiService';

export default {
  name: 'MainPage',
  components: {
    ImportButton,
    EmployeeTable,
    PresenceTable,
  },
  data() {
    return {
      employees: [],
      presences: [],
      importComplete: false,
      showEmployees: false,
      showPresence: false,
    };
  },
  methods: {
    handleFileSelected(file) {
      apiService.importExcel(file)
        .then(data => {
          this.employees = data.employees;
          this.presences = data.presences;
          this.importComplete = true;
          this.showEmployees = true;
          this.showPresence = false;
        })
        .catch(error => {
          console.error('Error importing file:', error);
          alert('An error occurred while importing the file.');
        });
    },
    showEmployeesTable() {
      this.showEmployees = true;
      this.showPresence = false;
    },
    showPresenceTable() {
      this.showEmployees = false;
      this.showPresence = true;
    },
  },
};
</script>

<style scoped>
.sub-header {
  margin-top: 20px;
}
</style>