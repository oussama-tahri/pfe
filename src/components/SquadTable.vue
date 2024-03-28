<template>
  <div class="squad-table">
    <h2>Squad Table</h2>
    <table>
      <thead>
        <tr>
          <th>Squad</th>
          <th v-for="day in daysOfMonth" :key="day">{{ day }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(squad, index) in squads" :key="index">
          <td>{{ squad.name }}</td>
          <td v-for="(presence, index) in squad.presences" :key="index">
            {{ presence.present ? 'Present' : 'Absent' }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'SquadTable',
  props: {
    squads: {
      type: Array,
      required: true,
    },
  },
  computed: {
    // Get list of unique days from squad presences
    daysOfMonth() {
      const daysSet = new Set();
      this.squads?.forEach(squad => {
        squad.presences?.forEach(presence => {
          daysSet.add(presence.date);
        });
      });
      return Array.from(daysSet).sort();
    },
  },
};
</script>

<style scoped>
.squad-table {
  margin-bottom: 20px;
}

.squad-table h2 {
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.squad-table table {
  width: 100%;
  border-collapse: collapse;
}

.squad-table th,
.squad-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.squad-table th {
  background-color: #f2f2f2;
}
</style>