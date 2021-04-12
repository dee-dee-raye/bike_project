<template>
  <v-container>
    <v-card max-width="400" elevation="2">
      <v-card-title>Current Bikes</v-card-title>
      <v-list-item v-for="bike in bikes" :key="bike.name">
        {{ bike.name }}
      </v-list-item>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: "BikeList",
  data: () => ({
    bikes: ["bike1", "bike2"],
  }),
  mounted() {
    this.getBikes();
  },
  methods: {
    getBikes() {
      let args = {};
      args.headers = {
        Accept: "application/json",
        "Content-Type": "application/json",
        "Access-Control-Allow-Headers": "Content-Type",
        Authorization: `Bearer ${this.$store.state.currentUser["token"]}`,
      };

      console.log(this.$store.state);

      args.mode = "cors";

      args.method = "GET";

      fetch("http://localhost:8080/api/v1/bikes", args)
        .then((response) => response.json())
        .then((data) => (this.bikes = data));
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
