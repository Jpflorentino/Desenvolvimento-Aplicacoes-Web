import React, { useContext, useState, useEffect } from "react";
import { Redirect, useParams } from "react-router-dom";
import { ProjectRouteParams } from "../common/types/utils";
import {
  IssueCollection,
  IssueCollectionEntity,
} from "../common/types/issueCollection";
import { Loader } from "../common/utils/loader";
import { ProjectType } from "../common/types/project";
import { useHistory } from "react-router-dom";
import { EditProject } from "./editProject";
import { ProjectDisplay } from "./projectDisplay";
import { IssuesList } from "../issues/issuesList";
import { PageButtons } from "../common/utils/pageButtons";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext } from "../users/userSessionContext";

/// Project component props
type ProjectProps = {
  uri: string;
};

export function Project({ uri }: ProjectProps) {
  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  let { user_id, project_id } = useParams<ProjectRouteParams>();

  // States
  const [issuesList, setIssuesList] = useState<IssueCollection>();
  const [project, setProject] = useState<ProjectType>(null);
  const [issuesURI, setIssuesURI] = useState("");
  const [deleteState, setDeleteState] = useState(false);
  const [editState, setEditState] = useState(false);
  const [pageState, setPageState] = useState({ currentPage: 1, pageSize: 4 });

  const history = useHistory();

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "projects") updateActiveItem("projects");
  }, []);

  /// Requests the project
  useEffect(() => {
    if (project) return;

    // Call fetch method
    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(
      uri.replace("{userid}", user_id).replace("{projectid}", project_id),
      options
    )
      .then((resp) => {
        return resp.json();
      })
      .then((json) => {
        if (json.status == null) {
          setIssuesURI(json.links[2].href);
          setProject(json);
        } else {
          alert(json.detail);
        }
      })
      .catch((err) => alert("There was a problem loading the project"));
  }, []);

  /// Requests the project's issues
  useEffect(() => {
    if (issuesURI == "") {
      return;
    }

    // Call fetch method
    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(
      issuesURI
        .replace("{index}", pageState.currentPage.toString())
        .replace("{pageSize}", pageState.pageSize.toString()),
      options
    )
      .then((resp) => {
        return resp.json();
      })
      .then((json) => {
        if (json.status == null) {
          setIssuesList(json);
        } else {
          alert(json.detail);
        }
      })
      .catch((err) =>
        alert("There was a problem loading the project's issues")
      );
  }, [issuesURI, pageState.currentPage, pageState.pageSize]);

  /// Requests a project removal
  useEffect(() => {
    if (deleteState == false) {
      return;
    }

    const deleteLink = project.actions.find(
      (element) => element.name === "delete-project"
    ).href;

    // Call fetch method
    const options = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(deleteLink, options)
      .then((resp) => {
        if (resp.status > 300) return resp.json(); //to handle the errors
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          history.push("/projects");
        }
      })
      .catch((err) => alert("There was a problem deleting the project"));
  }, [deleteState]);

  /**
   * Handle project deletion
   */
  function handleDelete() {
    if (activeUserSession.userID != project.entities[0].properties.user_id)
      alert("You can't delete other people's projects");
    else setDeleteState(true);
  }

  /**
   * Handle issue creation
   */
  function handleCreateIssue() {
    history.push(`/${user_id}/projects/${project_id}/issues/create`);
  }

  /*
  Instead of using the router we conditionally render the edit component

  Since we need to pass the project content to the Edit project page, in order to avoid
  another request to the API, we can render the EditProject component and the Project component
  in the "same" page

  In this case, when the user clicks "Edit" we render the edit component and pass through the props the 
  project details and all the other necessary data, such as the uri for the PUT request
  */
  if (editState) {
    return <EditProject uri={uri} project={project} />;
  }

  /**
   * Handle project edition
   */
  function handleEditProjectClick() {
    setEditState(true);
  }

  /**
   * Issue click handler
   */
  function handleIssueClick(issue: IssueCollectionEntity) {
    if (activeUserSession.isLoggedIn)
      // todo: esta verificação é necessária quando ja se tem algo parecido no inicio?
      history.push(issue.links[0].href.replace("/group7api", ""));
    else history.push("/loginRegister");
  }

  return !project || !issuesList ? (
    <Loader /> // Not loaded yet
  ) : (
    <div style={styles.myDiv}>
      <ProjectDisplay
        project={project}
        handleDelete={handleDelete}
        handleEditProjectClick={handleEditProjectClick}
        handleCreateIssue={handleCreateIssue}
      />

      {issuesList.entities.length == 0 ? (
        /// There is no issues to show
        <div style={styles.issueStyle}>
          <h3>{project.properties.name} doesn't have any issues!</h3>
        </div>
      ) : (
        /// There is issues to show
        <>
          <div>
            <IssuesList
              issuesList={issuesList.entities}
              handleIssueClick={handleIssueClick}
            />
          </div>
          <PageButtons
            pageState={pageState}
            setPageState={setPageState}
            collection={issuesList}
          />
        </>
      )}
    </div>
  );
}

// Styles
const styles = {
  myDiv: {
    margin: "10px 10px 10px 10px",
  },
  issueStyle: {
    margin: "auto",
    width: "60%",
    padding: 10,
    textAlign: "center" as "center",
  },
};
