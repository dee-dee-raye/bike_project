<template>
  <v-container>
    <v-card max-width="400" elevation="2">
      <v-card-title>Welcome to Bikes</v-card-title>
      <v-card-subtitle>Login to continue</v-card-subtitle>
      <v-card-text>
        <v-form>
          <v-text-field
            label="Username"
            hide-details="auto"
            v-model="username"
          ></v-text-field>
          <v-text-field
            label="Password"
            hide-details="auto"
            type="password"
            v-model="password"
          ></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="login()">Login</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: "Login",
  data: () => ({
    username: "",
    password: "",
    token: "",
  }),
  methods: {
    async login() {
      let args = {};
      args.headers = {
        Accept: "application/json",
        "Content-Type": "application/json",
        "Access-Control-Allow-Headers": "Content-Type",
      };

      args.mode = "cors";

      args.method = "POST";

      const body = {
        username: this.username,
        password: this.password,
      };

      args.body = JSON.stringify(body);

      const response = await fetch("http://localhost:8080/api/v1/login", args);
      const data = await response.json();

      this.token = data["jwt"];

      if (this.token != "") {
        this.$store.dispatch("setUser", {
          username: this.username,
          token: this.token,
        });
        this.$router.push({ name: "Bikes" });
      }
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
