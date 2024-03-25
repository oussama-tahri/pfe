<template>
    <div class="home">
      <import-button @import="handleImport" />
      <employee-table :employees="employees" @update-presence="handleUpdatePresence" />
      <squad-table :squads="squads" />
    </div>
  </template>
  
  <script>
  import apiService from '@/services/apiService';
  import ImportButton from '@/components/ImportButton.vue';
  import EmployeeTable from '@/components/EmployeeTable.vue';
  import SquadTable from '@/components/SquadTable.vue';
  
  export default {
    name: 'Home',
    components: {
      ImportButton,
      EmployeeTable,
      SquadTable,
    },
    data() {
      return {
        employees: [],
        squads: [],
      };
    },
    methods: {
      // Handle import button click
      handleImport(file) {
        apiService.importExcel(file)
          .then(() => {
            this.loadEmployeePresenceStatistics();
            this.loadSquadPresenceStatistics();
          })
          .catch(error => console.error('Error importing Excel file:', error));
      },
      // Handle presence update
      handleUpdatePresence(employeeId, date, newValue) {
        apiService.updatePresence(employeeId, date, newValue)
          .then(() => {
            this.loadEmployeePresenceStatistics();
            this.loadSquadPresenceStatistics();
          })
          .catch(error => console.error('Error updating presence:', error));
      },
      // Load employee presence statistics
      loadEmployeePresenceStatistics() {
        apiService.calculateEmployeePresenceStatistics()
          .then(data => {
            // Update employees data
            this.employees = data;
          })
          .catch(error => console.error('Error loading employee presence statistics:', error));
      },
      // Load squad presence statistics
      loadSquadPresenceStatistics() {
        apiService.calculateSquadPresenceStatistics()
          .then(data => {
            // Update squads data
            this.squads = data;
          })
          .catch(error => console.error('Error loading squad presence statistics:', error));
      },
    },
    created() {
      // Load initial data when component is created
      this.loadEmployeePresenceStatistics();
      this.loadSquadPresenceStatistics();
    },
  };
  </script>
  
  <style scoped>
  .home {
    padding: 20px;
    background-color: #f5f5f5;
    border-radius: 10px;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  }
  
  /* Import button style */
  .import-button {
    margin-bottom: 20px;
  }
  
  /* Employee table style */
  .employee-table {
    margin-bottom: 20px;
  }
  
  /* Squad table style */
  .squad-table {
    margin-bottom: 20px;
  }
  </style>  