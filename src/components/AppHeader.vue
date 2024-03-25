<template>
    <header>
      <h1>{{ title }}</h1>
      <nav>
        <ul>
          <li><a href="#">Home</a></li>
          <li><a href="#">About</a></li>
          <li><a href="#">Contact</a></li>
        </ul>
      </nav>
    </header>
  </template>
  
  <script>
  export default {
    name: 'AppHeader',
    props: {
      title: {
        type: String,
        default: 'Employee Management System' // Default title
      }
    }
  }
  </script>
  
  <style scoped>
  /* Add scoped styles for the Header component */
  
  header {
    background-color: #333;
    color: #fff;
    padding: 20px;
    text-align: center;
  }
  
  h1 {
    margin: 0;
    font-size: 24px;
  }
  
  nav ul {
    list-style: none;
    padding: 0;
  }
  
  nav ul li {
    display: inline;
    margin-right: 20px;
  }
  
  nav ul li a {
    color: #fff;
    text-decoration: none;
  }
  </style>  