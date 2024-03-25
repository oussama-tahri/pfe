<template>
  <div id="app">
    <ImportButton @file-selected="handleFileSelected" />
    <EmployeeTable :employees="employees" />
    <SquadTable :squads="squads" />
  </div>
</template>

<script>
import apiService from './services/apiService'; // Import the apiService
import ImportButton from './components/ImportButton.vue'; // Import the ImportButton component
import EmployeeTable from './components/EmployeeTable.vue'; // Import the EmployeeTable component
import SquadTable from './components/SquadTable.vue'; // Import the SquadTable component

export default {
  name: 'App',
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
    // Method to handle file selection
    async handleFileSelected(file) {
      try {
        // Import Excel file using the apiService
        const importedData = await apiService.importExcel(file);
        console.log(importedData); // Handle imported data as needed
        // Update employees and squads data based on the imported data
        this.employees = importedData.employees;
        this.squads = importedData.squads;
      } catch (error) {
        console.error('Error importing Excel file:', error);
      }
    },
  },
};
</script>

<style>
/* Add custom styles here if needed */
</style>
