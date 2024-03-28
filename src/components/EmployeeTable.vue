<template>
  <div class="employee-table">
    <h2>Employee Table</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Resource Name</th>
          <th>Site</th>
          <th>Tribe</th>
          <th>Squad</th>
          <th>Commentaire</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="employee in employees" :key="employee.id">
          <td>{{ employee.id }}</td>
          <td>{{ employee.resourceName }}</td>
          <td>{{ employee.site }}</td>
          <td>{{ employee.tribe }}</td>
          <td>{{ employee.squad }}</td>
          <td>{{ employee.commentaire }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      employees: [],
    };
  },
  mounted() {
    this.fetchEmployees();
  },
  methods: {
    fetchEmployees() {
      this.$apiService.getAllEmployees()
        .then(employees => {
          this.employees = employees;
        })
        .catch(error => {
          console.error('Error fetching employees:', error);
          alert('An error occurred while fetching employees.');
        });
    },
  },
};
</script>

<style scoped>
.employee-table {
  margin-top: 20px;
}

h2 {
  font-size: 20px;
  margin-bottom: 10px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 10px;
  border: 1px solid #ccc;
}

th {
  background-color: #f2f2f2;
}
</style>