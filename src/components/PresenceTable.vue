<template>
  <div class="presence-table">
    <h2>Presence Table</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Date</th>
          <th>Present</th>
          <th>Employee ID</th>
          <th>Employee Name</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="presence in presences" :key="presence.id">
          <td>{{ presence.id }}</td>
          <td>{{ presence.date }}</td>
          <td>{{ presence.present }}</td>
          <td>{{ presence.employee.id }}</td>
          <td>{{ presence.employeeName }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      presences: [],
    };
  },
  mounted() {
    this.fetchPresences();
  },
  methods: {
    fetchPresences() {
      this.$apiService.getPresence()
        .then(presences => {
          this.presences = presences;
        })
        .catch(error => {
          console.error('Error fetching presences:', error);
          alert('An error occurred while fetching presences.');
        });
    },
  },
};
</script>

<style scoped>
.presence-table {
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