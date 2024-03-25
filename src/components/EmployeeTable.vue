<template>
    <div class="employee-table">
      <h2>Employee Table</h2>
      <table>
        <thead>
          <tr>
            <th>Resource Name</th>
            <th>Site</th>
            <th>Tribe</th>
            <th>Squad</th>
            <th>Commentaire</th>
            <th v-for="day in daysOfMonth" :key="day">{{ day }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(employee, index) in employees" :key="index">
            <td>{{ employee.resourceName }}</td>
            <td>{{ employee.site }}</td>
            <td>{{ employee.tribe }}</td>
            <td>{{ employee.squad }}</td>
            <td>{{ employee.commentaire }}</td>
            <td v-for="(presence, index) in employee.presences" :key="index">
              <input type="checkbox" :id="`presence-${employee.id}-${index}`"
                     :checked="presence.present" @change="updatePresence(employee.id, presence.date, $event.target.checked)">
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  export default {
    name: 'EmployeeTable',
    props: {
      employees: {
        type: Array,
        required: true,
      },
    },
    computed: {
      // Get list of unique days from employee presences
      daysOfMonth() {
        const daysSet = new Set();
        this.employees.forEach(employee => {
          employee.presences.forEach(presence => {
            daysSet.add(presence.date);
          });
        });
        return Array.from(daysSet).sort();
      },
    },
    methods: {
      // Update presence for an employee
      updatePresence(employeeId, date, newValue) {
        this.$emit('update-presence', employeeId, date, newValue);
      },
    },
  };
  </script>
  
  <style scoped>
  .employee-table {
    margin-bottom: 20px;
  }
  
  .employee-table h2 {
    font-size: 1.5rem;
    margin-bottom: 10px;
  }
  
  .employee-table table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .employee-table th,
  .employee-table td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  .employee-table th {
    background-color: #f2f2f2;
  }
  
  .employee-table input[type="checkbox"] {
    cursor: pointer;
  }
  </style>  