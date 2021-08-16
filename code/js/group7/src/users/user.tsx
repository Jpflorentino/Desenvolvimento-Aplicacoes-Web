import React, { useEffect, useState, useContext } from "react";

import {  User} from "../common/types/user";
import { defaultUserSession } from "../common/types/user";
import {  ProjectCollection, ProjectCollectionEntity } from "../common/types/projectCollection";

import { useHistory, Redirect } from "react-router-dom";
import { Card } from "semantic-ui-react";
import { PageButtons } from "../common/utils/pageButtons";
import { UserDisplay } from "./userDisplay";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext} from "../users/userSessionContext";
import { Loader } from "../common/utils/loader";

export function User({
  uri, /// URI for user details
}: {
  uri: String;
}) {
  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession, updateUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  const [projects, setProjectsList] = useState<ProjectCollection>();
  const [userDetails, setUserDetails] = useState<User>(null);
  const [projectsURI, setProjectsURI] = useState("");
  const [deleteState, setDeleteState] = useState(false);
  const [pageState, setPageState] = useState({ currentPage: 1, pageSize: 4 });

  const history = useHistory();

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if(activeItem !== "user") updateActiveItem("user");
  }, []);


  /// Request user details
  useEffect(() => {
    if (userDetails) return;

    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(uri.replace("{userid}", activeUserSession.userID.toString()), options)
      .then((resp) => {
        return resp.json();
      })
      .then((json) => {
        setProjectsURI(json.links[1].href);
        setUserDetails(json);
      })
      .catch((err) => alert("There was a problem parsing the user Details"));
  }, []);

  /// Request user projects
  useEffect(() => {
    if (projectsURI == "") {
      return;
    }
    
    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(
      projectsURI
        .replace("{index}", pageState.currentPage.toString())
        .replace("{pageSize}", pageState.pageSize.toString()),
      options
    ).then((resp) => {
        return resp.json();
      })
      .then((json) => {
        setProjectsList(json);
      })
      .catch((err) => alert("There was a problem parsing the Projects"));
  }, [projectsURI, pageState.currentPage, pageState.pageSize]);

  /// Request user deletion
  useEffect(() => {
    if (deleteState == false) {
      return;
    }

    const options = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(uri.replace("{userid}", activeUserSession.userID.toString()), options)
      .then((resp) => {
        updateUserSession(defaultUserSession);
        history.push("/");
      })
      .catch((err) => alert("There was a problem deleting the user, " + err));
  }, [deleteState]);

  function handleProjectClick(project: ProjectCollectionEntity) {
    if (activeUserSession.isLoggedIn)
      history.push(project.links[0].href.replace("/group7api", ""));
    else history.push("/loginRegister");
  }

  function handleEditUserClick() {
    history.push("/user/edit");
  }

  function handleDeleteUser() {
    setDeleteState(true);
  }

  function handleCreateProject() {
    history.push("/projects/create");
  }

  return (!userDetails || !projects) ? <Loader/> : /// Loading user details and projects

    /// Loaded
    (
      <div style={styles.myDiv}>

        <UserDisplay
          handleEditUserClick={handleEditUserClick}
          handleDeleteUser={handleDeleteUser}
          handleCreateProject={handleCreateProject}
          userDetails={userDetails}
        />

        {projects.entities.length == 0 ? 

          /// If user has no projects!
          <div style={styles.noUserDiv}>
            <h3>{activeUserSession.username} has no projects!</h3>
          </div>

          :

          /// If user has projects!
          <>
          
            <Card.Group itemsPerRow={2}>
              {projects.entities.map((project: ProjectCollectionEntity) => (
                <Card
                  key={project.properties.project_id}
                  onClick={() => handleProjectClick(project)}
                >
                  <Card.Content>
                    <Card.Header>{project.properties.name}</Card.Header>
                    <Card.Meta>Project</Card.Meta>
                    <Card.Description>
                      {project.properties.description}
                    </Card.Description>
                  </Card.Content>
                </Card>
              ))}
            </Card.Group>

            <PageButtons
              pageState={pageState}
              setPageState={setPageState}
              collection={projects}
            />

          </>

        }

      </div>
    );
}

// Styles
const styles = {
  myDiv : {
    margin: "10px 10px 10px 10px",
  },
  noUserDiv : {
    margin: "auto",
    width: "60%",
    padding: 10,
    textAlign: "center" as "center",
  }
}