import React, { useEffect, useContext } from "react";
import { Container, Header } from "semantic-ui-react";
import logo from "./common/static/images/logo.png";
import { NavbarContext } from "./navbar/navbarContext";

export function Home() {

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => { /// Avoid component side-effects
    if(activeItem !== "home") updateActiveItem("home");
  }, [])

  return (
    <>

      {/* Logo */}
      <div style={styles.logo}>
        <img src={logo} style={{ width: "200px", height: "200px" }} />
      </div>

      {/* Group presentation */}
      <Container text>
        <Header
          as="h1"
          content="Projects App"
          style={styles.title}
        />
        <Header
          as="h2"
          content="Welcome to Daw Project App by Group 7 LEIRT61D"
          style={styles.studentPresentation}
        />
        {getStudent("João Florentino nº43874")}
        {getStudent("João Oliveira nº45225")}
        {getStudent("Ricardo Severino nº45245")}
      </Container>

    </>
  );
}

// Helper function
const getStudent = function (name: string) {
  return (<Header
    as="h4"
    content={name}
    style={styles.studentPresentation}
    />
  );
}

// Styles
const styles = {
  studentPresentation : {
    fontSize: "1.7em",
    fontWeight: "normal",
    marginTop: "0.5em",
  },
  welcome : {
    fontSize: "1.7em",
    fontWeight: "normal",
    marginTop: "0.5em",
  },
  title : {
    fontSize: "4em",
    fontWeight: "normal",
    marginBottom: 0,
    marginTop: "1em",
  },
  logo : {
    margin: "auto",
    textAlign: "center" as "center"
  }
}