<script setup>
import {Chart, registerables} from "chart.js";
import {onMounted, ref} from "vue";
import {useDataPointStore} from "@/stores/datapoint.store";

let ctx, chart;
Chart.register(...registerables);

const dataPointStore = useDataPointStore();

onMounted(() => {
  ctx = document.getElementById('monthlyBreakdown');
  dataPointStore.fetchMonthlyBreakdown().then(breakdown => {
    chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
        datasets: [{
          label: '# of data points',
          data: dataPointStore.getBreakdown(new Date().getFullYear()),
          borderWidth: 1
        }]
      }
    });
  })
})
</script>

<template>
  <canvas id="monthlyBreakdown"></canvas>
</template>

<style scoped>

</style>