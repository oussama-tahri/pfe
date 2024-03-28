import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import apiService from './services/apiService'; // Import the apiService

const app = createApp(App);

// Inject the apiService instance into the app instance
app.config.globalProperties.$apiService = apiService;

app.use(router).mount('#app');