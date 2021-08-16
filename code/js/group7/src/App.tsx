import React, { useState, useEffect } from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import { Links } from "./common/types/links";
import { useFetch } from "./common/hooks/useFetch";
import { Home } from "./home";
import { LoginRegister } from "./users/loginRegister";
import { Loader } from "./common/utils/loader";

import { Navbar } from "./navbar/navbar";
import { Logout } from "./navbar/logout";
import { NavbarContext } from "./navbar/navbarContext";

import { User } from "./users/user";
import { EditUser } from "./users/editUser";
import { UserSession } from "./common/types/user";
import { defaultUserSession } from "./common/types/user";
import { UserSessionContext } from "./users/userSessionContext";

import { CreateProject } from "./projects/createProject";
import { ProjectCollection } from "./projects/projectCollection";
import { Project } from "./projects/project";

import { CreateIssue } from "./issues/createIssue";
import { Issue } from "./issues/issue";

import { Comment } from "./comments/comment";
import { CreateComment } from "./comments/createComment";

function App() {

  /*** Navbar ***/
  const [navbarActiveItem, setNavbarActiveItem] = useState("home");

  const currentNavContextValue = {
    activeItem: navbarActiveItem,
    updateActiveItem: (newItem: string) => {
      setNavbarActiveItem(newItem);
    },
  };

  /*** User Session ***/
  const user_session = JSON.parse(sessionStorage.getItem("userSessionInfo")) || defaultUserSession; /// Get current user session (if none use default session)

  const [userSession, setUserSession] = useState<UserSession>(user_session);

  const currentSessionContextValue = {
    activeUserSession: userSession,
    updateUserSession: (newUserSession: UserSession) => {
      setUserSession(newUserSession);
    },
  };

  // If user state has changed, update session storage
  useEffect(() => {
    console.log("[Info] User is logged in with id: " + userSession.userID);

    sessionStorage.setItem(
      "userSessionInfo",
      JSON.stringify({
        isLoggedIn: userSession.isLoggedIn,
        username: userSession.username,
        userID: userSession.userID,
        authCredentials: userSession.authCredentials,
      })
    );
  }, [userSession]);

  /// Get Home Resoure links
  const host = "http://localhost:9000";
  const fetchState = useFetch(host + "/group7api", "GET", undefined, undefined); /// Get all the links in the API
  const body = fetchState.type == "response" ? fetchState.body : "";

  switch (fetchState.type) {
    case "error": {
      return (
        <div>
          <p>{fetchState.type}</p>
          <p>{fetchState.error}</p>
        </div>
      );
    }

    case "fetching": {
      return <Loader />;
    }

    case "started": {
      return <Loader />;
    }

    case "response": {
      const links: Links = JSON.parse(body);

      /// URIs
      const createUserURI = host + links.userDetails.replace("/{userid}", "");
      const projectCollectionUri = links.allProjects;
      const getUserUri = userSession.isLoggedIn ? "" : host + links.userLogin;

      return (
        <UserSessionContext.Provider value={currentSessionContextValue}>
          <NavbarContext.Provider value={currentNavContextValue}>

            <Router>

              <Navbar activeItem={navbarActiveItem} user={userSession} />

              <div>
                <Switch>

                  <Route exact path="/">
                    <Home/>
                  </Route>

                  <Route exact path="/logout">
                    <Logout/>
                  </Route>

                  <Route exact path="/:user_id/projects/:project_id/issues/:issue_id/comments/create">
                    <CreateComment uri={host + links.commentDetails}/>
                  </Route>

                  <Route exact path="/:user_id/projects/:project_id/issues/:issue_id/comments/:comment_id">
                    <Comment uri={host + links.commentDetails}/>
                  </Route>

                  <Route exact path="/:user_id/projects/:project_id/issues/create">
                    <CreateIssue uri={links.issueDetails} />
                  </Route>

                  <Route exact path="/:user_id/projects/:project_id/issues/:issue_id">
                    <Issue uri={host + links.issueDetails} />
                  </Route>

                  <Route exact path="/projects">
                    <ProjectCollection uri={projectCollectionUri} />
                  </Route>

                  <Route exact path="/user">
                    <User uri={links.userDetails} />
                  </Route>

                  <Route exact path="/loginRegister">
                    <LoginRegister getUserURI={getUserUri} createUserURI={createUserURI}></LoginRegister>
                  </Route>

                  <Route exact path="/:user_id/projects/:project_id">
                    <Project uri={host + links.projectDetails}/>
                  </Route>

                  <Route exact path="/user/edit">
                    <EditUser uri={links.userDetails} />
                  </Route>

                  <Route exact path="/projects/create">
                    <CreateProject uri={links.projectDetails}/>
                  </Route>

                </Switch>
              </div>

            </Router>

          </NavbarContext.Provider>
        </UserSessionContext.Provider>
      );
    }
  }
}

export function app() {
  ReactDOM.render(<App />, document.getElementById("root"));
}
