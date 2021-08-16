import React, { useState, useEffect, useContext } from "react";
import { Redirect, useParams } from "react-router-dom";
import { IssueRouteParams } from "../common/types/utils";
import { IssueType } from "../common/types/issue";
import { EditIssue } from "../issues/editIssue";
import { useHistory } from "react-router-dom";
import { EditLabels } from "../issues/editLabels";
import { EditState } from "../issues/editState";
import { CommentsList } from "../comments/commentsList";
import { IssueDisplay } from "./issueDisplay";
import { CommentCollection } from "../common/types/commentCollection";
import { PageButtons } from "../common/utils/pageButtons";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext } from "../users/userSessionContext";

/// Issue component props
type IssueProps = {
  uri: string;
};

export function Issue({ uri }: IssueProps) {
  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession } = useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  /// UseParams
  let { user_id, project_id, issue_id } = useParams<IssueRouteParams>();

  /// States
  const [commentsList, setCommentsList] = useState<CommentCollection>();
  const [issue, setIssue] = useState<IssueType>(null);
  const [commentsURI, setCommentsURI] = useState("");
  const [deleteState, setDeleteState] = useState(false);
  const [editIssue, setEditIssue] = useState(false); /// Instead of using the router we conditionally render the edit component
  const [editLabel, setEditLabel] = useState(false);
  const [editState, setEditState] = useState(false);

  const [pageState, setPageState] = useState({ currentPage: 1, pageSize: 4 });

  const history = useHistory();

  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "projects") updateActiveItem("projects");
  }, []);

  /*
  Get the issue Details
  */
  useEffect(() => {
    if (issue) return;

    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(
      uri
        .replace("{userid}", user_id)
        .replace("{projectid}", project_id)
        .replace("{issueid}", issue_id),
      options
    )
      .then((resp) => {
        return resp.json();
      })
      .then((json) => {
        if (json.status == null) {
          setCommentsURI(json.links[3].href);
          setIssue(json);
        } else {
          alert(json.detail);
        }
      })
      .catch((err) => alert("There was a problem parsing the issue Details"));
  }, []);

  /*
  Get the comments list
  */
  useEffect(() => {
    if (commentsURI == "") {
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
      commentsURI
        .replace("{index}", pageState.currentPage.toString())
        .replace("{pageSize}", pageState.pageSize.toString()),
      options
    )
      .then((resp) => {
        return resp.json();
      })
      .then((json) => {
        if (json.status == null) {
          setCommentsList(json);
        } else {
          alert(json.detail);
        }
      })
      .catch((err) => alert("There was a problem parsing the Comments"));
  }, [commentsURI, pageState.currentPage, pageState.pageSize]);

  /*
  Delete the issue
  */
  useEffect(() => {
    if (deleteState == false) {
      return;
    }

    const deleteLink = issue.actions.find(
      (element) => element.name === "delete-issue"
    ).href;

    const options = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
    };

    fetch(deleteLink, options)
      .then((resp) => {
        if (resp.status > 300) return resp.json();
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          history.push(`/${user_id}/projects/${project_id}`);
        }
      })
      .catch((err) => alert("There was a problem deleting the issue"));
  }, [deleteState]);

  function handleDelete() {
    if (activeUserSession.userID != issue.properties.user_id)
      alert("You can't delete other people's issues");
    else setDeleteState(true);
  }

  function handleEditIssueClick() {
    setEditIssue(true);
  }

  function handleEditLabelClick() {
    setEditLabel(true);
  }

  function handleEditStateClick() {
    setEditState(true);
  }

  function handleCommentClick(comment_id: string) {
    history.push(
      `/${user_id}/projects/${project_id}/issues/${issue_id}/comments/${comment_id}`
    );
  }

  function handleCommentCreate() {
    if (issue.properties.state_name == "archived") {
      alert("You can't add a comment to an archived issue");
    } else {
      history.push(
        `/${user_id}/projects/${project_id}/issues/${issue_id}/comments/create`
      );
    }
  }

  /*
  Since we need to pass the project content to the Edit project page, in order to avoid
  another request to the API, we can render the EditProject component and the Project component
  in the "same" page

  In this case, when the user clicks "Edit" we render the edit component and pass through the props the 
  project details and all the other necessary data, such as the uri for the PUT request
  */
  if (editIssue) {
    return <EditIssue uri={uri} issue={issue} />;
  }

  if (editLabel) {
    return (
      <EditLabels
        changeLabelURI={
          issue.actions.find((action) => action.name == "add-label").href //since the add label and remove label actions have the same href, we can use only one for the edit labels component
        }
        uri={uri}
        issue={issue}
      />
    );
  }

  if (editState) {
    return (
      <EditState
        changeStateURI={
          issue.actions.find((action) => action.name == "change-state").href
        }
        uri={uri}
        issue={issue}
      />
    );
  }

  if (!issue || !commentsList) {
    return <div>empty</div>;
  } else if (commentsList.entities.length == 0) {
    return (
      <div style={{ margin: "10px 10px 10px 10px" }}>
        <IssueDisplay
          issue={issue}
          handleDelete={handleDelete}
          handleEditIssueClick={handleEditIssueClick}
          handleEditLabelClick={handleEditLabelClick}
          handleEditStateClick={handleEditStateClick}
          handleCommentCreate={handleCommentCreate}
        />
        <div
          style={{
            margin: "auto",
            width: "60%",
            padding: 10,
            textAlign: "center",
          }}
        >
          <h3>{issue.properties.name} doesn't have any comments!</h3>
        </div>
      </div>
    );
  } else {
    return (
      <div style={{ margin: "10px 10px 10px 10px" }}>
        <IssueDisplay
          issue={issue}
          handleDelete={handleDelete}
          handleEditIssueClick={handleEditIssueClick}
          handleEditLabelClick={handleEditLabelClick}
          handleEditStateClick={handleEditStateClick}
          handleCommentCreate={handleCommentCreate}
        />
        <div>
          <CommentsList
            commentsList={commentsList.entities}
            handleCommentClick={handleCommentClick}
          />
        </div>
        <PageButtons
          pageState={pageState}
          setPageState={setPageState}
          collection={commentsList}
        />
      </div>
    );
  }
}
